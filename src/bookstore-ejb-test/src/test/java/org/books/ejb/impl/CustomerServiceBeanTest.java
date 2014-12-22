package org.books.ejb.impl;

import java.util.List;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import org.books.ejb.CustomerService;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.EmailAlreadyUsedException;
import org.books.ejb.exception.InvalidCredentialsException;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.books.persistence.testdata.AbstractTestBase;
import org.books.persistence.testdata.CustomerData;
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
                throw ((InvalidCredentialsException) e.getCause());
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
                throw ((InvalidCredentialsException) e.getCause());
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
                throw ((InvalidCredentialsException) e.getCause());
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
                throw ((InvalidCredentialsException) e.getCause());
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
                throw ((InvalidCredentialsException) e.getCause());
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
                throw ((InvalidCredentialsException) e.getCause());
            }
        }
    }

    @Test
    public void testChangePasswordSuccess() throws InvalidCredentialsException, CustomerNotFoundException {

        String newPw = "theNewPw";

        // Check old password
        customerService.authenticateCustomer(LoginData.PW_CHANGE_TEST.email(), LoginData.PW_CHANGE_TEST.password());

        // Change password
        customerService.changePassword(LoginData.PW_CHANGE_TEST.email(), newPw);

        // Check new password
        customerService.authenticateCustomer(LoginData.PW_CHANGE_TEST.email(), newPw);

        // Reset old password
        customerService.changePassword(LoginData.PW_CHANGE_TEST.email(), LoginData.PW_CHANGE_TEST.password());

        // Check old password
        customerService.authenticateCustomer(LoginData.PW_CHANGE_TEST.email(), LoginData.PW_CHANGE_TEST.password());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordNoEMail() throws InvalidCredentialsException, CustomerNotFoundException {

        String newPw = "theNewPw";

        try {
            customerService.changePassword(null, newPw);
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordNoPassword() throws InvalidCredentialsException, CustomerNotFoundException {

        try {
            customerService.changePassword(LoginData.PW_CHANGE_TEST.email(), null);
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testChangePasswordInvalidEmail() throws InvalidCredentialsException, CustomerNotFoundException {

        String newPw = "theNewPw";

        try {
            customerService.changePassword("invalid@e-mail.ch", newPw);
            Assert.fail("Hier erwarten wir eine CustomerNotFoundException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof CustomerNotFoundException) {
                throw ((InvalidCredentialsException) e.getCause());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordEmptyPassword() throws InvalidCredentialsException, CustomerNotFoundException {

        try {
            customerService.changePassword(LoginData.PW_CHANGE_TEST.email(), "");
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test
    public void testFindCustomerByEMailSuccess() throws CustomerNotFoundException {

        Customer testCustomer1 = customerService.findCustomer(CustomerData.HANS_WURST.email());

        Assert.assertNotNull(testCustomer1);
        Assert.assertEquals(CustomerData.HANS_WURST.firstName(), testCustomer1.getFirstName());
        Assert.assertEquals(CustomerData.HANS_WURST.lastName(), testCustomer1.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCustomerByEMailNoEmail() throws CustomerNotFoundException {

        try {
            customerService.findCustomer((String) null);
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCustomerByEMailEmptyEmail() throws CustomerNotFoundException {

        try {
            customerService.findCustomer("");
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testFindCustomerByEMailWrongEmail() throws CustomerNotFoundException {

        try {
            customerService.findCustomer("wrong_customer@e-mail.ch");
            Assert.fail("Hier erwarten wir eine CustomerNotFoundException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof CustomerNotFoundException) {
                throw ((CustomerNotFoundException) e.getCause());
            }
        }
    }

    @Test
    public void testFindCustomerByIdSuccess() throws CustomerNotFoundException {

        Customer testCustomer1 = customerService.findCustomer(CustomerData.HANS_WURST.email());
        Customer testCustomer2 = customerService.findCustomer(testCustomer1.getId());

        Assert.assertNotNull(testCustomer2);
        Assert.assertEquals(testCustomer1.getId(), testCustomer2.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCustomerByIdNoId() throws CustomerNotFoundException {

        try {
            customerService.findCustomer((Long) null);
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testFindCustomerByIdWrongId() throws CustomerNotFoundException {

        try {
            customerService.findCustomer(Long.MAX_VALUE);
            Assert.fail("Hier erwarten wir eine CustomerNotFoundException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof CustomerNotFoundException) {
                throw ((CustomerNotFoundException) e.getCause());
            }
        }
    }

    private Customer getNewCustomer() {

        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email@new.ch";

        String city = "city";
        String country = "country";
        String postalCode = "code";
        String street = "street";

        CreditCard.Type cardType = CreditCard.Type.MasterCard;
        String cardNumber = "11a";
        Integer cardExpirationMonth = 5;
        Integer cardExpirationYear = 2015;

        Address newAddress = new Address();
        newAddress.setCity(city);
        newAddress.setCountry(country);
        newAddress.setPostalCode(postalCode);
        newAddress.setStreet(street);

        CreditCard newCard = new CreditCard();
        newCard.setExpirationMonth(cardExpirationMonth);
        newCard.setExpirationYear(cardExpirationYear);
        newCard.setNumber(cardNumber);
        newCard.setType(cardType);

        Customer newCustomer = new Customer();
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);
        newCustomer.setEmail(email);
        newCustomer.setAddress(newAddress);
        newCustomer.setCreditCard(newCard);

        return newCustomer;
    }

    @Test
    public void testRegisterCustomerSuccess() throws EmailAlreadyUsedException, CustomerNotFoundException, InvalidCredentialsException {

        Customer newCustomer = getNewCustomer();
        newCustomer.setEmail("customer-success@email.ch");
        String password = "newPassword";

        customerService.registerCustomer(newCustomer, password);

        Customer testCustomer = customerService.findCustomer(newCustomer.getEmail());

        Assert.assertNotNull(testCustomer);
        Assert.assertEquals(newCustomer.getFirstName(), testCustomer.getFirstName());
        Assert.assertEquals(newCustomer.getLastName(), testCustomer.getLastName());
        Assert.assertEquals(newCustomer.getEmail(), testCustomer.getEmail());
        Assert.assertEquals(newCustomer.getAddress().getCity(), testCustomer.getAddress().getCity());
        Assert.assertEquals(newCustomer.getAddress().getCountry(), testCustomer.getAddress().getCountry());
        Assert.assertEquals(newCustomer.getAddress().getPostalCode(), testCustomer.getAddress().getPostalCode());
        Assert.assertEquals(newCustomer.getAddress().getStreet(), testCustomer.getAddress().getStreet());
        Assert.assertEquals(newCustomer.getCreditCard().getExpirationMonth(), testCustomer.getCreditCard().getExpirationMonth());
        Assert.assertEquals(newCustomer.getCreditCard().getExpirationYear(), testCustomer.getCreditCard().getExpirationYear());
        Assert.assertEquals(newCustomer.getCreditCard().getNumber(), testCustomer.getCreditCard().getNumber());
        Assert.assertEquals(newCustomer.getCreditCard().getType(), testCustomer.getCreditCard().getType());

        // Check authentication
        customerService.authenticateCustomer(newCustomer.getEmail(), password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCustomerNoCustomer() throws EmailAlreadyUsedException {

        String password = "newPassword";

        try {
            customerService.registerCustomer(null, password);
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCustomerNoPassword() throws EmailAlreadyUsedException {

        try {
            customerService.registerCustomer(getNewCustomer(), null);
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = EmailAlreadyUsedException.class)
    public void testRegisterCustomerMailAlreadyUsed() throws EmailAlreadyUsedException {

        Customer newCustomer = getNewCustomer();
        newCustomer.setEmail("email-already-used@email.ch");
        String password = "newPassword";

        customerService.registerCustomer(newCustomer, password);

        try {
            customerService.registerCustomer(newCustomer, password);
            Assert.fail("Hier erwarten wir eine EmailAlreadyUsedException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof EmailAlreadyUsedException) {
                throw ((EmailAlreadyUsedException) e.getCause());
            }
        }
    }

    @Test
    public void testSearchCustomers() {

        List<CustomerInfo> result1 = customerService.searchCustomers(CustomerData.HANS_WURST.firstName());
        List<CustomerInfo> result2 = customerService.searchCustomers(CustomerData.HANS_WURST.lastName());
        List<CustomerInfo> result3 = customerService.searchCustomers("bond");

        Assert.assertTrue(result1.size() > 0);
        Assert.assertTrue(result2.size() > 0);
        Assert.assertTrue(result3.size() > 1);

        Assert.assertTrue(checkContainsCustomer(CustomerData.HANS_WURST.firstName(), result1));
        Assert.assertTrue(checkContainsCustomer(CustomerData.HANS_WURST.firstName(), result2));
        Assert.assertTrue(checkContainsCustomer(CustomerData.SUPER_USER.firstName(), result3));
        Assert.assertTrue(checkContainsCustomer(CustomerData.BONDS_MOTHER.firstName(), result3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchCustomersNoName() {

        try {
            customerService.searchCustomers(null);
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchCustomersEmptyName() {

        try {
            customerService.searchCustomers("");
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
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

    @Test
    public void testUpdateCustomerSuccess() throws EmailAlreadyUsedException, CustomerNotFoundException, InvalidCredentialsException {

        // Create new customer
        Customer newCustomer = getNewCustomer();
        newCustomer.setEmail("customer-update-test@email.ch");
        String password = "newPassword";

        String newFirstName = "changed-firstName";
        String newLastName = "changed-lastName";
        String newEMail = "changed-email@email.ch";
        String newCity = "changed-city";
        String newCountry = "changed-country";
        String newPostalCode = "changed-postal-code";
        String newStreet = "changed-street";
        Integer newExpirationMonth = 1;
        Integer newExpirationYear = 2030;
        String newCardNumber = "changed-number";
        CreditCard.Type newCardType = CreditCard.Type.Visa;

        customerService.registerCustomer(newCustomer, password);

        // Load new customer...
        Customer customerToChange = customerService.findCustomer(newCustomer.getEmail());

        // Change some values
        customerToChange.setFirstName(newFirstName);
        customerToChange.setLastName(newLastName);
        customerToChange.setEmail(newEMail);
        customerToChange.getAddress().setCity(newCity);
        customerToChange.getAddress().setCountry(newCountry);
        customerToChange.getAddress().setPostalCode(newPostalCode);
        customerToChange.getAddress().setStreet(newStreet);
        customerToChange.getCreditCard().setExpirationMonth(newExpirationMonth);
        customerToChange.getCreditCard().setExpirationYear(newExpirationYear);
        customerToChange.getCreditCard().setNumber(newCardNumber);
        customerToChange.getCreditCard().setType(newCardType);

        // Update
        customerService.updateCustomer(customerToChange);

        // Load again to chack the changed values
        Customer testCustomer = customerService.findCustomer(customerToChange.getEmail());

        Assert.assertNotNull(testCustomer);
        Assert.assertEquals(newFirstName, testCustomer.getFirstName());
        Assert.assertEquals(newLastName, testCustomer.getLastName());
        Assert.assertEquals(newEMail, testCustomer.getEmail());
        Assert.assertEquals(newCity, testCustomer.getAddress().getCity());
        Assert.assertEquals(newCountry, testCustomer.getAddress().getCountry());
        Assert.assertEquals(newPostalCode, testCustomer.getAddress().getPostalCode());
        Assert.assertEquals(newStreet, testCustomer.getAddress().getStreet());
        Assert.assertEquals(newExpirationMonth, testCustomer.getCreditCard().getExpirationMonth());
        Assert.assertEquals(newExpirationYear, testCustomer.getCreditCard().getExpirationYear());
        Assert.assertEquals(newCardNumber, testCustomer.getCreditCard().getNumber());
        Assert.assertEquals(newCardType, testCustomer.getCreditCard().getType());

        // Check authentication with new email address
        customerService.authenticateCustomer(newEMail, password);
    }

    @Test(expected = EmailAlreadyUsedException.class)
    public void testUpdateCustomerEmailAlreadyUsed() throws EmailAlreadyUsedException, CustomerNotFoundException {

        // Create new customer
        Customer newCustomer = getNewCustomer();
        newCustomer.setEmail("email-already-used@email.ch");
        String password = "newPassword";

        customerService.registerCustomer(newCustomer, password);

        // Load new customer...
        Customer customerToChange = customerService.findCustomer(newCustomer.getEmail());

        // Change email address to an already existing entry
        customerToChange.setEmail(CustomerData.HANS_WURST.email());

        try {
            // Try to update
            customerService.updateCustomer(customerToChange);
            Assert.fail("Hier erwarten wir eine EmailAlreadyUsedException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof EmailAlreadyUsedException) {
                throw ((EmailAlreadyUsedException) e.getCause());
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCustomerNoCustomer() throws EmailAlreadyUsedException, CustomerNotFoundException {

        try {
            customerService.updateCustomer(null);
            Assert.fail("Hier erwarten wir eine IllegalArgumentException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) e.getCause());
            }
        }
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testUpdateCustomerCustomerNoId() throws EmailAlreadyUsedException, CustomerNotFoundException {

        Customer newCustomer = getNewCustomer();
        newCustomer.setEmail("unknown-customer-to-update@email.ch");

        try {
            customerService.updateCustomer(newCustomer);
            Assert.fail("Hier erwarten wir eine CustomerNotFoundException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof CustomerNotFoundException) {
                throw ((CustomerNotFoundException) e.getCause());
            }
        }
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testUpdateCustomerCustomerWrongId() throws EmailAlreadyUsedException, CustomerNotFoundException {

        Customer newCustomer = getNewCustomer();
        newCustomer.setEmail("customer-with-wrong-id@email.ch");
        newCustomer.setId(Long.MAX_VALUE);

        try {
            customerService.updateCustomer(newCustomer);
            Assert.fail("Hier erwarten wir eine CustomerNotFoundException!");
        } catch (EJBException e) {
            if (e.getCause() instanceof CustomerNotFoundException) {
                throw ((CustomerNotFoundException) e.getCause());
            }
        }
    }
}
