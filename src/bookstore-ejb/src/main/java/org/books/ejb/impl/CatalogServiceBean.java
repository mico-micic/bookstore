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
@Stateless
public class CatalogServiceBean implements CatalogService {

    private BookDao bookDao;

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        bookDao = new BookDao(em);
    }

    @Override
    public Book findBook(Long bookId) {
        return em.find(Book.class, bookId);
    }

    @Override
    public Book findBook(String isbn) {
        return bookDao.getByIsbn(isbn);
    }

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        if (isBlank(keywords)) {
            throw new IllegalArgumentException("Die Methode CatalogServiceBean.searchBooks(..) verlangt einen Parameter-String, der nicht leer ist.");
        }
        return bookDao.searchByKeywords(keywords.split("\\s+"));
    }

    private static boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }

}
