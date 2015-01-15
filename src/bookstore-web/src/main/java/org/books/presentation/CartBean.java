/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.MessageFactory;
import org.books.ejb.OrderServiceLocal;
import org.books.ejb.exception.BookNotFoundException;
import org.books.ejb.exception.CustomerNotFoundException;
import org.books.ejb.exception.PaymentFailedException;
import org.books.persistence.entity.Book;
import org.books.type.EnumActionResult;
import org.books.type.MessageKey;

/**
 * Bean containing the shopping cart functionality. 
 * 
 * The book orders are held in the class {@link Cart} and
 * handled by the {@link OrderServiceLocal} EJB.
 * 
 * @see Cart
 * @see OrderServiceLocal
 *
 * @author Sigi
 * @author Mico
 */
@Named("cartBean")
@SessionScoped
public class CartBean implements Serializable {

    @EJB
    private OrderServiceLocal orderService;

    @Inject
    private CustomerBean customerBean;

    // Cart holding the line items
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
            orderService.placeOrder(customerBean.getCustomer().getId(), cart.getOrderItems());
            MessageFactory.info(MessageKey.ORDER_CONFIRMED);
            cart.reset();
            return EnumActionResult.HOME;
        } catch (PaymentFailedException ex) {
            MessageFactory.error(MessageKey.ORDER_DECLINED);
            return null;
        } catch (CustomerNotFoundException ex) {
            MessageFactory.error(MessageKey.CUSTOMER_NOT_FOUND);
            return null;
        } catch (BookNotFoundException ex) {
            MessageFactory.error(MessageKey.BOOK_NOT_FOUND_BY_ISDN);
            return null;
        }
    }
}
