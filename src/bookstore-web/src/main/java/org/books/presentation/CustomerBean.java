/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.application.MessageFactory;
import org.books.application.exception.EmailAlreadyUsedException;
import org.books.application.exception.InvalidOrderStatusException;
import org.books.application.exception.OrderNotFoundException;
import org.books.persistence.Cart;
import org.books.persistence.Customer;
import org.books.persistence.Order;
import org.books.type.EnumActionResult;
import org.books.type.MessageKey;

/**
 * Allows Value-Bindings in order do handle Customer and Account Details.
 *
 * @author Sigi
 * @author Mico
 */
@Named
@SessionScoped
public class CustomerBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    private boolean loggedIn = false;

    private Customer customer;

    private String wayBack;

    private Integer year = 2014;

    private List<Order> allOrdersOfYear;
    
    private Order order;
    
    private Cart tmpCart = new Cart();

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    public Cart getTmpCart() {
        return this.tmpCart;
    }
    
    public Order getOrder() {
        return this.order;
    }
    
    public List<Order> getAllOrdersOfYear() {
        findAllOrdersOfYear();
        return allOrdersOfYear;
    }

    public List<Integer> getPossibleYears() {
        List<Integer> possibleYears = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            possibleYears.add(Year.now().getValue() - i);
        }
        return possibleYears;
    }

    public void findAllOrdersOfYear() {
        if (year == null) {
            year = Year.now().getValue();
        }
        allOrdersOfYear = bookstore.searchOrders(customer, year)
                .stream()
                .sorted((o1, o2) -> (o1.getDate().compareTo(o2.getDate())) * -1)
                .collect(Collectors.toList());
    }

    public EnumActionResult showOrder(String orderNumber) {
        try {
            this.order = bookstore.findOrder(orderNumber);
            
            // Create and fill a temporary cart to show the order content
            tmpCart.reset();
            this.order.getItems().forEach(lineItem -> tmpCart.addLineItem(lineItem));
        } catch (OrderNotFoundException ex) {
            MessageFactory.error(MessageKey.ORDER_NOT_FOUND, orderNumber);
            return null;
        }
        return EnumActionResult.ORDER_DETAILS;
    }
    
    public void cancelOrder(Order order) {
        
        try {
            this.bookstore.cancelOrder(order.getId());
        } catch (OrderNotFoundException ex) {
            MessageFactory.error(MessageKey.ORDER_NOT_FOUND, order.getNumber());
        } catch (InvalidOrderStatusException ex) {
            MessageFactory.error(MessageKey.ORDER_CANNOT_BE_CANCELLED);
        }
    }

    public EnumActionResult editAccount(String wayBack) {
        this.wayBack = wayBack;
        return EnumActionResult.EDIT_ACCOUNT;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public EnumActionResult updateCustomer() {
        try {
            bookstore.updateCustomer(customer);
            MessageFactory.info(MessageKey.SAVE_SUCCESSFUL);
            return navigateBack();
        } catch (EmailAlreadyUsedException ex) {
            MessageFactory.error(MessageKey.SAVE_UNSUCCESSFUL);
            return null;
        }
    }

    public EnumActionResult createCustomer() {

        try {
            bookstore.registerCustomer(this.customer);
            MessageFactory.info(MessageKey.REGISTRATION_SUCCESSFUL);
            this.setLoggedIn(true);
            return EnumActionResult.ACCOUNT;
        } catch (EmailAlreadyUsedException ex) {
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            MessageFactory.info(MessageKey.EMAIL_ALREADY_EXISTS);

            return null;
        }
    }

    public Customer getCustomer() {
        if (this.customer == null) {
            this.customer = new Customer();
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public EnumActionResult navigateBack() {
        if (wayBack == null || "".equals(wayBack)) {
            return EnumActionResult.HOME;
        }
        return EnumActionResult.valueOf(wayBack);
    }
}
