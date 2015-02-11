/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author Sigi
 */
public class BookNotFoundWebAppException extends WebApplicationException {

    public BookNotFoundWebAppException() {
        super(Response.Status.NOT_FOUND);
    }

}
