/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.application.MessageFactory;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.EmailAlreadyUsedException;
import org.books.persistence.Customer;
import org.books.persistence.Order;
import org.books.type.EnumActionResult;
import org.books.type.MessageKey;

/**
 * @author Sigi
 */
@Named
@SessionScoped
public class CustomerBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    private Customer customer;
    private String wayBack;

    public void findCustomer() {
        try {
            customer = bookstore.findCustomer("alice@example.org");
        } catch (CustomerNotFoundException ex) {
            // TODO
        }
    }

    public List<Order> getAllOrdersOfYear(Integer year) {
        return bookstore.searchOrders(customer, year);
    }

    public EnumActionResult editAccount(String wayBack) {
        this.wayBack = wayBack;
        return EnumActionResult.EDIT_ACCOUNT;
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

    public Customer getCustomer() {
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
