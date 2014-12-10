/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.math.BigDecimal;
import java.sql.Date;
import org.books.persistence.Order;

/**
 *
 * @author micic
 */
public class OrderInfo {
    
    private final Integer id;
    private final String number;
    private final Date date;
    private final BigDecimal amount;
    private final Order.Status status;

    public OrderInfo(Integer id, String number, Date date, BigDecimal amount, Order.Status status) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }
    
    public Integer getId() {
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
}
