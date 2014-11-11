/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author micic
 */
public class ResourceBundleHelper {
    
    public static String getLocalizedText(String key) {
        
        String ret = "???" + key +"???";
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "texts");
        
        try {
            ret = bundle.getString(key);
        } catch (MissingResourceException e) {}
        
        return ret;
    }
}

