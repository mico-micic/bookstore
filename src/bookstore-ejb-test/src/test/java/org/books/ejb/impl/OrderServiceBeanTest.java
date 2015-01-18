package org.books.ejb.impl;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
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
import org.books.persistence.entity.LineItem;
import org.books.persistence.entity.Order;
import org.books.persistence.testdata.AbstractTestBase;
import org.books.persistence.testdata.CustomerData;
import org.books.persistence.testdata.OrderData;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrderServiceBeanTest extends AbstractTestBase {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/OrderService!org.books.ejb.OrderServiceRemote";
    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/CustomerService!org.books.ejb.CustomerServiceRemote";

    private static OrderServiceRemote orderService;
    private static CustomerServiceRemote customerService;

    @BeforeClass
    public static void before() throws Exception {
        orderService = (OrderServiceRemote) new InitialContext().lookup(ORDER_SERVICE_NAME);
        customerService = (CustomerServiceRemote) new InitialContext().lookup(CUSTOMER_SERVICE_NAME);
    }

    @Test
    public void testFindOrderWithOrderId() throws OrderNotFoundException {
        
        Order test = orderService.findOrder(OrderData.O_1111_002.number());
        Order order = orderService.findOrder(test.getId());
        
        Assert.assertNotNull(order);
        Assert.assertEquals(test.getId(), order.getId());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testFindOrderWithInvalidOrderId() throws Throwable {
        try {
            orderService.findOrder(111L);
        } catch (EJBException ex) {
            throw ex.getCause();
        }
    }

    @Test
    public void testFindOrderWithOrderNumber() throws OrderNotFoundException {
        String number = OrderData.ORDER_WITH_LINE_ITEMS.number();
        Order order = orderService.findOrder(number);
        Assert.assertNotNull(order);
        Assert.assertEquals(number, order.getNumber());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testFindOrderWithInvalidOrderNumber() throws Throwable {
        try {
            orderService.findOrder("");
        } catch (EJBException ex) {
            throw ex.getCause();
        }
    }

    @Test
    public void testPlaceOrder() throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException, OrderNotFoundException {
        Customer customer = customerService.findCustomer(CustomerData.HANS_WURST.email());
        OrderInfo orderInfo = orderService.placeOrder(customer.getId(), someValidOrderItems());

        Assert.assertNotNull(orderInfo);
        Assert.assertEquals(Order.Status.accepted, orderInfo.getStatus());

        Assert.assertNotNull(orderService.findOrder(orderInfo.getId()));
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testPlaceOrderWithInvalidCustomer() throws Throwable {
        try {
            orderService.placeOrder(1L, someValidOrderItems());
        } catch (EJBException ex) {
            throw ex.getCause();
        }
    }

    @Test(expected = BookNotFoundException.class)
    public void testPlaceOrderWithInvalidBook() throws Throwable {
        Customer customer = customerService.findCustomer(CustomerData.HANS_WURST.email());
        List<OrderItem> orderItems = someValidOrderItems();
        orderItems.add(new OrderItem("AN_INVALID_ISBN", 7));
        try {
            orderService.placeOrder(customer.getId(), orderItems);
        } catch (EJBException ex) {
            throw ex.getCause();
        }
    }

    @Test(expected = PaymentFailedException.class)
    public void testPlaceOrderWithInvalidPaymentInfos() throws Throwable {
        Customer customer = customerService.findCustomer(CustomerData.HANS_WURST.email());
        customer.setId(null);
        customer.setEmail("someArbitraryEMail@jaf0.ch");
        customer.getCreditCard().setNumber("AN_INVALID_CREDIT_CARD_NUMBER");
        Long registeredCustomerId = customerService.registerCustomer(customer, "bliBlaBlu-Password");

        try {
            orderService.placeOrder(registeredCustomerId, someValidOrderItems());
        } catch (EJBException ex) {
            throw ex.getCause();
        }
    }

    @Test(expected = InvalidOrderStatusException.class)
    public void testCancelOrder() throws Throwable {
        Customer customer = customerService.findCustomer(CustomerData.HANS_WURST.email());
        OrderInfo orderInfo = orderService.placeOrder(customer.getId(), someValidOrderItems());

        Assert.assertNotNull(orderInfo);
        Assert.assertEquals(Order.Status.accepted, orderInfo.getStatus());

        Order order = orderService.findOrder(orderInfo.getId());
        Assert.assertNotNull(order);
        orderService.setOrderStatus(order, Order.Status.shipped);

        order = orderService.findOrder(order.getId());
        Assert.assertEquals("Order-Status must be canceled after canceling the order.",
                Order.Status.canceled, order.getStatus());

        try {
            orderService.cancelOrder(order.getId());
        } catch (EJBException ex) {
            throw ex.getCause();
        }
    }

    private List<OrderItem> someValidOrderItems() throws OrderNotFoundException {
        List<OrderItem> orderItems = new ArrayList<>();
        Order order = orderService.findOrder(OrderData.ORDER_WITH_LINE_ITEMS.number());
        List<LineItem> lineItems = order.getItems();
        for (LineItem lineItem : lineItems) {
            orderItems.add(new OrderItem(lineItem.getBook().getIsbn(), lineItem.getQuantity()));
        }
        return orderItems;
    }

}
