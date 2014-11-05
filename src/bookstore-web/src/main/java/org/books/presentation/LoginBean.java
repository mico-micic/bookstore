/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import org.books.application.Bookstore;
import org.books.application.MessageFactory;
import org.books.application.exception.InvalidCredentialsException;
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

    public String doLogin() {
        
        try {
            bookstore.authenticateCustomer(this.email, this.password);
        } catch (InvalidCredentialsException ex) {
            MessageFactory.error(MessageKey.INVALID_USER);
            return null;
        }
        
        MessageFactory.info(MessageKey.LOGIN_SUCCESS);
        return null;
    }
}
