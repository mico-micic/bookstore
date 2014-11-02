/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.books.persistence.Book;
import org.books.persistence.Cart;

/**
 *
 * @author micic
 */
@Named("cartBean")
@SessionScoped
public class CartBean implements Serializable {

    private Cart cart;

    public Cart getCart() {
        return cart;
    }

    public EnumActionResult addToCart(Book book) {

        if (cart == null) {
            cart = new Cart();
        }

        cart.addBook(book);
        return EnumActionResult.SUCCEED;
    }

    public int getBooksInCart() {
        return cart == null ? 0 : cart.getBookCount();
    }
}
