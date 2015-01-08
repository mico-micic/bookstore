package org.books.persistence.testdata;

public enum OrderData {

    ORDER_WITH_LINE_ITEMS("1111-001"),
    ORDER_WITHOUT_LINE_ITEMS("2222-001");

    private final String number;

    private OrderData(String number) {
        this.number = number;
    }

    public String number() {
        return number;
    }

}
