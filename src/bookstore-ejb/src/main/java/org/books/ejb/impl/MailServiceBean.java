/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.books.ejb.MailService;

/**
 * E-Mail service bean.
 * 
 * @author micic
 */
@Stateless
public class MailServiceBean implements MailService {

    private final Logger LOGGER = Logger.getLogger(MailServiceBean.class);
    
    @Resource(name="mail/bookstore") 
    private Session mailSession;
    
    @Override
    @Asynchronous
    public void sendMailAsync(String email, String subject, String text) {
        
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
}
