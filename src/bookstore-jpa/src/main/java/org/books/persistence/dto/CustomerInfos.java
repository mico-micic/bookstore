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
@XmlRootElement(name = "customerInfoes")
public class CustomerInfos {

    @XmlElement(name = "customerInfo")
    public List<CustomerInfo> customerInfos = null;

    public CustomerInfos set(List<CustomerInfo> infos) {
        this.customerInfos = infos;
        return this;
    }

    @Override
    public String toString() {
        return "CustomerInfos " + this.customerInfos;
    }
}
