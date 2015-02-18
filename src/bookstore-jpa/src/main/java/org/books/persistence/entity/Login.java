/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.entity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;
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

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

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
        return this.password != null && this.password.equals(hashString(password));
    }

    private String hashString(String password) {
        return DatatypeConverter.printHexBinary(hash(password));
    }

    private byte[] hash(String password) {

        try {
            return MessageDigest
                    .getInstance("SHA-256")
                    .digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new IllegalStateException("Error in the static setup of the Hashing-Algorithm");
        }
    }

}
