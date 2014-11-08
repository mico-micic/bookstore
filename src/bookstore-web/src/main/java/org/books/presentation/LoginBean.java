/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.books.application.Bookstore;
import org.books.application.MessageFactory;
import org.books.application.exception.InvalidCredentialsException;
import org.books.persistence.Customer;
import org.books.type.EnumActionResult;
import org.books.type.MessageKey;

/**
 *
 * @author micic
 */
@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private Bookstore bookstore;

    @Inject
    private CustomerBean customerBean;

    private String nextPage;
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EnumActionResult openAuthorized(String nextPage) {

        this.nextPage = nextPage;

        if (this.customerBean.getCustomer() == null) {
            return EnumActionResult.LOGIN;
        } else {
            return EnumActionResult.valueOf(nextPage);
        }
    }

    public EnumActionResult doLogin() {

        Customer customer = this.customerBean.getCustomer();

        if (customer == null) {
            try {
                customer = bookstore.authenticateCustomer(this.email, this.password);
                this.customerBean.setCustomer(customer);
            } catch (InvalidCredentialsException ex) {
                MessageFactory.error(MessageKey.INVALID_USER);
                return null;
            }

            MessageFactory.info(MessageKey.LOGIN_SUCCESS);
        }

        return (this.nextPage == null || this.nextPage.isEmpty())
                ? EnumActionResult.HOME
                : EnumActionResult.valueOf(this.nextPage);
    }
    
    public EnumActionResult doLogout() {
        this.customerBean.setCustomer(null);
        MessageFactory.info(MessageKey.LOGOUT_SUCCESS);
        return EnumActionResult.HOME;
    }
}
