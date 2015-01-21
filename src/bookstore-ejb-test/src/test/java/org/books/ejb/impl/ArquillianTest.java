/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.AppenderAttachable;
import org.books.ejb.CatalogService;
import org.books.ejb.CatalogServiceLocal;
import org.books.ejb.CatalogServiceRemote;
import org.books.ejb.CustomerServiceLocal;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.converter.AesEncryptorConverter;
import org.books.persistence.dao.BookDao;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.Customer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author micic
 */
@RunWith(Arquillian.class)
public class ArquillianTest {

    @Deployment
    public static Archive<?> createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(CatalogService.class)
                .addClass(CatalogServiceLocal.class)
                .addPackage(CatalogServiceRemote.class.getPackage())
                .addPackage(CatalogServiceBean.class.getPackage())
                .addPackage(BookNotFoundException.class.getPackage())
                .addPackage(BookDao.class.getPackage())
                .addPackage(Customer.class.getPackage())
                .addPackage(Logger.class.getPackage())
                .addPackage(AppenderAttachable.class.getPackage())
                .addPackage(OrderInfo.class.getPackage())
                .addPackage(AesEncryptorConverter.class.getPackage())
                .addAsManifestResource("META-INF/persistence-arquillian.xml", "persistence.xml")
                .addAsManifestResource("META-INF/queries.xml", "queries.xml")
                .addAsManifestResource("META-INF/converters.xml", "converters.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        System.out.println(jar.toString(true));

        return jar;
    }

    @EJB
    CustomerServiceLocal customerService;
    
    @Test
    public void AlbumRepository_should_return_album_by_id(){
     
        Assert.assertNotNull(customerService);
    }

}
