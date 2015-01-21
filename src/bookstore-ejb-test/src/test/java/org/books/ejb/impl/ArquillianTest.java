/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import javax.ejb.EJB;
import org.books.ejb.CustomerServiceLocal;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Assert;
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

        PomEquippedResolveStage resolver = Maven
                .resolver()
                .loadPomFromFile("pom.xml");

        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class);

        ear.addAsLibraries(resolver
                .resolve("org.books.persistence:bookstore-jpa")
                .withTransitivity()
                .asFile());

        ear.addAsLibraries(resolver
                .resolve("org.bookstore:bookstore-ejb")
                .withTransitivity()
                .asFile());

        ear.addAsLibraries(resolver
                .resolve("log4j:log4j:1.2.17")
                .withoutTransitivity()
                .asFile());

        ear.addAsLibraries(
                ShrinkWrap
                .create(JavaArchive.class)
                .addPackage(ArquillianTest.class.getPackage()));

        System.out.println(ear.toString(true));

        return ear;
    }

    @EJB
    CustomerServiceLocal customerService;

    @Test
    public void testCustomerServiceNotNull() {

        Assert.assertNotNull(customerService);
    }

}
