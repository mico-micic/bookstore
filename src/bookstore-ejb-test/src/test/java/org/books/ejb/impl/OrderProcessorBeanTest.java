package org.books.ejb.impl;

import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import org.books.ejb.CustomerServiceRemote;
import org.books.ejb.OrderServiceRemote;
import org.books.ejb.exception.BookNotFoundException;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.InvalidOrderStatusException;
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
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class OrderProcessorBeanTest extends AbstractTestBase {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/OrderService!org.books.ejb.OrderServiceRemote";
    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/CustomerService!org.books.ejb.CustomerServiceRemote";
    
    private static final int QUEUE_WAIT_TIME = 5000;
    
    private static OrderServiceRemote orderService;
    
    private static CustomerServiceRemote customerService;

    @BeforeClass
    public static void setup() throws Exception {
        orderService = (OrderServiceRemote) new InitialContext().lookup(ORDER_SERVICE_NAME);
        customerService = (CustomerServiceRemote) new InitialContext().lookup(CUSTOMER_SERVICE_NAME);
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
    public void autoChangeToShipped() throws CustomerNotFoundException, BookNotFoundException, 
            PaymentFailedException, InterruptedException, OrderNotFoundException {
        
        Customer customer = customerService.findCustomer(CustomerData.SUPER_USER.email());    
        OrderInfo info = orderService.placeOrder(customer.getId(), getOrderItems());
        
        // Wait some time to give the queue a chance to process the order and update the status
        Thread.sleep(QUEUE_WAIT_TIME + OrderProcessorBean.STATUS_AUTO_CHANGE_TIMEOUT);
        
        // Check the order status
        Assert.assertEquals(Order.Status.shipped, orderService.findOrder(info.getId()).getStatus());
    }
    
    @Test
    public void cancelOrderBeforeShipped() throws CustomerNotFoundException, BookNotFoundException, 
            PaymentFailedException, InterruptedException, OrderNotFoundException, InvalidOrderStatusException {
        
        Customer customer = customerService.findCustomer(CustomerData.SUPER_USER.email());    
        OrderInfo info = orderService.placeOrder(customer.getId(), getOrderItems());
        
        // Wait some time to give the queue a chance to process the order
        Thread.sleep(QUEUE_WAIT_TIME);
        
        // Cancel the order
        orderService.cancelOrder(info.getId());
        
        // Wait again
        Thread.sleep(QUEUE_WAIT_TIME + OrderProcessorBean.STATUS_AUTO_CHANGE_TIMEOUT);
        
        // Check the order status
        Assert.assertEquals(Order.Status.canceled, orderService.findOrder(info.getId()).getStatus());
    }
}
