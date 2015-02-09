/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import java.util.List;
import javax.naming.InitialContext;
import org.books.ejb.AmazonCatalogServiceRemote;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.entity.Book;
import org.books.persistence.testdata.AbstractTestBase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author micic
 */
public class AmazonCatalogServiceBeanTest extends AbstractTestBase {

    private static final String AMAZON_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/AmazonCatalogService!org.books.ejb.AmazonCatalogServiceRemote";

    private static AmazonCatalogServiceRemote amazonService;

    @BeforeClass
    public static void setup() throws Exception {
        amazonService = (AmazonCatalogServiceRemote) new InitialContext().lookup(AMAZON_SERVICE_NAME);
    }

    @Test
    public void testFindBook() throws BookNotFoundException {

        String isbn = "0071809252";

        Book book = amazonService.findBook(isbn);

        Assert.assertNotNull(book);
        Assert.assertEquals(isbn, book.getIsbn());
        Assert.assertEquals("Java: A Beginner's Guide", book.getTitle());
        Assert.assertEquals("Herbert Schildt", book.getAuthors());
        Assert.assertEquals("Mcgraw-Hill Osborne Media", book.getPublisher());
        Assert.assertEquals(2014, book.getPublicationYear().intValue());
        Assert.assertEquals(Book.Binding.Paperback, book.getBinding());
        Assert.assertEquals(699, book.getNumberOfPages().intValue());
        Assert.assertEquals(40, book.getPrice().doubleValue(), 1E-6);
    }

    @Test
    public void testSearchBooks() throws BookNotFoundException {

        List<Book> result = amazonService.searchBooks("Java test");
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() >= 50);
    }
}
