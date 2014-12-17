package org.books.persistence.testdata;

public enum IsbnNumber {

    ISBN_978_3897214484("978-3897214484"),
    ISBN_978_3836217408("978-3836217408"),
    ISBN_978_3527710706("978-3527710706"),
    ISBN_978_3836217880("978-3836217880");

    private final String number;

    private IsbnNumber(String number) {
        this.number = number;
    }

    public String number() {
        return this.number;
    }

}
