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
import org.books.type.MessageKey;

/**
 *
 * @author micic
 */
@FacesValidator("org.books.presentation.validator.PasswordValidator")
public class PasswordValidator implements Validator, StateHolder {

    private String firstPassFieldId = null;

    private boolean isTransient = false;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (firstPassFieldId != null) {
            UIComponent comp = component.findComponent(firstPassFieldId);
            if (comp != null && comp instanceof UIInput) {
                String compValue = (String) ((UIInput) comp).getValue();
                if (!value.equals(compValue)) {
                    FacesMessage msg = MessageFactory.createMessage(FacesMessage.SEVERITY_ERROR, MessageKey.PASSWORDS_ARE_NOT_EQUAL.value());
                    throw new ValidatorException(msg);
                }
            }
        }
    }

    public void setFirstPassFieldId(String id) {
        firstPassFieldId = id;
    }

    @Override
    public Object saveState(FacesContext context) {
        return firstPassFieldId;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        this.firstPassFieldId = (String) state;
    }

    @Override
    public boolean isTransient() {
        return this.isTransient;
    }

    @Override
    public void setTransient(boolean newTransientValue) {
        this.isTransient = newTransientValue;
    }
}
