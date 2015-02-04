/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

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
        
        Book book = amazonService.findBook("076243631X");
        Assert.assertNotNull(book);
    }
}
