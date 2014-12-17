package org.books.ejb.impl;

import javax.naming.InitialContext;
import org.books.ejb.CustomerService;
import org.books.ejb.exception.InvalidCredentialsException;
import org.books.persistence.testdata.AbstractTestBase;
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

    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerNoEMail() {
        
        Assert.fail("Hier erwarten wir eine InvalidCanonicalizerException!");
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerNoPassword() {
        
        Assert.fail("Hier erwarten wir eine InvalidCanonicalizerException!");
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerInvalidEMail() {
        
        Assert.fail("Hier erwarten wir eine InvalidCanonicalizerException!");
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerInvalidPassword() {
        
        Assert.fail("Hier erwarten wir eine InvalidCanonicalizerException!");
    }
}
