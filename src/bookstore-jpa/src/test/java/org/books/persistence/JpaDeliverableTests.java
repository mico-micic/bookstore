package org.books.persistence;

import java.io.UnsupportedEncodingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.books.persistence.entity.Login;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sigi
 */
public class JpaDeliverableTests {

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
    public void testLoginWithPWEncryption() throws UnsupportedEncodingException {
        Login login = em
                .createQuery("SELECT l FROM Login l", Login.class)
                .getResultList()
                .get(0);

        Assert.assertNotNull(login);
        Assert.assertEquals("pass@word", login.getPassword());

        // Attention: With JPQL we get the encrypted value:
        // => thus we need to operate only over the Entity when decrypting DB-Fields!
        String decryptedPassword = em
                .createQuery("SELECT l.password FROM Login l WHERE l.userName = 'superuser@email.com'", String.class)
                .getResultList()
                .get(0);
        Assert.assertNotSame("pass@word", decryptedPassword);

    }

}
