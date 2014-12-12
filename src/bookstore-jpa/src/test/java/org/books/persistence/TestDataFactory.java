/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import org.books.persistence.entity.Book;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Login;

/**
 * @author Sigi
 */
public class TestDataFactory {

    private final EntityManager em;

    TestDataFactory(EntityManager em) {
        this.em = em;
    }

    void prepareTestData() {
        createNewBook("Java Insel", "978-3897214484", new BigDecimal(105.50), "Fowler", "O'Reilly Verlag GmbH");
        createNewBook("Programmieren mit Java", "978-3836217408", new BigDecimal(105.50), "Philip Ackermann", "Wiley-VCH");
        createNewBook("Java 8 - Die Neuerungen", "978-3527710706", new BigDecimal(105.50), "Jutta Schmidt, Barry Burd", "Galileo Computing");
        createNewBook("Java für Dummies", "978-3836217880", new BigDecimal(105.50), "Hans-Peter Habelitz", "Carl Hanser Verlag GmbH");

        Login login = createLogin("superuser@email.com", "pass@word");
        CreditCard masterCard = createMasterCard("5105105105105100");
        Address address = createAddress();
        Customer customer = createCustomer(login, masterCard, address);

        // TODO complete me
        em.getTransaction().begin();
        em.getTransaction().commit();
    }

    private Book createNewBook(String title, String isbn, BigDecimal price, String authors, String publisher) {
        Book book = new Book();
        book.setAuthors(authors);
        book.setBinding(Book.Binding.Hardcover);
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setNumberOfPages(101);
        book.setPrice(price);
        book.setPublicationYear(2013);
        book.setPublisher(publisher);
        em.persist(book);
        return book;
    }

    private CreditCard createMasterCard(String number) {
        CreditCard creditCard = new CreditCard();
        creditCard.setExpirationMonth(12);
        creditCard.setExpirationYear(2017);
        creditCard.setNumber(number);
        creditCard.setType(CreditCard.Type.MasterCard);
        return creditCard;
    }

    private Login createLogin(String email, String password) {
        Login login = new Login();
        login.setUserName(email);
        login.setPassword(password);
        em.persist(login);
        return login;
    }

    private Address createAddress() {
        Address address = new Address();
        address.setCity("Bern");
        address.setCountry("CH");
        address.setPostalCode("3000");
        address.setStreet("Strasse 8");
        return address;
    }

    private Customer createCustomer(Login login, CreditCard masterCard, Address address) {
        Customer customer = new Customer();

        // Wie abgemacht entspricht der UserName immer der EMail-Adresse:
        customer.setEmail(login.getUserName());

        customer.setFirstName("James");
        customer.setLastName("Bond");
        customer.setAddress(address);
        customer.setCreditCard(masterCard);
        em.persist(customer);
        return customer;
    }

}
