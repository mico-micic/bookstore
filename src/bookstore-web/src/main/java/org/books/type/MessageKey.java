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
    ORDER_DECLINED("org.books.presentation.CartBean.ORDER_DECLINED"),
    SAVE_SUCCESSFUL("org.books.presentation.CustomerBean.SAVE_SUCCESSFUL"),
    SAVE_UNSUCCESSFUL("org.books.presentation.CustomerBean.SAVE_UNSUCCESSFUL");

    private final String key;

    private MessageKey(String key) {
        this.key = key;
    }

    public String value() {
        return key;
    }

}
