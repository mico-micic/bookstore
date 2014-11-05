/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.MessageFactory;
import org.books.persistence.Book;
import org.books.persistence.Cart;
import org.books.type.EnumActionResult;
import org.books.type.MessageKey;

/**
 *
 * @author micic
 */
@Named("cartBean")
@SessionScoped
public class CartBean implements Serializable {

    @Inject
    private CustomerBean customerBean;
    
    private Cart cart;

    public Cart getCart() {
        return cart;
    }

    public void addToCart(Book book) {
        if (cart == null) {
            cart = new Cart();
        }
        cart.addBook(book);
    }

    public int getBooksInCart() {
        return cart == null ? 0 : cart.getBookCount();
    }
    
    public EnumActionResult checkout() {
        customerBean.findCustomer();
        return EnumActionResult.ORDER;
    }
    
    public EnumActionResult confirmOrder() {
        MessageFactory.info(MessageKey.ORDER_CONFIRMED);
        cart.reset();
        return EnumActionResult.HOME;
    }
}
