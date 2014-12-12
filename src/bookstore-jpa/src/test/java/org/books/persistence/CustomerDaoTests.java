package org.books.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.books.persistence.dao.BookDao;
import org.books.persistence.dao.CustomerDao;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.Customer;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author micic
 */
public class CustomerDaoTests {

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
    public void testGetByEmail() {

        Customer result = new CustomerDao(this.em).getByEmail("hans@wurst.ch");
       
        assertNotNull(result);
        assertEquals("Hans", result.getFirstName());
    }
    
    @Test
    public void testSearchByNamePart() {
        
        CustomerDao customerDao = new CustomerDao(this.em);

        List<CustomerInfo> result1 = customerDao.searchByNamePart("wurst");
        List<CustomerInfo> result2 = customerDao.searchByNamePart("hans");
        List<CustomerInfo> result3 = customerDao.searchByNamePart("AMES");
        List<CustomerInfo> result4 = customerDao.searchByNamePart("rst");
        List<CustomerInfo> result5 = customerDao.searchByNamePart("BOND");
        
        assertTrue(result1.size() > 0);
        assertTrue(result2.size() > 0);
        assertTrue(result3.size() > 0);
        assertTrue(result4.size() > 0);
        assertTrue(result5.size() > 1);
        
        assertEquals("Hans", result1.get(0).getFirstName());
        assertEquals("Hans", result2.get(0).getFirstName());
        assertEquals("James", result3.get(0).getFirstName());
        assertEquals("Hans", result4.get(0).getFirstName());
        
        assertTrue(checkContainsCustomer("James", result5));
        assertTrue(checkContainsCustomer("Bonds_Mother", result5));
    }
    
    private boolean checkContainsCustomer(String firstName, List<CustomerInfo> customerList) {

        boolean ret = false;

        for (CustomerInfo customerInfo : customerList) {
            if (firstName.equals(customerInfo.getFirstName())) {
                ret = true;
                break;
            }
        }

        return ret;
    }
   
}
