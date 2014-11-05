/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.application.exception.CustomerNotFoundException;
import org.books.persistence.Customer;

/**
 * @author Sigi
 */
@Named
@SessionScoped
public class CustomerBean implements Serializable {
    
    @Inject
    private Bookstore bookstore;
    
    private Customer customer;

    public void findCustomer() {
        try {
            customer = bookstore.findCustomer("alice@example.org");
        } catch (CustomerNotFoundException ex) {
            // TODO
        }
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
}
