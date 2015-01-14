/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import java.io.Serializable;

/**
 *
 * @author micic
 */
public class OrderTransport implements Serializable {

    public static String JMS_TYPE_STRING = OrderTransport.class.getName();
    
    private final Long orderId;

    public OrderTransport(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
