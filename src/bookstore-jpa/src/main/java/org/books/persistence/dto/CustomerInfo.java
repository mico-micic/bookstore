/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author micic
 */
@XmlRootElement
@XmlType(propOrder = {"firstName", "lastName", "email"})
public class CustomerInfo implements Serializable {

    @XmlAttribute
    private Long id;
    
    private String firstName;
    private String lastName;
    private String email;

    public CustomerInfo() {
    }

    public CustomerInfo(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String name) {
        lastName = name;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String mail) {
        email = mail;
    }

}
