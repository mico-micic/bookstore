/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.log4j.Logger;
import org.books.ejb.MailServiceLocal;
import org.books.ejb.OrderServiceRemote;
import org.books.ejb.exception.InvalidOrderStatusException;
import org.books.ejb.exception.OrderNotFoundException;
import org.books.persistence.entity.Customer;
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
    
    public static final int STATUS_AUTO_CHANGE_TIMEOUT = 60000; //60s
    
    private final Logger LOGGER = Logger.getLogger(OrderProcessorBean.class);
     
    @EJB
    private OrderServiceRemote orderService;
    
    @Resource
    private TimerService timerService;
    
    @EJB
    private MailServiceLocal mailService;
    
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
            
            // Create timer to change the status to "shipped" after a delay of 60s
            Timer timer = timerService.createSingleActionTimer(STATUS_AUTO_CHANGE_TIMEOUT, new TimerConfig(orderId, true));
            
            LOGGER.info("Single action timer \"" + timer + "\" with " + STATUS_AUTO_CHANGE_TIMEOUT + "ms delay created.");
                    
            // Send processing e-mail
            mailService.sendOrderProcessingEMailAsync(this.orderService.findOrder(orderId));
            
        } catch (OrderNotFoundException ex) {
            LOGGER.error("Oder with ID " + orderId + " not found!", ex);
        } catch (InvalidOrderStatusException ex) {
            LOGGER.error("Order status cannot be changed!", ex);
        }
    }
    
    @Timeout
    public void changeOrderStatusToShipped(Timer timer) {
        
        LOGGER.info("Timer \"" + timer + "\" expired. Timer info (order id): " + timer.getInfo());
        
        try {
            Order order = orderService.findOrder((Long)timer.getInfo());
            Order.Status currentStatus = order.getStatus();
            Order.Status newStatus = Order.Status.shipped;
            
            if (currentStatus.equals(Order.Status.processing)) {
                this.orderService.setOrderStatus(order, newStatus);
                
                LOGGER.info("Status for order with id \"" + order.getId() + "\" changed to \"" + newStatus + "\".");
                
                // Send shipped e-mail
                mailService.sendOrderShippedEMailAsync(order);
                
            } else {
                LOGGER.info("Status for order with id \"" + order.getId() 
                        + "\" not changed because of the current status \"" + currentStatus + "\".");
            }
        } catch (OrderNotFoundException ex) {
            LOGGER.error("Status update failed!", ex);
        } catch (InvalidOrderStatusException ex) {
            LOGGER.error("Status update failed because of current order status!", ex);
        }
    }
}
