/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import org.books.persistence.converter.AesEncryptorConverter;

/**
 * @author Sigi
 */
@Entity
public class Login extends IdentifiableObject {

    @Column(
            nullable = false,
            unique = true)
    private String userName;

    @Column(nullable = false)
    @Convert(converter = AesEncryptorConverter.class)
    // This Encryption ist just a dummy-security.
    // It will be replaced by JEE-Security later in the project.
    private String password;

    public Login() {
    }

    public Login(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
