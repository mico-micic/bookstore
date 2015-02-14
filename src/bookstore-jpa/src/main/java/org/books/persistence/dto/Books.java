/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.books.persistence.entity.Book;

/**
 *
 * @author micic
 */
@XmlRootElement(name = "books")
public class Books {

    @XmlElement(name = "book")
    public List<Book> books = null;
    
    public Books set(List<Book> books) {
        this.books = books;
        return this;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + this.books;
    }
}
