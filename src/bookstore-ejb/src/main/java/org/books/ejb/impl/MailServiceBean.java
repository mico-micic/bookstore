/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.books.ejb.MailServiceLocal;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Order;

/**
 * E-Mail service bean.
 * 
 * @author micic
 */
@Stateless
public class MailServiceBean implements MailServiceLocal {

    private final Logger LOGGER = Logger.getLogger(MailServiceBean.class);
    
    @Resource(name="mail/bookstore") 
    private Session mailSession;
    
    private void sendMail(String email, String subject, String text) {
        
        MimeMessage msg = new MimeMessage(this.mailSession);
        try {
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.setSubject(subject);
            msg.setText(text);
            
            // Send e-mail
            Transport.send(msg);
        } catch (MessagingException ex) {
            LOGGER.error("Error sending e-mail to \"" + email +"\"", ex);
        }
    }
    
    @Override
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendOrderProcessingEMailAsync(Order order) {
        
        String email = order.getCustomer().getEmail();
        String subject = "Order processing started!";
        
        String text = getEMailIntroduction(order.getCustomer());
        text += "Your order with number \""+ order.getNumber() + "\" has now the status \"processing\"!" + "\n\n";
        text += getEMailEnding();
        
        LOGGER.info("Sending order processing e-mail to: " + email);
        
        sendMail(email, subject, text);
    }
    
    @Override
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendOrderShippedEMailAsync(Order order) {
        
        String email = order.getCustomer().getEmail();
        String subject = "Order shipped!";
        
        String text = getEMailIntroduction(order.getCustomer());
        text += "Your order with number \""+ order.getNumber() + "\" has been shipped!" + "\n";
        text += "We hope you enjoy the book(s)!"+ "\n\n";
        text += getEMailEnding();
        
        LOGGER.info("Sending order shipped e-mail to: " + email);
        
        sendMail(email, subject, text);
    }
    
    private String getEMailIntroduction(Customer customer) {
        return "Hi " + customer.getFirstName() + "!\n\n";
    }
    
    private String getEMailEnding() {
        return "Best regards," + "\n" + "Your bookstore";
    }
}
