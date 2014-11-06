/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.Bookstore;
import org.books.application.MessageFactory;
import org.books.application.exception.PaymentFailedException;
import org.books.persistence.Book;
import org.books.persistence.Cart;
import org.books.persistence.LineItem;
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

    public int getBooksInCart() {
        return cart == null ? 0 : cart.getBookCount();
    }

    public EnumActionResult confirmOrder() {
        try {
            bookstore.placeOrder(customerBean.getCustomer(), toLineItems(cart.getBookOrders()));
            MessageFactory.info(MessageKey.ORDER_CONFIRMED);
            cart.reset();
            return EnumActionResult.HOME;
        } catch (PaymentFailedException ex) {
            MessageFactory.error(MessageKey.ORDER_DECLINED);
            return null;
        }
    }

    private List<LineItem> toLineItems(List<Cart.BookOrder> bookOrders) {
        List<LineItem> lineItems = new ArrayList<>();
        for (Cart.BookOrder bookOrder : bookOrders) {
            lineItems.add(new LineItem(bookOrder.getBook(), bookOrder.getCount()));
        }
        return lineItems;
    }
}
