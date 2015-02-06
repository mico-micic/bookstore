/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.PostConstruct;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import org.apache.log4j.Logger;
import org.books.ejb.AmazonRequestGrinchServiceLocal;

/**
 * @author micic
 */
@Singleton
@javax.ejb.Lock(LockType.READ)
public class AmazonRequestGrinchBean implements AmazonRequestGrinchServiceLocal {

    private static final int AMAZON_REQUEST_TIMEOUT = 1000; //1s

    private static final Logger LOGGER = Logger.getLogger(AmazonRequestGrinchBean.class);

    private Lock masterLock;
    
    private long lastRequestTs = 0;

    @PostConstruct
    public void init() {
        masterLock = new ReentrantLock();
    }

    @Override
    public void lockAndWaitForNextRequest() {
        
        masterLock.lock();

        long diff = System.currentTimeMillis() - lastRequestTs;
        if (diff < AMAZON_REQUEST_TIMEOUT) {
            try {
                long waitTime = (AMAZON_REQUEST_TIMEOUT - diff);
                LOGGER.info("Waiting for " + waitTime + "ms");
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                LOGGER.error(ex);
            }
        }
    }

    @Override
    public void unlockAndUpdateLastRequest() {
        lastRequestTs = System.currentTimeMillis();
        masterLock.unlock();
    }
}
