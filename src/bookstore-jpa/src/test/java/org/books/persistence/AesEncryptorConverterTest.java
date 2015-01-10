package org.books.persistence;

import org.books.persistence.testdata.AbstractTestBase;
import java.io.UnsupportedEncodingException;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Login;
import org.books.persistence.testdata.CustomerData;
import org.books.persistence.testdata.LoginData;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Sigi
 */
public class AesEncryptorConverterTest extends AbstractTestBase {

    @Test
    public void testLoginWithPwHashing() throws UnsupportedEncodingException {
        Login login = getEm()
                .createQuery("SELECT l FROM Login l ORDER BY l.id", Login.class)
                .getResultList()
                .get(0);

        Assert.assertNotNull(login);
        Assert.assertTrue(login.isPasswordValid(LoginData.SUPER_USER.password()));
    }

    @Test
    @Ignore
    public void testEncryptionOfCreditCardNumber() throws UnsupportedEncodingException {
        // The Entity is customer, not CreditCard. So Decrypting only works, when loading the whole Customer.
        Customer customer = getEm()
                .createQuery("SELECT c FROM Customer c ORDER BY c.id", Customer.class)
                .getResultList()
                .get(0);

        Assert.assertNotNull(customer);
        Assert.assertNotNull(customer.getCreditCard());
        Assert.assertEquals("5105105105105100", customer.getCreditCard().getNumber());

        // Attention: With JPQL we get the encrypted value:
        // => thus we need to operate only over the Entity when decrypting DB-Fields!
        String decryptedValue = getEm()
                .createQuery("SELECT c.creditCard.number FROM Customer c WHERE c.email = '" + CustomerData.SUPER_USER.email() + "'", String.class)
                .getResultList()
                .get(0);
        Assert.assertNotSame("5105105105105100", decryptedValue);
    }

}
