/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.entity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

/**
 * @author Sigi
 */
@Entity
public class Login extends IdentifiableObject {

    @Column(
            nullable = false,
            unique = true)
    private String userName;

    @Transient
    private String plainTextPassword;

    @Column(nullable = false)
    private byte[] password;

    @Column(nullable = false)
    // @Convert(converter = AesEncryptorConverter.class)
    private String salt;

    public Login() {
    }

    public Login(String userName, String password) {
        this.userName = userName;
        this.plainTextPassword = password;
    }

    @PrePersist
    public void createSaltAndHashPassword() {
        salt = UUID.randomUUID().toString();
        password = hash(plainTextPassword, salt);
    }

    @PreUpdate
    public void handlePasswordChanged() {
        if (plainTextPassword != null && !isPasswordValid(plainTextPassword)) {
            password = hash(plainTextPassword, salt);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.plainTextPassword = password;
    }

    public boolean isPasswordValid(String password) {
        return Arrays.equals(this.password, hash(password, salt));
    }

    private byte[] hash(String password, String salt) {
        String saltedPwd = new StringBuilder()
                .append(salt)
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
