/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
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

    public EnumSearchResult findBook() {
        
        EnumSearchResult ret;
        book = null;
        message = null;
        
        try {
            book = bookstore.findBook(isbn);
            ret = EnumSearchResult.SUCCEED;
            
        } catch (BookNotFoundException ex) {
            message = "Book not found!!";
            ret = EnumSearchResult.FAIL;
        }
        
        return ret;
    }
    
    public EnumSearchResult searchBooks() {
        
        EnumSearchResult ret;
        message = null;
        books = null;
        
        books = this.bookstore.searchBooks(this.searchKey);
        
        if (books != null && books.size() > 0) {
            ret = EnumSearchResult.SUCCEED;
        } else {
            message = "Nothing found!";
            ret = EnumSearchResult.FAIL;
        }

        return ret;
    }
    
    public EnumSearchResult setBookSelection(Book book) {
        this.book = book;
        return EnumSearchResult.SUCCEED;
    }
}
