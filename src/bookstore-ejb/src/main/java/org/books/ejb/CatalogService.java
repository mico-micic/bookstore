/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb;

import java.util.List;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.entity.Book;

/**
 *
 * @author micic
 */
public interface CatalogService {
    
    /**
     * Finds a book with a particular identifier.
     *
     * @param bookId The identifier to find the book.
     * @return The book according to the given identifier.
     * @throws BookNotFoundException when no book can be found with the given
     * bookId.
     */
    Book findBook(Long bookId) throws BookNotFoundException;

    /**
     * Finds a book with a particular ISBN number.
     *
     * @param isbn The unique ISBN to find the book.
     * @return The book according to the giben ISBN-Number.
     * @throws BookNotFoundException when no book can be found with the given
     * ISBN-Number.
     */
    Book findBook(String isbn) throws BookNotFoundException;

    /**
     * Searches for books by keywords. A book is included in the results list if
     * all keywords are contained in its title, authors or publisher field. If
     * the given parameter is null or empty an IllegalArgumentException will be
     * thrown.
     *
     * @param keywords The keywords (white-space-separated).
     * @return All Books that match all keywords.
     */
    List<Book> searchBooks(String keywords) throws IllegalArgumentException;
    
    /**
     * Make sure that the given list of books is persisted in our local database.
     * 
     * @param books List of books to check or add to local database
     */
    void ensureBooks(List<Book> books);
}
