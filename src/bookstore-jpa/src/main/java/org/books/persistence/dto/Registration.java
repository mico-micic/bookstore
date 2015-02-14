/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import javax.xml.bind.annotation.XmlRootElement;
import org.books.persistence.entity.Customer;

/**
 * @author Sigi
 */
@XmlRootElement
public class Registration {

    private Customer customer;
    private String password;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getPassword() {
        return password;
    }

}
