/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.testdata.dbunit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.books.persistence.dao.BookDao;
import org.books.persistence.testdata.TestDataFactory;
import org.dbunit.DBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;

/**
 * @author Sigi
 */
public class BookStoreDataDBUnit extends DBTestCase {

    private static final String PERSISTENCE_UNIT = "test-bookstore";
    private static final String DB_UNIT_PROPERTIES = "/dbunit.properties";
    private static final String DB_UNIT_DATASET = "/dataset.xml";

    private final EntityManager entityManager;

    public BookStoreDataDBUnit() {
        super();

        entityManager = Persistence
                .createEntityManagerFactory(PERSISTENCE_UNIT)
                .createEntityManager();
        
        new TestDataFactory(entityManager).deleteTestData();
        new TestDataFactory(entityManager).prepareTestData();

        try {
            System.getProperties().load(getClass().getResourceAsStream(DB_UNIT_PROPERTIES));

        } catch (IOException ex) {
            Logger.getLogger(BookStoreDataDBUnit.class.getName()).log(Level.SEVERE, "Setup-Error!!", ex);
        }
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(DB_UNIT_DATASET));
    }

    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.NONE;
    }

    public void testSetup() throws Exception {
        Assert.assertTrue(new BookDao(entityManager).findLatestBooks(10).size() > 0);
    }

}
