/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.books.application.MessageFactory;
import org.books.type.MessageKey;

/**
 *
 * @author micic
 */
@FacesValidator("org.books.presentation.validator.EmailValidator")
public class EmailValidator implements Validator {

    private final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private final static Pattern EMAIL_COMPILED_PATTERN = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value != null) {

            // The email matcher
            Matcher matcher = EMAIL_COMPILED_PATTERN.matcher((String) value);

            if (!matcher.matches()) {   // Email doesn't match
                FacesMessage msg = MessageFactory.createMessage(FacesMessage.SEVERITY_ERROR, MessageKey.EMAIL_INVALID.value());
                throw new ValidatorException(msg);
            }
        }
    }
}
