package org.books.ejb.impl;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import org.books.ejb.CustomerService;
import org.books.ejb.exception.InvalidCredentialsException;
import org.books.persistence.testdata.AbstractTestBase;
import org.books.persistence.testdata.LoginData;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CustomerServiceBeanTest extends AbstractTestBase {

    private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/CustomerService";

    private static CustomerService customerService;

    @BeforeClass
    public static void setup() throws Exception {
        customerService = (CustomerService) new InitialContext().lookup(CUSTOMER_SERVICE_NAME);
    }

    @Test
    public void testAuthenticateCustomerSuccess() throws InvalidCredentialsException {
        customerService.authenticateCustomer(LoginData.SUPER_USER.email(), LoginData.SUPER_USER.password());
    }

    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerNoEMail() throws InvalidCredentialsException {
        try {
            customerService.authenticateCustomer(null, LoginData.SUPER_USER.password());
            Assert.fail("Hier erwarten wir eine InvalidCredentialsException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof InvalidCredentialsException) {
                throw ((InvalidCredentialsException)e.getCause());
            }
        }
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerEmptyEMail() throws InvalidCredentialsException {
        try {
            customerService.authenticateCustomer("", LoginData.SUPER_USER.password());
            Assert.fail("Hier erwarten wir eine InvalidCredentialsException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof InvalidCredentialsException) {
                throw ((InvalidCredentialsException)e.getCause());
            }
        }
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerNoPassword() throws InvalidCredentialsException {
        try {
            customerService.authenticateCustomer(LoginData.SUPER_USER.email(), null);
            Assert.fail("Hier erwarten wir eine InvalidCredentialsException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof InvalidCredentialsException) {
                throw ((InvalidCredentialsException)e.getCause());
            }
        }
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerEmptyPassword() throws InvalidCredentialsException {
        try {
            customerService.authenticateCustomer(LoginData.SUPER_USER.email(), "");
            Assert.fail("Hier erwarten wir eine InvalidCredentialsException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof InvalidCredentialsException) {
                throw ((InvalidCredentialsException)e.getCause());
            }
        }
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerInvalidEMail() throws InvalidCredentialsException {
        try {
            customerService.authenticateCustomer("some_invalid_email@email.ch", LoginData.SUPER_USER.password());
            Assert.fail("Hier erwarten wir eine InvalidCredentialsException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof InvalidCredentialsException) {
                throw ((InvalidCredentialsException)e.getCause());
            }
        }
    }
    
    @Test(expected = InvalidCredentialsException.class)
    public void testAuthenticateCustomerInvalidPassword() throws InvalidCredentialsException {
        try {
            customerService.authenticateCustomer(LoginData.SUPER_USER.email(), "some_invalid_password");
            Assert.fail("Hier erwarten wir eine InvalidCredentialsException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof InvalidCredentialsException) {
                throw ((InvalidCredentialsException)e.getCause());
            }
        }
    }
}
