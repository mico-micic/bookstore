package org.books.persistence;

import org.books.persistence.testdata.AbstractTestBase;
import java.util.List;
import org.books.persistence.dao.CustomerDao;
import org.books.persistence.dao.OrderDao;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Order;
import org.books.persistence.testdata.CustomerData;
import org.books.persistence.testdata.OrderData;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author micic
 */
public class OrderDaoTest extends AbstractTestBase {

    @Test
    public void testGetByNumber() {

        Order result = new OrderDao(getEm()).getByNumber(OrderData.O_3333_001.number());
       
        assertNotNull(result);
        
        assertEquals(OrderData.O_3333_001.number(), result.getNumber());
        assertEquals(CustomerData.BONDS_MOTHER.firstName(), result.getCustomer().getFirstName());
    }
    
    @Test
    public void testSearchByCustomerAndYear() {

        OrderDao orderDao = new OrderDao(getEm());
        CustomerDao customerDao = new CustomerDao(getEm());
        
        Customer customer1 = customerDao.getByEmail(CustomerData.BONDS_MOTHER.email());
        Customer customer2 = customerDao.getByEmail(CustomerData.SUPER_USER.email());
        
        List<OrderInfo> result1 = orderDao.searchByCustomerAndYear(customer1, 2014);
        List<OrderInfo> result2 = orderDao.searchByCustomerAndYear(customer1, 1999);
        List<OrderInfo> result3 = orderDao.searchByCustomerAndYear(customer2, 2011);
        List<OrderInfo> result4 = orderDao.searchByCustomerAndYear(customer2, 2010);
        
        assertTrue(result1.size() > 0);
        assertEquals(0, result2.size());
        assertTrue(result3.size() > 0);
        assertTrue(result4.size() > 1);
        
        assertEquals(OrderData.O_3333_001.number(), result1.get(0).getNumber()); 
        assertEquals(OrderData.O_1111_004.number(), result3.get(0).getNumber()); 
        
        assertTrue(checkContainsOrder(OrderData.O_1111_001.number(), result4));
        assertTrue(checkContainsOrder(OrderData.O_1111_002.number(), result4));
        assertTrue(checkContainsOrder(OrderData.O_1111_003.number(), result4));
    }
    
    private boolean checkContainsOrder(String number, List<OrderInfo> orderList) {

        boolean ret = false;

        for (OrderInfo orderInfo : orderList) {
            if (number.equals(orderInfo.getNumber())) {
                ret = true;
                break;
            }
        }

        return ret;
    }
}
