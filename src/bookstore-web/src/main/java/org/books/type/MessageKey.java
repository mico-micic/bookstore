/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.type;

/**
 *
 * @author Sigi
 */
public enum MessageKey {

    BOOK_NOT_FOUND_BY_ISDN("org.books.presentation.CatalogBean.BOOK_NOT_FOUND"),
    ORDER_CONFIRMED("org.books.presentation.CartBean.ORDER_CONFIRMED"),
    INVALID_USER("org.books.presentation.LoginBean.INVALID_USER"),
    LOGIN_SUCCESS("org.books.presentation.LoginBean.LOGIN_SUCCESS"),
    BOOK_ADDED_TO_CART("org.books.presentation.CartBean.BOOK_ADDED_TO_CART");

    private final String key;

    private MessageKey(String key) {
        this.key = key;
    }

    public String value() {
        return key;
    }

}
