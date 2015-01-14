/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb;

import javax.ejb.Local;
import org.books.persistence.entity.Order;

/**
 *
 * @author micic
 */
@Local
public interface MailServiceLocal {
    
    public void sendOrderProcessingEMailAsync(Order order);
    
    public void sendOrderShippedEMailAsync(Order order);
}
