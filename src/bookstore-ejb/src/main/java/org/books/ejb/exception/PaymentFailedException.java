/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.exception;

/**
 * @author Sigi
 */
public class PaymentFailedException extends Exception {

    public PaymentFailedException() {
        super();
    }

    public PaymentFailedException(String message) {
        super(message);
    }

}