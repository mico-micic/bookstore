/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.log4j.Logger;
import org.books.ejb.OrderService;
import org.books.ejb.exception.InvalidOrderStatusException;
import org.books.ejb.exception.OrderNotFoundException;
import org.books.persistence.entity.Order;

/**
 * Order processor bean (message driven).
 * 
 * @author micic
 */
@MessageDriven( 
        activationConfig = {
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
            @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/orderQueue")
})
public class OrderProcessorBean implements MessageListener {
    
    private Logger LOGGER = Logger.getLogger(OrderProcessorBean.class);
    
    @EJB
    private OrderService orderService;
    
    public OrderProcessorBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        
        try {
            // Check message type. The only supported type is OrderTransport.JMS_TYPE_STRING
            String jmsType = message.getJMSType();
            
            LOGGER.info("Received message with id \"" + message.getJMSMessageID() + "\", type: \"" + jmsType + "\"");
            
            if (jmsType != null && jmsType.equals(OrderTransport.JMS_TYPE_STRING)) {

                // Get order ID and process the order
                OrderTransport orderTransport = (OrderTransport)message.getBody(OrderTransport.class);
                processOrder(orderTransport.getOrderId());
                
            } else {
                LOGGER.error("Unknown message type: \"" + jmsType + "\"");
            }
        } catch (JMSException e) {
            LOGGER.error("Error occurred while processing message!", e);
        }
    }
    
    private void processOrder(Long orderId) {
        
        Order.Status newStatus = Order.Status.processing;
        
        try {
            LOGGER.info("Changeng status for order with id \"" + orderId + "\" to \"" + newStatus + "\"");
            
            this.orderService.setOrderStatus(orderId, newStatus);
        } catch (OrderNotFoundException ex) {
            LOGGER.error("Oder with ID " + orderId + " not found!", ex);
        } catch (InvalidOrderStatusException ex) {
            LOGGER.error("Order status cannot be changed!", ex);
        }
    }
}
