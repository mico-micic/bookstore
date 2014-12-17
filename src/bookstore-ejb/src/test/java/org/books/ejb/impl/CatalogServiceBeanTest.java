package org.books.ejb.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import org.books.ejb.CatalogService;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.entity.Book;
import org.books.persistence.testdata.AbstractTestBase;
import org.books.persistence.testdata.IsbnNumber;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CatalogServiceBeanTest extends AbstractTestBase {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-ejb/CatalogService";

    private static CatalogService catalogService;

    @BeforeClass
    public static void setup() throws Exception {
        catalogService = (CatalogService) new InitialContext().lookup(CATALOG_SERVICE_NAME);
    }

    @Test
    public void testFindBookByIsbn() throws BookNotFoundException {
        Book book = catalogService.findBook(IsbnNumber.ISBN_978_3836217880.number());

        Assert.assertNotNull(book);
        Assert.assertEquals(IsbnNumber.ISBN_978_3836217880.number(), book.getIsbn());
    }

    @Test
    public void testFindBookByInvalidIsbn() {
        try {
            catalogService.findBook("123");
            Assert.fail("Hier erwarten wir eine EJBException!");
        } catch (BookNotFoundException | EJBException ex) {
            Assert.assertTrue(ex.getCause() instanceof BookNotFoundException);
        }
    }

}
