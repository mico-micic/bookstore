package org.books.persistence;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class LineItem extends IdentifiableObject {

    @OneToOne
    @JoinColumn(name = "bookId")
    private Book book;

    private Integer quantity;

    public LineItem() {
    }

    public LineItem(Book book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "LineItem{" + "book=" + book + ", quantity=" + quantity + '}';
    }
}
