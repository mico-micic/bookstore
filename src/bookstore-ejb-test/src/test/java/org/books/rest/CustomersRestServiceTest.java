/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest;

import java.util.List;
import java.util.UUID;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.assertj.core.api.Assertions.assertThat;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.dto.Registration;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author micic
 */
public class CustomersRestServiceTest {

    private final WebTarget customersTarget = ClientBuilder.newClient().target("http://localhost:8080/bookstore/rest/customers");

    @Test
    public void testRegisterCustomerSuccess() {
        Registration reg = createRegistration();
        Response postResponse = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg));

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        Assert.assertEquals(MediaType.TEXT_PLAIN, postResponse.getMediaType().toString());
        Long newId = postResponse.readEntity(Long.class);
        Assert.assertTrue(newId > 0);
    }

    @Test
    public void testRegisterCustomerInvalidData() {
        Registration reg = createRegistration();
        reg.getCustomer().setAddress(null);
        Response postResponse = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg));

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        Assert.assertEquals(MediaType.TEXT_PLAIN, postResponse.getMediaType().toString());
    }

    @Test
    public void testRegisterCustomerConflict() {
        Registration reg = createRegistration();
        Response postResponse1 = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg));

        assertThat(postResponse1.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        Assert.assertEquals(MediaType.TEXT_PLAIN, postResponse1.getMediaType().toString());
        Long newId = postResponse1.readEntity(Long.class);
        Assert.assertTrue(newId > 0);

        Response postResponse2 = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg));

        assertThat(postResponse2.getStatus()).isEqualTo(Response.Status.CONFLICT.getStatusCode());
        Assert.assertEquals(MediaType.TEXT_PLAIN, postResponse1.getMediaType().toString());
    }

    @Test
    public void testFindCustomerByIdSuccess() {
        Long customerId = registerNewCustomer();

        Response postResponse = customersTarget
                .path(customerId.toString())
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Assert.assertEquals(MediaType.APPLICATION_XML, postResponse.getMediaType().toString());
        Customer customer = postResponse.readEntity(Customer.class);

        Assert.assertNotNull(customer);
        Assert.assertNotNull(customer.getAddress());
        Assert.assertNotNull(customer.getCreditCard());
        Assert.assertNotNull(customer.getEmail());
        Assert.assertNotNull(customer.getFirstName());
        Assert.assertNotNull(customer.getLastName());
        Assert.assertNotNull(customer.getId());
    }

    @Test
    public void testFindCustomerByIdInvalidId() {
        Response postResponse = customersTarget
                .path("99999999")
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testFindCustomerByIdNoId() {
        Response postResponse = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testFindCustomerByEMailSuccess() {
        Registration reg = createRegistration();
        Response postResponse1 = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg));

        assertThat(postResponse1.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        Long newId = postResponse1.readEntity(Long.class);
        Assert.assertTrue(newId > 0);

        Response postResponse = customersTarget
                .queryParam("email", reg.getCustomer().getEmail())
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Assert.assertEquals(MediaType.APPLICATION_XML, postResponse.getMediaType().toString());
        Customer customer = postResponse.readEntity(Customer.class);

        Assert.assertEquals(reg.getCustomer().getEmail(), customer.getEmail());
        Assert.assertEquals(reg.getCustomer().getFirstName(), customer.getFirstName());
        Assert.assertEquals(reg.getCustomer().getLastName(), customer.getLastName());
        Assert.assertNotNull(customer);
        Assert.assertNotNull(customer.getAddress());
        Assert.assertNotNull(customer.getCreditCard());
        Assert.assertNotNull(customer.getId());
    }

    @Test
    public void testFindCustomerByEMailNoEmail() {
        Response postResponse = customersTarget
                .queryParam("email", "")
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testFindCustomerByEMailNotFound() {
        Response postResponse = customersTarget
                .queryParam("email", "xy@fail.cch")
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testFindCustomerNameSuccess() {
        Registration reg1 = createRegistration();
        reg1.getCustomer().setFirstName("FindMePlease1");
        Response postResponse1 = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg1));

        assertThat(postResponse1.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        Long newId1 = postResponse1.readEntity(Long.class);
        Assert.assertTrue(newId1 > 0);

        Registration reg2 = createRegistration();
        reg2.getCustomer().setFirstName("FindMePlease2");
        Response postResponse2 = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg2));

        assertThat(postResponse2.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        Long newId2 = postResponse2.readEntity(Long.class);
        Assert.assertTrue(newId2 > 0);

        Response postResponse = customersTarget
                .path("search")
                .queryParam("name", "FindMePlease")
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Assert.assertEquals(MediaType.APPLICATION_XML, postResponse.getMediaType().toString());
        List<CustomerInfo> customerInfos = postResponse.readEntity(new GenericType<List<CustomerInfo>>() {
        });

        Assert.assertNotNull(customerInfos);
        Assert.assertTrue(customerInfos.size() > 0);
        Assert.assertTrue(containsEMail(customerInfos, reg1.getCustomer().getEmail()));
        Assert.assertTrue(containsEMail(customerInfos, reg2.getCustomer().getEmail()));
    }

    @Test
    public void testFindCustomerNameNoName() {
        Response postResponse = customersTarget
                .path("search")
                .queryParam("name", "")
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testFindCustomerNameNoMatch() {
        Response postResponse = customersTarget
                .path("search")
                .queryParam("name", "someInvalidNamePart")
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Assert.assertEquals(MediaType.APPLICATION_XML, postResponse.getMediaType().toString());
        List<CustomerInfo> customerInfos = postResponse.readEntity(new GenericType<List<CustomerInfo>>() {
        });
        Assert.assertTrue(customerInfos.isEmpty());
    }

    @Test
    public void testUpdateCustomerSuccess() {
        Registration reg = createRegistration();
        Response postResponse = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg));

        assertThat(postResponse.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        Assert.assertEquals(MediaType.TEXT_PLAIN, postResponse.getMediaType().toString());
        Long newId = postResponse.readEntity(Long.class);
        Assert.assertTrue(newId > 0);
        
        reg.getCustomer().setFirstName("ChangedFirstName");
        reg.getCustomer().setId(newId);
        Response putResponse = customersTarget
                .path(newId.toString())
                .request()
                .put(Entity.xml(reg));
        
        assertThat(putResponse.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        
        Response getResponse = customersTarget
                .path(newId.toString())
                .request(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_XML)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Assert.assertEquals(MediaType.APPLICATION_XML, getResponse.getMediaType().toString());
        Customer customer = getResponse.readEntity(Customer.class);
        
        Assert.assertEquals(reg.getCustomer().getFirstName(), customer.getFirstName());
    }

    // -----------------------------------------------------------------------------
    // Private Helpers:
    // -----------------------------------------------------------------------------
    private boolean containsEMail(List<CustomerInfo> customerInfos, String eMail) {

        boolean ret = false;

        for (CustomerInfo info : customerInfos) {
            if (info.getEmail().equals(eMail)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    private Registration createRegistration() {
        Registration registration = new Registration();
        Customer customer = new Customer();
        customer.setAddress(new Address("Street", "city", "PC", "country"));
        customer.setCreditCard(new CreditCard(CreditCard.Type.Visa, "5105105105105100", 01, 2015));
        customer.setEmail(UUID.randomUUID() + "@mail.org");
        customer.setFirstName("NewRegFirstname");
        customer.setLastName("NewRegLastName");
        registration.setCustomer(customer);
        registration.setPassword("new@customer");
        return registration;
    }

    private Long registerNewCustomer() {
        Registration reg = createRegistration();
        Response postResponse1 = customersTarget
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.xml(reg));

        assertThat(postResponse1.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        Assert.assertEquals(MediaType.TEXT_PLAIN, postResponse1.getMediaType().toString());
        Long newId = postResponse1.readEntity(Long.class);
        Assert.assertTrue(newId > 0);

        return newId;
    }

}
