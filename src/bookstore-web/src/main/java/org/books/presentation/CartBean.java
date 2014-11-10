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
import org.books.application.Bookstore;
import org.books.application.MessageFactory;
import org.books.application.exception.PaymentFailedException;
import org.books.persistence.Book;
import org.books.persistence.Cart;
import org.books.type.EnumActionResult;
import org.books.type.MessageKey;

/**
 * Bean containing the shopping cart functionality. 
 * 
 * The book orders are stored in the persistence class {@link Cart} and
 * handled by {@link Bookstore}.
 * 
 * @see Cart
 * @see Bookstore
 *
 * @author Sigi
 * @author Mico
 */
@Named("cartBean")
@SessionScoped
public class CartBean implements Serializable {

    @Inject
    private Bookstore bookstore;

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

        MessageFactory.info(MessageKey.BOOK_ADDED_TO_CART);
    }

    public EnumActionResult confirmOrder() {
        try {
            bookstore.placeOrder(customerBean.getCustomer(), cart.getLineItems());
            MessageFactory.info(MessageKey.ORDER_CONFIRMED);
            cart.reset();
            return EnumActionResult.HOME;
        } catch (PaymentFailedException ex) {
            MessageFactory.error(MessageKey.ORDER_DECLINED);
            return null;
        }
    }
}
