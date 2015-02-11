/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import javax.ws.rs.BadRequestException;

/**
 * @author Sigi
 */
public abstract class AbstractResource {

    protected void validateNotNull(Object id) throws BadRequestException {
        if (id == null) {
            throw new BadRequestException();
        }
    }

    protected void validateNotBlank(String str) {
        if (str == null || "".equals(str.trim())) {
            throw new BadRequestException();
        }
    }

}
