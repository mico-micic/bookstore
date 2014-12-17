package org.books.ejb.impl;

import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import org.books.ejb.CatalogService;
import org.books.ejb.CustomerService;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.entity.Book;
import org.books.persistence.testdata.AbstractTestBase;
import org.books.persistence.testdata.IsbnNumber;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CustomerServiceBeanTest extends AbstractTestBase {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-ejb/CustomerService";

    private static CustomerService customerService;

    @BeforeClass
    public static void setup() throws Exception {
        customerService = (CustomerService) new InitialContext().lookup(CUSTOMER_SERVICE_NAME);
    }

    @Test
    public void testAuthenticateCustomerSuccess() {
 
    }

    @Test(expected = InvalidCanonicalizerException.class)
    public void testAuthenticateCustomerNoEMail() {
        
        Assert.fail("Hier erwarten wir eine InvalidCanonicalizerException!");
    }
    
    @Test(expected = InvalidCanonicalizerException.class)
    public void testAuthenticateCustomerNoPassword() {
        
        Assert.fail("Hier erwarten wir eine InvalidCanonicalizerException!");
    }
    
    @Test(expected = InvalidCanonicalizerException.class)
    public void testAuthenticateCustomerInvalidEMail() {
        
        Assert.fail("Hier erwarten wir eine InvalidCanonicalizerException!");
    }
    
    @Test(expected = InvalidCanonicalizerException.class)
    public void testAuthenticateCustomerInvalidPassword() {
        
        Assert.fail("Hier erwarten wir eine InvalidCanonicalizerException!");
    }
}
