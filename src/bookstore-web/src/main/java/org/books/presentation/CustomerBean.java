/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.books.persistence.Customer;

/**
 * @author Sigi
 */
@Named
@SessionScoped
public class CustomerBean implements Serializable {
       
    private Customer customer;
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }   
}
