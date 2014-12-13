package org.books.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.books.persistence.dao.LoginDao;
import org.books.persistence.entity.Login;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author micic
 */
public class LoginDaoTests {

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
    public void testGetByUserName() {

        Login result = new LoginDao(this.em).getByUserName("superuser@email.com");
       
        assertNotNull(result);
        assertEquals("superuser@email.com", result.getUserName());
    }
    
}
