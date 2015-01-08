package org.books.ejb.impl;

import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import org.books.ejb.CustomerService;
import org.books.ejb.OrderService;
import org.books.ejb.exception.BookNotFoundException;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.OrderNotFoundException;
import org.books.ejb.exception.PaymentFailedException;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.dto.OrderItem;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Order;
import org.books.persistence.testdata.AbstractTestBase;
import org.books.persistence.testdata.CustomerData;
import org.books.persistence.testdata.IsbnNumber;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrderProcessorBeanTest extends AbstractTestBase {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/OrderService";
    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/CustomerService";
    
    private static final int QUEUE_WAIT_TIME = 2000;
    
    private static OrderService orderService;
    
    private static CustomerService customerService;

    @BeforeClass
    public static void setup() throws Exception {
        orderService = (OrderService) new InitialContext().lookup(ORDER_SERVICE_NAME);
        customerService = (CustomerService) new InitialContext().lookup(CUSTOMER_SERVICE_NAME);
    }

    private List<OrderItem> getOrderItems() {
        
        List<OrderItem> items = new ArrayList<>();
        
        items.add(new OrderItem(IsbnNumber.ISBN_978_3527710706.number(), 1));
        items.add(new OrderItem(IsbnNumber.ISBN_978_3836217408.number(), 2));
        
        return items;
    }
    
    @Test
    public void processNewOrderCorrectly() throws CustomerNotFoundException, BookNotFoundException, 
            PaymentFailedException, InterruptedException, OrderNotFoundException {
        
        Customer customer = customerService.findCustomer(CustomerData.SUPER_USER.email());    
        OrderInfo info = orderService.placeOrder(customer.getId(), getOrderItems());
        
        // Wait some time to give the queue a chance to process the order
        Thread.sleep(QUEUE_WAIT_TIME);
        
        // Check the order status
        Assert.assertEquals(Order.Status.processing, orderService.findOrder(info.getId()).getStatus());
    }
    
    @Test
    public void processInvalidOrderNumber() {
        
        
    }
    
    @Test
    public void processInvalidOrderStatus() {
        
        
    }
     
    
    @Test
    public void autoChangeToShipped() {
        
        
    }
    
    @Test
    public void cancelOrderBeforeShipped() {
        
        
    }

}
