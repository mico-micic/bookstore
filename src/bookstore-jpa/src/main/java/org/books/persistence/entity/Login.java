/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.entity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Sigi
 */
@Entity
@XmlRootElement
public class Login extends IdentifiableObject {

    @Column(
            nullable = false,
            unique = true)
    private String userName;

    @Transient
    private String plainTextPassword;

    @Column(nullable = false)
    private String password;

    @Column
    private String groupname;

    public Login() {
    }

    public Login(String userName, String password) {
        this.userName = userName;
        this.plainTextPassword = password;
    }

    @PrePersist
    public void createSaltAndHashPassword() {
        password = hashString(plainTextPassword);
    }

    @PreUpdate
    public void handlePasswordChanged() {
        if (plainTextPassword != null && !isPasswordValid(plainTextPassword)) {
            password = hashString(plainTextPassword);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        // Reset password, otherwise the handePasswordChanged method will not be called
        this.password = "";
        this.plainTextPassword = password;
    }

    public boolean isPasswordValid(String password) {
        return this.password !=  null && this.password.equals(hashString(password));
    }

    private String hashString(String password) {
        return Base64.getEncoder().encodeToString(hash(password));
    }
    
    private byte[] hash(String password) {
        String saltedPwd = new StringBuilder()
                .append(password)
                .toString();
        try {
            return MessageDigest
                    .getInstance("SHA-256")
                    .digest(saltedPwd.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new IllegalStateException("Error in the static setup of the Hashing-Algorithm");
        }
    }

}
