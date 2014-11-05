/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sigi
 */
public class Cart implements Serializable {

    private final List<BookOrder> bookOrders = new ArrayList<>();

    public void addBook(Book book) {
        BookOrder bookOrder = new BookOrder(book);
        if (bookOrders.contains(bookOrder)) {
            bookOrders.get(bookOrders.indexOf(bookOrder)).incrementCount();
        } else {
            bookOrders.add(bookOrder);
        }
    }

    public List<BookOrder> getBookOrders() {
        return bookOrders;
    }

    public int getBookCount() {
        return bookOrders.size();
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (BookOrder bookOrder : bookOrders) {
            BigDecimal bookOrderPrice = bookOrder.getBook().getPrice();
            BigDecimal orderCount = new BigDecimal(bookOrder.getCount());
            totalPrice = totalPrice.add(bookOrderPrice.multiply(orderCount));
        }
        return totalPrice;
    }

    public void remove(BookOrder bookOrder) {
        bookOrders.remove(bookOrder);
    }

    public void reset() {
        bookOrders.clear();
    }

    public class BookOrder implements Serializable {

        private final Book book;
        private final AtomicInteger count;

        public BookOrder(Book book) {
            this.book = book;
            this.count = new AtomicInteger(1);
        }

        public Book getBook() {
            return book;
        }

        public int getCount() {
            return count.get();
        }

        public int incrementCount() {
            return count.incrementAndGet();
        }

        public int decrementCount() {
            if (count.get() > 0) {
                return count.decrementAndGet();
            }
            return count.get();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 73 * hash + Objects.hashCode(this.book);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final BookOrder other = (BookOrder) obj;
            if (!Objects.equals(this.book, other.book)) {
                return false;
            }
            return true;
        }

    }

}
