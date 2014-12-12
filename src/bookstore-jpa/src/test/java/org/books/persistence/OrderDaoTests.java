package org.books.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.books.persistence.dao.CustomerDao;
import org.books.persistence.dao.OrderDao;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Order;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author micic
 */
public class OrderDaoTests {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static TestDataFactory tdf;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("bookstore");
        em = emf.createEntityManager();
        tdf = new TestDataFactory(em);
        tdf.prepareTestData();
    }

    @AfterClass
    public static void tearDownClass() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @Test
    public void testGetByNumber() {

        Order result = new OrderDao(this.em).getByNumber("3333-001");
       
        assertNotNull(result);
        
        assertEquals("3333-001", result.getNumber());
        assertEquals("Bonds_Mother", result.getCustomer().getFirstName());
    }
    
    @Test
    public void testSearchByCustomerAndYear() {

        OrderDao orderDao = new OrderDao(this.em);
        CustomerDao customerDao = new CustomerDao(this.em);
        
        Customer customer1 = customerDao.getByEmail("bonds_mother@007.ch");
        Customer customer2 = customerDao.getByEmail("superuser@email.com");
        
        List<OrderInfo> result1 = orderDao.searchByCustomerAndYear(customer1, 2014);
        List<OrderInfo> result2 = orderDao.searchByCustomerAndYear(customer1, 1999);
        List<OrderInfo> result3 = orderDao.searchByCustomerAndYear(customer2, 2011);
        List<OrderInfo> result4 = orderDao.searchByCustomerAndYear(customer2, 2010);
        
        assertTrue(result1.size() > 0);
        assertEquals(0, result2.size());
        assertTrue(result3.size() > 0);
        assertTrue(result4.size() > 1);
        
        assertEquals("3333-001", result1.get(0).getNumber()); 
        assertEquals("1111-004", result3.get(0).getNumber()); 
        
        assertTrue(checkContainsOrder("1111-001", result4));
        assertTrue(checkContainsOrder("1111-002", result4));
        assertTrue(checkContainsOrder("1111-003", result4));
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
