/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sigi
 */
@XmlRootElement
@XmlType(propOrder = {"customerId", "items"})
public class OrderRequest {

    private Long customerId;
    private List<OrderItem> items;

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

}
