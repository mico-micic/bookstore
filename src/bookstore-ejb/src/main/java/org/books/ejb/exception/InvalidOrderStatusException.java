/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.exception;

/**
 * @author Sigi
 */
public class InvalidOrderStatusException extends Exception {

    public InvalidOrderStatusException() {
        super();
    }

    public InvalidOrderStatusException(String message) {
        super(message);
    }

}
