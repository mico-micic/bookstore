/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence;

import org.books.persistence.entity.Book;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.EntityManager;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.LineItem;
import org.books.persistence.entity.Login;
import org.books.persistence.entity.Order;

/**
 * @author Sigi
 */
public class TestDataFactory {

    private final EntityManager em;

    TestDataFactory(EntityManager em) {
        this.em = em;
    }

    void prepareTestData() {

        Book book1 = createNewBook("Java Insel", "978-3897214484", new BigDecimal(105.50), "Fowler", "O'Reilly Verlag GmbH");
        Book book2 = createNewBook("Programmieren mit Java", "978-3836217408", new BigDecimal(105.50), "Philip Ackermann", "Wiley-VCH");
        Book book3 = createNewBook("Java 8 - Die Neuerungen", "978-3527710706", new BigDecimal(105.50), "Jutta Schmidt, Barry Burd", "Galileo Computing");
        Book book4 = createNewBook("Java für Dummies", "978-3836217880", new BigDecimal(105.50), "Hans-Peter Habelitz", "Carl Hanser Verlag GmbH");

        Login login1 = createLogin("superuser@email.com", "pass@word");
        CreditCard masterCard1 = createMasterCard("5105105105105100");
        Address address1 = createAddress();

        Login login2 = createLogin("hans@wurst.ch", "secret_word");
        CreditCard masterCard2 = createMasterCard("5105105105105100");
        Address address2 = createAddress();

        Login login3 = createLogin("bonds_mother@007.ch", "secret_word");
        CreditCard masterCard3 = createMasterCard("5105105105105100");
        Address address3 = createAddress();

        Customer customer1 = createCustomer("James", "Bond", login1, masterCard1, address1);
        Customer customer2 = createCustomer("Hans", "Wurst", login2, masterCard2, address2);
        Customer customer3 = createCustomer("Bonds_Mother", "Some_Name", login3, masterCard3, address3);

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        cal1.set(2010, 1, 25);
        cal2.set(2011, 6, 15);
        cal3.set(2014, 9, 23);

        createOrder(customer1, BigDecimal.valueOf(158.60), new Date(cal1.getTimeInMillis()), "1111-001", createAddress(), masterCard1, lineItemFor(book1), lineItemFor(book2));
        createOrder(customer1, BigDecimal.valueOf(33.55), new Date(cal1.getTimeInMillis()), "1111-002", createAddress(), masterCard1, lineItemFor(book3), lineItemFor(book4));
        createOrder(customer1, BigDecimal.valueOf(16.99), new Date(cal1.getTimeInMillis()), "1111-003", createAddress(), masterCard1);
        createOrder(customer1, BigDecimal.valueOf(77.20), new Date(cal2.getTimeInMillis()), "1111-004", createAddress(), masterCard1);

        createOrder(customer2, BigDecimal.valueOf(28.15), new Date(cal3.getTimeInMillis()), "2222-001", createAddress(), masterCard1);

        createOrder(customer3, BigDecimal.valueOf(77.10), new Date(cal3.getTimeInMillis()), "3333-001", createAddress(), masterCard1);

        em.getTransaction().begin();
        em.getTransaction().commit();
    }

    public void deleteTestData() {

        em.getTransaction().begin();

        em.createQuery("DELETE FROM LineItem").executeUpdate();
        em.createQuery("DELETE FROM Book").executeUpdate();
        em.createQuery("DELETE FROM Order").executeUpdate();
        em.createQuery("DELETE FROM Customer").executeUpdate();
        em.createQuery("DELETE FROM Login").executeUpdate();

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

    private Customer createCustomer(String firstName, String lastName, Login login, CreditCard masterCard, Address address) {
        Customer customer = new Customer();

        // Wie abgemacht entspricht der UserName immer der EMail-Adresse:
        customer.setEmail(login.getUserName());

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setCreditCard(masterCard);
        em.persist(customer);
        return customer;
    }

    private Order createOrder(Customer customer, BigDecimal amount, Date date, String number, Address address, CreditCard card, LineItem... lineItems) {

        Order order = new Order();

        order.setCustomer(customer);
        order.setAmount(amount);
        order.setDate(date);
        order.setNumber(number);
        order.setStatus(Order.Status.accepted);
        order.setAddress(address);
        order.setCreditCard(card);

        order.setItems(Arrays.asList(lineItems));

        em.persist(order);
        return order;
    }

    private LineItem lineItemFor(Book book) {
        return new LineItem(book, 7);
    }

}
