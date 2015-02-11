/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.books.ejb.CustomerServiceLocal;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.persistence.entity.Customer;

/**
 * @author Sigi
 */
@Path("customers")
public class CustomersResource extends AbstractResource {

    @EJB
    private CustomerServiceLocal customerService;

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
    @Path("search")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Customer findByName(@QueryParam("name") String name) {
        validateNotBlank(name);
        try {
            return customerService.findCustomer(name);
        } catch (CustomerNotFoundException ex) {
            throw new NotFoundException("No customer with the given id found!");
        }
    }

}
