/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.Book_;
import org.books.persistence.dto.BookInfo;

/**
 * @author micic
 */
public class BookDao {

    private static final String ALL_BOOKS_ORDERED_BY_PUBYEAR_SQL = "org.books.persistence.dao.BooksOrderedByPublicationYear";
    private static final String ISBN_SEARCH_SQL = "org.books.persistence.dao.BookByIsbn";
    private static final String ISBN_SEARCH_SQL_PARAM_ISBN = "isbn";

    private final EntityManager mgr;

    public BookDao(EntityManager mgr) {
        this.mgr = mgr;
    }

    public Book getByIsbn(String isbn) {
        return this.mgr.createNamedQuery(ISBN_SEARCH_SQL, Book.class)
                .setParameter(ISBN_SEARCH_SQL_PARAM_ISBN, isbn)
                .getSingleResult();
    }

    public List<Book> searchByKeywords(String[] keywords) {

        CriteriaBuilder cb = this.mgr.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);

        // Concatinate title, authors and publisher to one string
        Expression<String> exp1 = cb.concat(book.get(Book_.title), "\t");
        exp1 = cb.concat(exp1, book.get(Book_.authors));
        exp1 = cb.concat(exp1, "\t");
        exp1 = cb.concat(exp1, book.get(Book_.publisher));

        // Add LIKE statement for each keyword
        List<Predicate> predicates = new ArrayList<>();
        for (String key : keywords) {
            predicates.add(cb.like(cb.upper(exp1), "%" + key.toUpperCase() + "%"));
        }

        cq.select(book).where(cb.or(predicates.toArray(new Predicate[]{})));

        return this.mgr.createQuery(cq).getResultList();
    }

    /**
     * @return The first given number of books of all books ordered by their
     * publication-year.
     * @param maxResults Defines the max-size of the return-list (must be bigger
     * than 0).
     */
    public List<BookInfo> findLatestBooks(int maxResults) {
        if (maxResults <= 0) {
            throw new IllegalArgumentException("The parameter of the method BookDao.findLatestBooks(..) must be bigger than 0 !!");
        }
        return this.mgr
                .createNamedQuery(ALL_BOOKS_ORDERED_BY_PUBYEAR_SQL, BookInfo.class)
                .setFirstResult(0)
                .setMaxResults(maxResults)
                .getResultList();
    }
    
    /**
     * Check all given books if they exists in our local database 
     * and add them if required.
     * 
     * @param books List of books to check or add
     */
    public void checkAndAddBooks(List<Book> books) {
        
        Book bookInDb;
        
        for (Book book : books) {
            try {
                // Try to load the book by ISBN
                bookInDb = this.getByIsbn(book.getIsbn());
            } catch (NoResultException e) {
                bookInDb = null;
            }
            
            // Nothing found? --> Insert
            if (bookInDb == null) {
                this.mgr.merge(book);
            }
        }
    }
}
