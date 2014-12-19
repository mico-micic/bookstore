package org.books.ejb.impl;

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

    @Test(expected = IllegalArgumentException.class)
    public void testFindBookByEmptyIsbn() throws Throwable {
        try {
            catalogService.findBook("");
            Assert.fail("Hier erwarten wir eine EJBException!");
        } catch (EJBException ex) {
            throw ex.getCause(); // Gewrappt müsste die IllegalArgumentException zu finden sein
        }
    }

    @Test(expected = BookNotFoundException.class)
    public void testFindBookByInvalidIsbn() throws Throwable {
        try {
            catalogService.findBook("123");
            Assert.fail("Hier erwarten wir eine EJBException!");
        } catch (EJBException ex) {
            throw ex.getCause(); // Gewrappt müsste die BookNotFoundException zu finden sein
        }
    }

    @Test
    public void testFindBookById() throws BookNotFoundException {
        Book book = catalogService.findBook(10_001L);

        Assert.assertNotNull(book);
        Assert.assertEquals(Long.valueOf(10_001L), book.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindBookByEmptyId() throws Throwable {
        try {
            catalogService.findBook((Long) null);
            Assert.fail("Hier erwarten wir eine EJBException!");
        } catch (EJBException ex) {
            throw ex.getCause(); // Gewrappt müsste die IllegalArgumentException zu finden sein
        }
    }

    @Test(expected = BookNotFoundException.class)
    public void testFindBookByInvalidId() throws Throwable {
        try {
            catalogService.findBook(0L);
            Assert.fail("Hier erwarten wir eine EJBException!");
        } catch (EJBException ex) {
            throw ex.getCause(); // Gewrappt müsste die BookNotFoundException zu finden sein
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchBooksWithNullKeywords() throws Throwable {
        try {
            catalogService.searchBooks(null);
            Assert.fail("Hier erwarten wir eine EJBException!");
        } catch (EJBException ex) {
            throw ex.getCause(); // Gewrappt müsste die IllegalArgumentException zu finden sein
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchBooksWithEmptyKeywords() throws Throwable {
        try {
            catalogService.searchBooks("");
            Assert.fail("Hier erwarten wir eine EJBException!");
        } catch (EJBException ex) {
            throw ex.getCause(); // Gewrappt müsste die IllegalArgumentException zu finden sein
        }
    }

}
