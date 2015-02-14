/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.assertj.core.api.Assertions.assertThat;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.dto.OrderItem;
import org.books.persistence.dto.OrderRequest;
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
        Customer customer = customersTarget
                .queryParam("email", customerData.email())
                .request(MediaType.APPLICATION_XML)
                .get()
                .readEntity(Customer.class);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(customer.getId());

        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(IsbnNumber.ISBN_978_3527710706.number(), 1));
        items.add(new OrderItem(IsbnNumber.ISBN_978_3836217408.number(), 2));
        items.add(new OrderItem(IsbnNumber.ISBN_978_3836217880.number(), 3));
        orderRequest.setItems(items);

        return Entity.xml(orderRequest);
    }

}
