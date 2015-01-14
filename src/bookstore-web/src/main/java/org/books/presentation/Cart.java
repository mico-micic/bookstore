/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.books.persistence.dto.OrderItem;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.LineItem;

/**
 * @author Sigi
 */
public class Cart implements Serializable {

    private final List<LineItem> lineItems = new ArrayList<>();

    public void addBook(Book book) {
                
        LineItem existing = getExistingLineItem(book);

        if (existing != null) {
            incrementQuantity(existing);
        } else {
            lineItems.add(new LineItem(book, 1));
        }
    }
    
    public void addLineItem(LineItem item) {
        this.lineItems.add(item);
    }
    
    private LineItem getExistingLineItem(Book book) {
        return this.lineItems.stream().filter(item -> item.getBook().equals(book)).findFirst().orElse(null);
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }
    
    public List<OrderItem> getOrderItems() {
        List<OrderItem> ret = new ArrayList<>();
        this.lineItems.forEach(lineItem -> ret.add(new OrderItem(lineItem.getBook().getIsbn(), lineItem.getQuantity())));
        return ret;
    }

    public int getTotalBooksInCart() {
        return lineItems.stream().mapToInt(LineItem::getQuantity).sum();
    }

    public double getTotalPrice() {
        double sum = lineItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getBook().getPrice().doubleValue())
                .sum();
        return Math.round(100.0 * sum) / 100.0;
    }

    public void remove(LineItem lineItem) {
        lineItems.remove(lineItem);
    }

    public void reset() {
        lineItems.clear();
    }
    
    public void incrementQuantity(LineItem item) {
        item.setQuantity(item.getQuantity() + 1);
    }
    
    public void decrementQuantity(LineItem item) {
        int currQuantity = item.getQuantity();
        if (currQuantity > 1) {
            item.setQuantity(currQuantity - 1);
        }
    }
}