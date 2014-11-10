/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.books.application.Bookstore;
import org.books.application.MessageFactory;
import org.books.type.MessageKey;

/**
 * @author Sigi
 */
@FacesValidator("org.books.presentation.validator.CreditCardValidator")
public class CreditCardNumberValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        checkLuhnDigit(String.valueOf(value));
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

}
