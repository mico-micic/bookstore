/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

/**
 * @author micic
 */
public class BeanHelper {
    
    private static final String ERROR_EMPTY_STRING_ARG = "Die Service-Methode verlangt einen Parameter-String, der nicht leer ist.";
    
    private BeanHelper() {}
    
    public static void validateInput(Object objectValue) throws IllegalArgumentException {
        if (isBlank(objectValue)) {
            throw new IllegalArgumentException(ERROR_EMPTY_STRING_ARG);
        }
    }

    public static boolean isBlank(Object object) {
        return object == null || String.valueOf(object).length() == 0;
    }
}
