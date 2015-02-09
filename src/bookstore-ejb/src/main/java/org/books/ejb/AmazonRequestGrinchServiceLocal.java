/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb;

import javax.ejb.Local;

/**
 *
 * @author micic
 */
@Local
public interface AmazonRequestGrinchServiceLocal {
    
    public void lockAndWaitForNextRequest();
    public void unlockAndUpdateLastRequest(long duration);
}
