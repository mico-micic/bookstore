/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.books.ejb.OrderServiceLocal;
import org.books.ejb.exception.BookNotFoundException;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.InvalidOrderStatusException;
import org.books.ejb.exception.OrderNotFoundException;
import org.books.ejb.exception.PaymentFailedException;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.dto.OrderInfos;
import org.books.persistence.dto.OrderRequest;
import org.books.persistence.entity.Order;
import org.bookstore.rs.exception.PaymentRequiredException;

/**
 * @author Sigi
 */
@Path("orders")
public class OrdersResource extends AbstractResource {

    @Context
    private HttpServletResponse response;

    @EJB
    private OrderServiceLocal orderService;

    @POST
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Response placeOrder(OrderRequest orderRequest) {
        validateNotNull(orderRequest);
        validateNotNull(orderRequest.getCustomerId());
        validateNotNull(orderRequest.getItems());
        try {
            OrderInfo orderInfo = orderService.placeOrder(orderRequest.getCustomerId(), orderRequest.getItems());
            return Response
                    .status(Status.CREATED.getStatusCode())
                    .entity(orderInfo)
                    .build();
        } catch (CustomerNotFoundException ex) {
            throw new NotFoundException("Customer not found!");
        } catch (BookNotFoundException ex) {
            throw new NotFoundException("Book not found!");
        } catch (PaymentFailedException ex) {
            throw new PaymentRequiredException();
        }
    }

    @GET
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Order findById(@PathParam("id") Long id) {
        validateNotNull(id);
        try {
            return orderService.findOrder(id);
        } catch (OrderNotFoundException ex) {
            throw new NotFoundException("Order not Found!");
        }
    }

    @GET
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Order findByNumber(@QueryParam("number") String number) {
        validateNotBlank(number);
        try {
            return orderService.findOrder(number);
        } catch (OrderNotFoundException ex) {
            throw new NotFoundException("Order not Found!");
        }
    }

    @GET
    @Path("search")
    @Consumes({"application/xml", "application/json"})
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

    @DELETE
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void cancelOrder(@PathParam("id") Long id) {
        validateNotNull(id);
        try {
            orderService.cancelOrder(id);
        } catch (OrderNotFoundException ex) {
            throw new NotFoundException("Order not Found!");
        } catch (InvalidOrderStatusException ex) {
            throw new ForbiddenException("The order is not in a valid status to be deleted!");
        }
    }
}
