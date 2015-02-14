/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs.validation;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import org.books.persistence.dto.OrderInfo;

/**
 *
 * @author micic
 */
@Provider
@Produces(MediaType.APPLICATION_XML) 
public class OrderInfoXmlValidationWriter extends AbstractXmlValidationWriter<OrderInfo> {

    private static final String XSD_FILE = "orders.xsd";

    public OrderInfoXmlValidationWriter() {
        super(XSD_FILE);
    }
}
