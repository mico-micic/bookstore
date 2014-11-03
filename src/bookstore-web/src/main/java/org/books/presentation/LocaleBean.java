/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Sigi
 */
@Named
public class LocaleBean {


    public void updateLocale(String loc) {
        Locale locale = new Locale(loc);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }

}
