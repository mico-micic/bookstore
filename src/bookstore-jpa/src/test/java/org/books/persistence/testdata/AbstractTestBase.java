/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.testdata;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author micic
 */
public abstract class AbstractTestBase {

    private static EntityManager em;
    private static TestDataFactory tdf;
    
    @BeforeClass
    public static void setUpClass() {
        em = Persistence.createEntityManagerFactory("bookstore").createEntityManager();
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
}
