package org.books.persistence;

import org.books.persistence.testdata.AbstractTestBase;
import java.util.List;
import org.books.persistence.dao.CustomerDao;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author micic
 */
public class CustomerDaoTest extends AbstractTestBase {

    @Test
    public void testGetByEmail() {

        Customer result = new CustomerDao(getEm()).getByEmail("hans@wurst.ch");
       
        assertNotNull(result);
        assertEquals("Hans", result.getFirstName());
    }
    
    @Test
    public void testSearchByNamePart() {
        
        CustomerDao customerDao = new CustomerDao(getEm());

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
