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
    LOGOUT_SUCCESS("org.books.presentation.LoginBean.LOGOUT_SUCCESS"),
    BOOK_ADDED_TO_CART("org.books.presentation.CartBean.BOOK_ADDED_TO_CART"),
    ORDER_DECLINED("org.books.presentation.CartBean.ORDER_DECLINED"),
    SAVE_SUCCESSFUL("org.books.presentation.CustomerBean.SAVE_SUCCESSFUL"),
    SAVE_UNSUCCESSFUL("org.books.presentation.CustomerBean.SAVE_UNSUCCESSFUL"),
    REGISTRATION_SUCCESSFUL("org.books.presentation.CustomerBean.REGISTRATION_SUCCESSFUL"),
    EMAIL_ALREADY_EXISTS("org.books.presentation.CustomerBean.EMAIL_ALREADY_EXISTS"),
    CREDIT_CARD_NOT_VALID("org.books.presentation.validator.CreditCardValidator.CREDIT_CARD_NUMER"),
    PASSWORDS_ARE_NOT_EQUAL("org.books.presentation.validator.PasswordValidator.PASSWORDS_ARE_NOT_EQUAL"),
    YEAR_CONVERSION("org.books.presentation.converter.YearConverter.YEAR");

    private final String key;

    private MessageKey(String key) {
        this.key = key;
    }

    public String value() {
        return key;
    }

}
