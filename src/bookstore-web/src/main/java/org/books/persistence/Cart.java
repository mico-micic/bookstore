/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sigi
 */
public class Cart {

    private final List<BookOrder> books = new ArrayList();

    public void addBook(Book book) {
        books.add(new BookOrder(book));
    }

    public int getBookCount() {
        return books.size();
    }

    private static class BookOrder {

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
            return count.decrementAndGet();
        }
    }

}
