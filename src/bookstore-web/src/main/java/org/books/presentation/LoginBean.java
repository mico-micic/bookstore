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
import org.books.type.EnumActionResult;


/**
 *
 * @author micic
 */
@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    
    @Inject
    private CustomerBean customerBean;
    
    private String username;
    
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    } 
    
    public EnumActionResult doLoginAndContinueTo(String actionName) {
        customerBean.findCustomer();
        return EnumActionResult.valueOf(actionName);
    }
}
