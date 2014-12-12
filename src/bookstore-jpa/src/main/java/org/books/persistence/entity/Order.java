package org.books.persistence.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BOOKORDER")
public class Order extends IdentifiableObject {

    public enum Status {

        accepted, processing, delivered, canceled
    }

    @Column(
            unique = true,
            nullable = false)
    private String number;

    @Column(
            name = "ORDERDATE",
            nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(optional = false)
    @JoinColumn(name = "customerId")
    private Customer customer;

    @Embedded
    private Address address;

    @Embedded
    private CreditCard creditCard;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "lineItemId")
    private List<LineItem> items;

    public Order() {
    }

    public Order(String number, Date date, BigDecimal amount, Status status,
            Customer customer, Address address, CreditCard creditCard, List<LineItem> items) {
        this.number = number;
        this.date = date;
        this.amount = amount;
        this.status = status;
        this.customer = customer;
        this.address = address;
        this.creditCard = creditCard;
        this.items = items;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        if (customer == null) {
            customer = new Customer();
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public CreditCard getCreditCard() {
        if (creditCard == null) {
            creditCard = new CreditCard();
        }
        return creditCard;
    }

    public void setCreditCard(CreditCard card) {
        this.creditCard = card;
    }

    public List<LineItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }
}
