/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.books.persistence.entity.CreditCard;

/**
 *
 * @author micic
 */
@Named("creditCardsBean")
@SessionScoped
public class CreditCardsBean implements Serializable {
    
    private static final List<SelectItem> supportedTypes = new ArrayList<>();

    static {
        supportedTypes.add(new SelectItem(CreditCard.Type.MasterCard.toString(), CreditCard.Type.MasterCard.toString()));
        supportedTypes.add(new SelectItem(CreditCard.Type.Visa.toString(), CreditCard.Type.Visa.toString()));
    }
    
    public List<SelectItem> getSupportedTypes() {
        return supportedTypes;
    }
}
