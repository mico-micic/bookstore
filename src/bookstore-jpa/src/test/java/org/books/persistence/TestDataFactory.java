/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import org.books.persistence.entity.Book;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Login;

/**
 * @author Sigi
 */
public class TestDataFactory {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    TestDataFactory(EntityManagerFactory emf, EntityManager em) {
        this.emf = emf;
        this.em = em;
    }

    void prepareTestData() {
        createNewBook("Java Insel", "013-123-342-1", new BigDecimal(105.50));
        createLogin("superuser@email.com", "pass@word");
        createMasterCard("5105105105105100");
        // TODO complete me
        em.getTransaction().begin();
        em.getTransaction().commit();
    }

    private Book createNewBook(String title, String isbn, BigDecimal price) {
        Book book = new Book();
        book.setAuthors("Fowler");
        book.setBinding(Book.Binding.Hardcover);
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setNumberOfPages(101);
        book.setPrice(price);
        book.setPublicationYear(2013);
        book.setPublisher("O Reilly");
        em.persist(book);
        return book;
    }

    private CreditCard createMasterCard(String number) {
        CreditCard creditCard = new CreditCard();
        creditCard.setExpirationMonth(12);
        creditCard.setExpirationYear(2017);
        creditCard.setNumber(number);
        creditCard.setType(CreditCard.Type.MasterCard);
        em.persist(creditCard);
        return creditCard;
    }

    private Login createLogin(String email, String password) {
        Login login = new Login();
        login.setUserName(email);
        login.setPassword(password);
        em.persist(login);
        return login;
    }

}
