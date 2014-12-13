package org.books.persistence;

import java.io.UnsupportedEncodingException;
import org.books.persistence.entity.Login;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sigi
 */
public class JpaDeliverableTest extends AbstractTestBase {

    @Test
    public void testLoginWithPWEncryption() throws UnsupportedEncodingException {
        Login login = super.getEm()
                .createQuery("SELECT l FROM Login l ORDER BY l.id", Login.class)
                .getResultList()
                .get(0);

        Assert.assertNotNull(login);
        Assert.assertEquals("pass@word", login.getPassword());

        // Attention: With JPQL we get the encrypted value:
        // => thus we need to operate only over the Entity when decrypting DB-Fields!
        String decryptedPassword = super.getEm()
                .createQuery("SELECT l.password FROM Login l WHERE l.userName = 'superuser@email.com'", String.class)
                .getResultList()
                .get(0);
        Assert.assertNotSame("pass@word", decryptedPassword);
    }

}
