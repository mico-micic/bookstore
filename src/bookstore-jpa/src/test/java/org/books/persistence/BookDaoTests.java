package org.books.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.books.persistence.dao.BookDao;
import org.books.persistence.dto.BookInfo;
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
        Book theBook = bookDao.getByIsbn("978-3836217880");

        assertNotNull(theBook);
    }

    @Test
    public void testSearchByKeywords() {

        BookDao bookDao = new BookDao(this.em);
        List<BookInfo> result1 = bookDao.searchByKeywords(new String[]{"Programmieren", "Philip"});
        List<BookInfo> result2 = bookDao.searchByKeywords(new String[]{"Habelitz"});
        List<BookInfo> result3 = bookDao.searchByKeywords(new String[]{"Insel", "Reilly"});
        List<BookInfo> result4 = bookDao.searchByKeywords(new String[]{"Galileo", "Reilly"});
        List<BookInfo> result5 = bookDao.searchByKeywords(new String[]{"ckerma"});
        List<BookInfo> result6 = bookDao.searchByKeywords(new String[]{"rungen", "anse"});

        assertTrue(result1.size() > 0);
        assertTrue(result2.size() > 0);
        assertTrue(result3.size() > 0);
        assertTrue(result4.size() > 0);
        assertTrue(result5.size() > 0);
        assertTrue(result6.size() > 0);

        assertEquals("Programmieren mit Java", result1.get(0).getTitle());
        assertEquals("Java für Dummies", result2.get(0).getTitle());
        assertEquals("Java Insel", result3.get(0).getTitle());

        assertTrue(result4.size() > 1);
        assertTrue(checkContainsBook("Java 8 - Die Neuerungen", result4));
        assertTrue(checkContainsBook("Java Insel", result4));

        assertEquals("Programmieren mit Java", result5.get(0).getTitle());

        assertTrue(result6.size() > 1);
        assertTrue(checkContainsBook("Java für Dummies", result6));
        assertTrue(checkContainsBook("Java 8 - Die Neuerungen", result6));
    }

    private boolean checkContainsBook(String title, List<BookInfo> bookList) {

        boolean ret = false;

        for (BookInfo bookInfo : bookList) {
            if (title.equals(bookInfo.getTitle())) {
                ret = true;
                break;
            }
        }

        return ret;
    }
}
