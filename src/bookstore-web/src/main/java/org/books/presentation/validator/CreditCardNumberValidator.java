/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.books.application.MessageFactory;
import org.books.persistence.entity.CreditCard;
import org.books.type.MessageKey;

/**
 * @author Sigi
 */
@FacesValidator("org.books.presentation.validator.CreditCardValidator")
public class CreditCardNumberValidator implements Validator, StateHolder {

    private static final String VISACARD_PATTERN = "4[0-3]\\d{14}";
    private static final String MASTERCARD_PATTERN = "5[0-5]\\d{14}";

    private String cardTypeFieldId;
    private boolean isTransient; // false := default

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String number = String.valueOf(value);

        checkLuhnDigit(number);
        checkCartTypeFormat(component, number);
    }

    private void checkCartTypeFormat(UIComponent component, String number) {
        UIInput cardTypeComponent = (UIInput) component.findComponent(cardTypeFieldId);
        String fieldValue = String.valueOf(cardTypeComponent.getValue());
        CreditCard.Type cardType = convertToEnum(fieldValue);
        if (CreditCard.Type.MasterCard == cardType) {
            assertThatNumberMatches(number, MASTERCARD_PATTERN);
        } else {
            assertThatNumberMatches(number, VISACARD_PATTERN);
        }
    }

    private CreditCard.Type convertToEnum(String fieldValue) throws ValidatorException {
        try {
            return CreditCard.Type.valueOf(String.valueOf(fieldValue));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ValidatorException(getMessage());
        }
    }

    private void checkLuhnDigit(String number) throws ValidatorException {
        if (!number.matches("\\d+")) {
            throw new ValidatorException(getMessage());
        }
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int d = Character.digit(number.charAt(i), 10);
            if (i % 2 == number.length() % 2) {
                d += d < 5 ? d : (d - 9);
            }
            sum += d;
        }
        if (sum % 10 != 0) {
            throw new ValidatorException(getMessage());
        }
    }

    private FacesMessage getMessage() {
        return MessageFactory.createMessage(FacesMessage.SEVERITY_ERROR, MessageKey.CREDIT_CARD_NOT_VALID.value());
    }

    public void setCardTypeFieldId(String cardTypeFieldId) {
        this.cardTypeFieldId = cardTypeFieldId;
    }

    @Override
    public Object saveState(FacesContext context) {
        return cardTypeFieldId;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        cardTypeFieldId = String.valueOf(state);
    }

    @Override
    public boolean isTransient() {
        return isTransient;
    }

    @Override
    public void setTransient(boolean newTransientValue) {
        isTransient = newTransientValue;
    }

    private void assertThatNumberMatches(String number, String pattern) {
        if (!number.matches(pattern)) {
            throw new ValidatorException(getMessage());
        }
    }

}
