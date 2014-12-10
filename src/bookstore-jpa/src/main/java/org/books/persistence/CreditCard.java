package org.books.persistence;

import java.util.Calendar;
import javax.persistence.Embeddable;

@Embeddable
public class CreditCard extends ValueObject {

    public enum Type {

        MasterCard, Visa
    }

    private static final int MAX_CARD_EXPIRY_YEARS = 5;

    private Type type;
    private String number;
    private Integer expirationMonth;
    private Integer expirationYear;

    public CreditCard() {
    }

    public CreditCard(Type type, String number, Integer expirationMonth, Integer expirationYear) {
        this.type = type;
        this.number = number;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }

    public int getMinExpirationYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public int getMaxExpirationYear() {
        return Calendar.getInstance().get(Calendar.YEAR) + MAX_CARD_EXPIRY_YEARS;
    }
}
