/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sigi
 */
@XmlRootElement
@XmlType(propOrder = {"isbn", "quantity"})
public class OrderItem implements Serializable {

    private String isbn;
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(String isbn, Integer quantity) {
        this.isbn = isbn;
        this.quantity = quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
