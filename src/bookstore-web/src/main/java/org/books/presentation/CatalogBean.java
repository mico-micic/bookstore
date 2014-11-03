/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.books.application.BookNotFoundException;
import org.books.application.Bookstore;
import org.books.persistence.Book;

/**
 *
 * @author micic
 */
@Named("catalogBean")
@SessionScoped
public class CatalogBean implements Serializable {
    
    @Inject
    private Bookstore bookstore;
    
    private String isbn;
    
    private String searchKey;

    private Book book;
    
    private List<Book> books;
    
    private String message;
    
    private EnumActionResult wayBack;
    
    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    
    public Bookstore getBookstore() {
        return bookstore;
    }

    public void setBookstore(Bookstore bookstore) {
        this.bookstore = bookstore;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Book getBook() {
        return book;
    }

    public String getMessage() {
        return message;
    }

    public EnumActionResult findBook() {
        
        EnumActionResult ret;
        book = null;
        message = null;
        
        try {
            book = bookstore.findBook(isbn);
            ret = EnumActionResult.BOOK;
        } catch (BookNotFoundException ex) {
            ret = EnumActionResult.BOOK;
        }
        
        return ret;
    }
    
    public EnumActionResult searchBooks() {
        
        EnumActionResult ret;
        message = null;
        books = null;
        
        books = this.bookstore.searchBooks(this.searchKey);
        
        if (books != null && books.size() > 0) {
            ret = EnumActionResult.RESULTS;
        } else {
            message = "Sorry, nothing found!";
            ret = EnumActionResult.RESULTS;
        }

        return ret;
    }
    
    public EnumActionResult setBookSelection(Book book, String wayBack) {
        this.wayBack = EnumActionResult.valueOf(wayBack);
        this.book = book;
        return EnumActionResult.BOOK;
    }
    
    public EnumActionResult navigateBack() {
        if(wayBack == null) {
            return EnumActionResult.HOME;
        }
        return wayBack;
    }
}
