/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.books.ejb.CustomerServiceLocal;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.EmailAlreadyUsedException;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.dto.Registration;
import org.books.persistence.entity.Customer;
import org.bookstore.rs.exception.ConflictException;

/**
 * @author Sigi
 */
@Path("customers")
public class CustomersResource extends AbstractResource {

    @EJB
    private CustomerServiceLocal customerService;

    @POST
    @Consumes({"application/xml", "application/json"})
    @Produces({"text/plain"})
    public Long registerCustomer(Registration registration) {
        validateNotNull(registration);
        validateNotNull(registration.getCustomer());
        validateNotBlank(registration.getPassword());
        try {
            return customerService.registerCustomer(registration.getCustomer(), registration.getPassword());
        } catch (EmailAlreadyUsedException ex) {
            throw new ConflictException("EMail already in use!");
        }
    }

    @GET
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Customer findById(@PathParam("id") Long id) {
        validateNotNull(id);
        try {
            return customerService.findCustomer(id);
        } catch (CustomerNotFoundException ex) {
            throw new NotFoundException("No customer with the given id found!");
        }
    }

    @GET
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Customer findByEmail(@QueryParam("email") String email) {
        validateNotBlank(email);
        try {
            return customerService.findCustomer(email);
        } catch (CustomerNotFoundException ex) {
            throw new NotFoundException("No customer with the given id found!");
        }
    }

    @GET
    @Path("search")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public List<CustomerInfo> searchByName(@QueryParam("name") String name) {
        validateNotBlank(name);
        return customerService.searchCustomers(name);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void updateCustomer(Customer customer, @PathParam("id") Long id) {
        validateNotNull(customer);
        if (!id.equals(customer.getId())) {
            throw new BadRequestException("The ID of the CustomerObject is not equal to the given Resource-ID!");
        }
        try {
            customerService.updateCustomer(customer);
        } catch (EmailAlreadyUsedException ex) {
            throw new ConflictException("EMail already in use!");
        } catch (CustomerNotFoundException ex) {
            throw new NotFoundException("The Customer with the given ID does not exist!");
        }
    }

}
