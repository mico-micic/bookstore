/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb;

import java.util.List;
import javax.ejb.Remote;
import org.books.ejb.exception.BookNotFoundException;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.InvalidOrderStatusException;
import org.books.ejb.exception.OrderNotFoundException;
import org.books.ejb.exception.PaymentFailedException;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.dto.OrderItem;
import org.books.persistence.entity.Order;

/**
 * The remote interface OrderService defines the operations of a bookstore's
 * order service.
 */
@Remote
public interface OrderService {

    /**
     * Cancels an order.
     *
     * @param orderId the order idenifier
     * @throws OrderNotFoundException if no order with the specified identifier
     * exists
     * @throws InvalidOrderStatusException if the order is not cancelable
     * anymore
     */
    void cancelOrder(Long orderId)
            throws
            OrderNotFoundException,
            InvalidOrderStatusException;

    /**
     * Finds an order with the specified identifier.
     *
     * @param orderId the order identifier
     * @return the found order
     * @throws OrderNotFoundException if no order with the specified identifier
     * exists
     */
    Order findOrder(Long orderId)
            throws
            OrderNotFoundException;

    /**
     * Finds an order with the specified number.
     *
     * @param number the order number
     * @return the found order
     * @throws OrderNotFoundException if no order with the specified number
     * exists
     */
    Order findOrder(String number)
            throws
            OrderNotFoundException;

    /**
     * Places an order on the bookstore.
     *
     * @return information on the created order
     * @param customerId the customer identifier
     * @param items the order items
     *
     * @throws CustomerNotFoundException if no customer with the specified
     * identifier exists
     * @throws BookNotFoundException if an order item reference a book that does
     * not exist
     * @throws PaymentFailedException if an error occurs during the credit card
     * payment
     */
    OrderInfo placeOrder(Long customerId, List<OrderItem> items)
            throws
            CustomerNotFoundException,
            BookNotFoundException,
            PaymentFailedException;

    /**
     * Searches for orders of a particular customer and year.
     *
     * @return information on matching orders (may be empty)
     * @param customerId the customer identifier
     * @param year the order year.
     *
     * @throws CustomerNotFoundException if no customer with the specified
     * identifier exists
     */
    List<OrderInfo> searchOrders(Long customerId, Integer year)
            throws
            CustomerNotFoundException;

}
