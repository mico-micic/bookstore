package org.books.ejb.impl;

import javax.naming.InitialContext;
import org.books.ejb.OrderService;
import org.books.ejb.exception.OrderNotFoundException;
import org.books.persistence.entity.Order;
import org.books.persistence.testdata.AbstractTestBase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrderServiceBeanTest extends AbstractTestBase {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/OrderService";

    private static OrderService orderService;

    @BeforeClass
    public static void setup() throws Exception {
        orderService = (OrderService) new InitialContext().lookup(ORDER_SERVICE_NAME);
    }

    @Test
    public void testFindOrder() throws OrderNotFoundException {
        Order order = orderService.findOrder("1111-001");
        Assert.assertNotNull(order);
        Assert.assertEquals("1111-001", order.getNumber());
    }

}
