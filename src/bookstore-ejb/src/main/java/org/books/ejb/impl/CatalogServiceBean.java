/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.books.ejb.CatalogService;
import org.books.persistence.dao.BookDao;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;

/**
 * @author Sigi
 */
@Stateless(name = "CatalogService")
public class CatalogServiceBean implements CatalogService {

    private static final String ERROR_EMPTY_STRING_ARG = "Die Service-Methode verlangt einen Parameter-String, der nicht leer ist.";
    private static final String PATTERN_FOR_WHITESPACE = "\\s+";

    private BookDao bookDao;

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        bookDao = new BookDao(em);
    }

    @Override
    public Book findBook(Long bookId) {
        validateInput(bookId);
        return em.find(Book.class, bookId);
    }

    @Override
    public Book findBook(String isbn) {
        validateInput(isbn);
        return bookDao.getByIsbn(isbn);
    }

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        validateInput(keywords);
        return bookDao.searchByKeywords(keywords.split(PATTERN_FOR_WHITESPACE));
    }

    private void validateInput(Object objectValue) throws IllegalArgumentException {
        if (isBlank(objectValue)) {
            throw new IllegalArgumentException(ERROR_EMPTY_STRING_ARG);
        }
    }

    private static boolean isBlank(Object object) {
        return object == null || String.valueOf(object).length() == 0;
    }

}
