package org.books.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.books.persistence.dao.BookDao;
import org.books.persistence.entity.Book;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author micic
 */
public class BookDaoTests {

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
    public void testGetByIsbn() {

        BookDao bookDao = new BookDao(this.em);
        Book theBook = bookDao.getByIsbn("013-123-342-1");

        assertNotNull(theBook);
    }

}
