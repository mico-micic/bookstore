/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.books.ejb.OrderServiceLocal;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.persistence.dto.OrderInfos;

/**
 * @author micic
 */
@Path("admin")
public class AdminResource extends AbstractResource {

    @EJB
    private OrderServiceLocal orderService;

    @GET
    @Path("orders/search")
    @Produces({"application/xml", "application/json"})
    public OrderInfos searchOrders(@QueryParam("customerId") Long customerId, @QueryParam("year") Integer year) {
        validateNotNull(customerId);
        validateNotNull(year);
        try {
            return new OrderInfos().set(orderService.searchOrders(customerId, year));
        } catch (CustomerNotFoundException ex) {
            throw new NotFoundException("Order not found!");
        }
    }
}
