/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.books.ejb.AmazonCatalogServiceLocal;
import org.books.ejb.CatalogServiceLocal;
import org.books.ejb.CatalogServiceRemote;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.dao.BookDao;
import org.books.persistence.entity.Book;

/**
 * @author Sigi
 */
@Stateless(name = "CatalogService")
public class CatalogServiceBean implements CatalogServiceRemote, CatalogServiceLocal {

    private static final String PATTERN_FOR_WHITESPACE = "\\s+";

    private BookDao bookDao;

    @EJB
    private AmazonCatalogServiceLocal amazonService;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        bookDao = new BookDao(em);
    }

    @Override
    public Book findBook(Long bookId) throws BookNotFoundException {
        BeanHelper.validateInput(bookId);
        Book book = em.find(Book.class, bookId);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return book;
    }

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {
        BeanHelper.validateInput(isbn);
        try {
            return bookDao.getByIsbn(isbn);
        } catch (NoResultException ex) {
            
            // Fallback: Try to lookup the ISBN with amazon webservice
            return amazonService.findBook(isbn);
        }
    }

    @Override
    public List<Book> searchBooks(String keywords) {
        BeanHelper.validateInput(keywords);
        // return bookDao.searchByKeywords(keywords.split(PATTERN_FOR_WHITESPACE));
        return amazonService.searchBooks(keywords);
    }
}
