/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.math.BigDecimal;

/**
 *
 * @author micic
 */
public class BookInfo {
    
    private final Long id;
    private final String title;
    private final String isbn;
    private final BigDecimal price;

    public BookInfo(Long id, String title, String isbn, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
