/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.books.ejb.CustomerServiceLocal;
import org.books.ejb.OrderServiceLocal;
import org.books.ejb.exception.BookNotFoundException;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.EmailAlreadyUsedException;
import org.books.ejb.exception.InvalidOrderStatusException;
import org.books.ejb.exception.OrderNotFoundException;
import org.books.ejb.exception.PaymentFailedException;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.dto.OrderRequest;
import org.books.persistence.dto.Registration;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Order;
import org.bookstore.rs.exception.ConflictException;
import org.bookstore.rs.exception.PaymentRequiredException;
import org.bookstore.rs.exception.PreconditionFailedException;

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
        validateNotNull(number);
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
    public List<OrderInfo> searchOrders(@QueryParam("customerId") Long customerId, @QueryParam("year") Integer year) {
        validateNotNull(customerId);
        validateNotNull(year);
        try {
            return orderService.searchOrders(customerId, year);
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
            throw new PreconditionFailedException("The order is not in a valid status to be deleted!");
        }
    }

}
