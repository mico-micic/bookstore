/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import org.books.persistence.entity.Order;

/**
 * @author micic
 */
@XmlRootElement
public class OrderInfo implements Serializable {

    private Long id;
    private String number;
    private Date date;
    private BigDecimal amount;
    private Order.Status status;

    public OrderInfo() {
        // For JAXB.
    }
    
    public OrderInfo(Long id, String number, Date date, BigDecimal amount, Order.Status status) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Order.Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }
    
    
    
}
