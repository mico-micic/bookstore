/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.testdata;

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

        Book book1 = createNewBook("Java Insel", IsbnNumber.ISBN_978_3897214484.number(), new BigDecimal(105.50), "Fowler", "O'Reilly Verlag GmbH", 1919);
        Book book2 = createNewBook("Programmieren mit Java", IsbnNumber.ISBN_978_3836217408.number(), new BigDecimal(105.50), "Philip Ackermann", "Wiley-VCH", 2001);
        Book book3 = createNewBook("Java 8 - Die Neuerungen", IsbnNumber.ISBN_978_3527710706.number(), new BigDecimal(105.50), "Jutta Schmidt, Barry Burd", "Galileo Computing", 2013);
        Book book4 = createNewBook("Java für Dummies", IsbnNumber.ISBN_978_3836217880.number(), new BigDecimal(105.50), "Hans-Peter Habelitz", "Carl Hanser Verlag GmbH", 2013);

        Login login1 = createLogin(LoginData.SUPER_USER.email(), LoginData.SUPER_USER.password());
        CreditCard masterCard1 = createMasterCard("5105105105105100");
        Address address1 = createAddress();

        Login login2 = createLogin(LoginData.HANS_WURST.email(), LoginData.HANS_WURST.password());
        CreditCard masterCard2 = createMasterCard("5105105105105100");
        Address address2 = createAddress();

        Login login3 = createLogin(LoginData.BONDS_MOTHER.email(), LoginData.BONDS_MOTHER.password());
        CreditCard masterCard3 = createMasterCard("5105105105105100");
        Address address3 = createAddress();
        
        Login login4 = createLogin(LoginData.PW_CHANGE_TEST.email(), LoginData.PW_CHANGE_TEST.password());
        CreditCard masterCard4 = createMasterCard("5105105105105100");
        Address address4 = createAddress();

        Customer customer1 = createCustomer(CustomerData.SUPER_USER.firstName(), CustomerData.SUPER_USER.lastName(), login1, masterCard1, address1);
        Customer customer2 = createCustomer(CustomerData.HANS_WURST.firstName(), CustomerData.HANS_WURST.lastName(), login2, masterCard2, address2);
        Customer customer3 = createCustomer(CustomerData.BONDS_MOTHER.firstName(), CustomerData.BONDS_MOTHER.lastName(), login3, masterCard3, address3);
        Customer customer4 = createCustomer(CustomerData.PW_CHANGE_TEST.firstName(), CustomerData.PW_CHANGE_TEST.lastName(), login4, masterCard4, address4);

        createOrder(customer1, OrderData.O_1111_001.amount(), OrderData.O_1111_001.date(), OrderData.O_1111_001.number(), createAddress(), masterCard1, 
                lineItemFor(book1), lineItemFor(book2));
        createOrder(customer1, OrderData.O_1111_002.amount(), OrderData.O_1111_002.date(), OrderData.O_1111_002.number(), createAddress(), masterCard1, 
                lineItemFor(book3), lineItemFor(book4));
        createOrder(customer1, OrderData.O_1111_003.amount(), OrderData.O_1111_003.date(), OrderData.O_1111_003.number(), createAddress(), masterCard1);
        createOrder(customer1, OrderData.O_1111_004.amount(), OrderData.O_1111_004.date(), OrderData.O_1111_004.number(), createAddress(), masterCard1);
        createOrder(customer2, OrderData.O_2222_001.amount(), OrderData.O_2222_001.date(), OrderData.O_2222_001.number(), createAddress(), masterCard1);
        createOrder(customer3, OrderData.O_3333_001.amount(), OrderData.O_3333_001.date(), OrderData.O_3333_001.number(), createAddress(), masterCard1);

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

    private Book createNewBook(String title, String isbn, BigDecimal price, String authors, String publisher, Integer pubYear) {
        Book book = new Book();
        book.setAuthors(authors);
        book.setBinding(Book.Binding.Hardcover);
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setNumberOfPages(101);
        book.setPrice(price);
        book.setPublicationYear(pubYear);
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
