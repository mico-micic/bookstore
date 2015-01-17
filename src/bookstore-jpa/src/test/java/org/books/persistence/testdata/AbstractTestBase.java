/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.testdata;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.dbunit.IDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author micic
 */
public abstract class AbstractTestBase {

    private static final String PERSISTENCE_UNIT = "test-bookstore";

    private static EntityManager em;
    private static TestDataFactory tdf;

    @BeforeClass
    public static void setUpClass() {
        em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT).createEntityManager();
        initTestData();
        initDBUnitData();
    }

    private static void initTestData() {
        tdf = new TestDataFactory(em);
        tdf.deleteTestData();
        tdf.prepareTestData();
    }

    @AfterClass
    public static void tearDownClass() {
        //tdf.deleteTestData();
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    protected static EntityManager getEm() {
        return em;
    }

    private static void initDBUnitData() {
        new DBUnitDataInitializer().initData();
    }

    private static final class DBUnitDataInitializer {

        private static final String DB_UNIT_PROPERTIES = "/dbunit.properties";
        private static final String DB_UNIT_DATASET = "/dataset.xml";

        void initData() {
            try {
                System.getProperties().load(getClass().getResourceAsStream(DB_UNIT_PROPERTIES));
                IDatabaseTester databaseTester = new PropertiesBasedJdbcDatabaseTester();
                IDataSet dataset = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream(DB_UNIT_DATASET));
                databaseTester.setSetUpOperation(DatabaseOperation.REFRESH);
                databaseTester.setTearDownOperation(DatabaseOperation.NONE);
                databaseTester.setDataSet(dataset);
                databaseTester.onSetup();
            } catch (Exception ex) {
                Logger
                        .getLogger(AbstractTestBase.class.getName())
                        .log(Level.SEVERE, "Fehler beim Aufsetzen der DBUnit-Daten!", ex);
            }
        }

    }
}
