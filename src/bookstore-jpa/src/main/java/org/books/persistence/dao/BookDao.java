/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dao;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.books.persistence.Book;
import org.books.persistence.Book_;
import org.books.persistence.dto.BookInfo;

/**
 *
 * @author micic
 */
public class BookDao {

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
    
    public List<BookInfo> searchByKeywords(List<String> keywords) {
        
        List<BookInfo> ret = new ArrayList<>();
        
        CriteriaBuilder cb = this.mgr.getCriteriaBuilder();
        CriteriaQuery<BookInfo> cq = cb.createQuery(BookInfo.class);
        Root<Book> book = cq.from(Book.class);
   
        Expression<String> exp1 = cb.concat(book.get(Book_.title), book.get(Book_.authors));
        exp1 = cb.concat(exp1, book.get(Book_.publisher));
        
        List<Predicate> predicates = new ArrayList<>();
        
        for (String key : keywords) {
            predicates.add(cb.or(cb.like(exp1, "%" + key + "%")));
        }
        
        cq.select(cb.construct(BookInfo.class, 
                book.get(Book_.id), 
                book.get(Book_.title), 
                book.get(Book_.isbn), 
                book.get(Book_.price)))
                .where(predicates.toArray(new Predicate[]{}));
        
        return this.mgr.createQuery(cq).getResultList();
    }
}
