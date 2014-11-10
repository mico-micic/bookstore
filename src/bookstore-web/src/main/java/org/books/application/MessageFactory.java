/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import org.books.type.MessageKey;

/**
 *
 * @author Sigi
 */
public class MessageFactory {

    public static void error(MessageKey key, Object... params) {
        addMessageToContext(FacesMessage.SEVERITY_ERROR, key.value(), params);
    }

    public static void warning(MessageKey key, Object... params) {
        addMessageToContext(FacesMessage.SEVERITY_WARN, key.value(), params);
    }

    public static void info(MessageKey key, Object... params) {
        addMessageToContext(FacesMessage.SEVERITY_INFO, key.value(), params);
    }

    private static void addMessageToContext(Severity severity, String key, Object... params) {
        FacesMessage message = createMessage(severity, key, params);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static FacesMessage createMessage(FacesMessage.Severity severity, String key, Object... params) {

        String summary = "???" + key + "???";
        String detail = null;
        try {

            ResourceBundle bundle = getMessageBundle();
            summary = MessageFormat.format(bundle.getString(key), params);
            detail = MessageFormat.format(bundle.getString(key + "_detail"), params);

        } catch (MissingResourceException resEx) {
            // Leave with the Defaults.
        }
        return new FacesMessage(severity, summary, detail);
    }

    private static ResourceBundle getMessageBundle() {
        String name = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return ResourceBundle.getBundle(name, locale);
    }

}
