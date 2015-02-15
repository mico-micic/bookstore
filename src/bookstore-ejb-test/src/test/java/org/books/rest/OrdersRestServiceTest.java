/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.assertj.core.api.Assertions.assertThat;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.dto.OrderItem;
import org.books.persistence.dto.OrderRequest;
import org.books.persistence.dto.Registration;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Order;
import org.books.persistence.testdata.CustomerData;
import static org.books.persistence.testdata.CustomerData.*;
import org.books.persistence.testdata.IsbnNumber;
import org.junit.Test;

/**
 * @author Sigi
 */
public class OrdersRestServiceTest {

    private final WebTarget customersTarget = ClientBuilder.newClient().target("http://localhost:8080/bookstore/rest/customers");
    private final WebTarget ordersTarget = ClientBuilder.newClient().target("http://localhost:8080/bookstore/rest/orders");
    private final WebTarget adminTarget = ClientBuilder.newClient().target("http://localhost:8080/bookstore/rest/admin");

    @Test
    public void testPlaceOrderForNotExistingCustomer() {
        OrderRequest orderRequest = createSomeOrderRequestWithCustomerId(0L);
        Response postResponse = ordersTarget
                .request(MediaType.APPLICATION_XML)
                .post(Entity.xml(orderRequest));
        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testPlaceOrderForNotExistingBook() {
        OrderRequest orderRequest = createSomeOrderRequestWithCustomerId(loadCustomer(SUPER_USER).getId());
        orderRequest.getItems().add(new OrderItem("someInvalidISBN", 1));
        Response postResponse = ordersTarget
                .request(MediaType.APPLICATION_XML)
                .post(Entity.xml(orderRequest));
        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testPlaceOrderWithMissingPayment() {
        Registration registration = createRegistrationWithInvalidCreditCard();
        Response response = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(registration));
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        Long customerId = response.readEntity(Long.class);

        OrderRequest orderRequest = createSomeOrderRequestWithCustomerId(customerId);
        Response postResponse = ordersTarget
                .request(MediaType.APPLICATION_XML)
                .post(Entity.xml(orderRequest));
        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.PAYMENT_REQUIRED.getStatusCode());
    }

    @Test
    public void testPlaceOrderAndFindItById() {
        OrderInfo orderInfo = placeOrderFor(HANS_WURST);

        Response getResponse = ordersTarget
                .path(String.valueOf(orderInfo.getId()))
                .request(MediaType.APPLICATION_XML)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Order order = getResponse.readEntity(Order.class);
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(orderInfo.getId());
    }

    @Test
    public void testFindOrderWithInvalidId() {
        Response getResponse = ordersTarget
                .path(String.valueOf(0))
                .request(MediaType.APPLICATION_XML)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testPlaceOrderAndFindItByNumber() {
        OrderInfo orderInfo = placeOrderFor(BONDS_MOTHER);

        Response getResponse = ordersTarget
                .queryParam("number", orderInfo.getNumber())
                .request(MediaType.APPLICATION_XML)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Order order = getResponse.readEntity(Order.class);
        assertThat(order).isNotNull();
        assertThat(order.getNumber()).isEqualTo(orderInfo.getNumber());
    }

    @Test
    public void testFindOrderWithInvalidNumber() {
        Response getResponse = ordersTarget
                .queryParam("number", 0)
                .request(MediaType.APPLICATION_XML)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testFindOrderWithMissingNumber() {
        Response getResponse = ordersTarget
                // No OrderNumber: .queryParam("number", 0)
                .request(MediaType.APPLICATION_XML)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testPlaceOrderAndSearchByCustomer() {
        placeOrderFor(HANS_WURST);

        Response response = adminTarget
                .path("orders/search")
                .queryParam("customerId", loadCustomer(HANS_WURST).getId())
                .queryParam("year", Year.now().getValue())
                .request(MediaType.APPLICATION_XML)
                .get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        List<OrderInfo> orderInfos = response.readEntity(new GenericType<List<OrderInfo>>() {
        });
        assertThat(orderInfos).isNotNull();
        assertThat(orderInfos.size()).isGreaterThan(0);
    }

    @Test
    public void testSearchByCustomerWithoutCustomerId() {
        Response response = adminTarget
                .path("orders/search")
                .queryParam("customerId", loadCustomer(HANS_WURST).getId())
                // No Year: .queryParam("year", Year.now().getValue())
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testSearchByCustomerWithNotExistingCustomer() {
        Response response = adminTarget
                .path("orders/search")
                .queryParam("customerId", 0)
                .queryParam("year", Year.now().getValue())
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testSearchByCustomerWithoutYear() {
        Response response = adminTarget
                .path("orders/search")
                // No Customer: .queryParam("customerId", loadCustomer(HANS_WURST).getId())
                .queryParam("year", Year.now().getValue())
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testPlaceOrderAndDeleteIt() {
        OrderInfo orderInfo = placeOrderFor(SUPER_USER);

        Response response = ordersTarget
                .path(String.valueOf(orderInfo.getId()))
                .request()
                .delete();

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void testDeleteOrderWithInvalidId() {
        Response response = ordersTarget
                .path(String.valueOf(99999))
                .request()
                .delete();

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    // -----------------------------------------------------------------------------
    // Private Helpers:
    // -----------------------------------------------------------------------------
    private OrderInfo placeOrderFor(CustomerData customerData) {
        Response postResponse = ordersTarget
                .request(MediaType.APPLICATION_XML)
                .post(orderFor(customerData));
        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        OrderInfo orderInfo = postResponse.readEntity(OrderInfo.class);
        assertThat(orderInfo).isNotNull();
        return orderInfo;
    }

    private Entity<OrderRequest> orderFor(CustomerData customerData) {
        Customer customer = loadCustomer(customerData);
        OrderRequest orderRequest = createSomeOrderRequestWithCustomerId(customer.getId());
        return Entity.xml(orderRequest);
    }

    private OrderRequest createSomeOrderRequestWithCustomerId(Long customerId) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(customerId);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(IsbnNumber.ISBN_978_3527710706.number(), 1));
        items.add(new OrderItem(IsbnNumber.ISBN_978_3836217408.number(), 2));
        items.add(new OrderItem(IsbnNumber.ISBN_978_3836217880.number(), 3));
        orderRequest.setItems(items);
        return orderRequest;
    }

    private Customer loadCustomer(CustomerData customerData) {
        return loadCustomer(customerData.email());
    }

    private Customer loadCustomer(String email) {
        Customer customer = customersTarget
                .queryParam("email", email)
                .request(MediaType.APPLICATION_XML)
                .get()
                .readEntity(Customer.class);
        return customer;
    }

    private Registration createRegistrationWithInvalidCreditCard() {
        Registration registration = new Registration();
        Customer customer = new Customer();
        customer.setAddress(new Address("Street", "city", "PC", "country"));
        customer.setCreditCard(new CreditCard(CreditCard.Type.Visa, "1000000000000000", 11, 2015));
        customer.setEmail(UUID.randomUUID() + "@mail.org");
        customer.setFirstName("NewCustomer");
        customer.setLastName("LastNameOfNewCustomer");
        registration.setCustomer(customer);
        registration.setPassword("pass@word");
        return registration;
    }

}
