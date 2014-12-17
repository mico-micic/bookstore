package org.books.ejb;

import java.util.List;
import javax.ejb.Remote;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;

/**
 * @author micic
 */
@Remote
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
    List<BookInfo> searchBooks(String keywords) throws IllegalArgumentException;

}
