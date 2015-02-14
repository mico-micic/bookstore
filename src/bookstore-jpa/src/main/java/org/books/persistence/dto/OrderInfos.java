/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author micic
 */
@XmlRootElement(name = "orderInfoes")
public class OrderInfos {

    @XmlElement(name = "orderInfo")
    public List<OrderInfo> orderInfos = null;

    public OrderInfos set(List<OrderInfo> infos) {
        this.orderInfos = infos;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + this.orderInfos;
    }
}
