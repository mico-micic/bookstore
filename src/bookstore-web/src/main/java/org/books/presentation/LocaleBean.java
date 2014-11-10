/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Sigi
 */
@Named
@SessionScoped
public class LocaleBean implements Serializable {

    private Locale locale;

    public void updateLocale(String loc) {
        locale = new Locale(loc);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

}
