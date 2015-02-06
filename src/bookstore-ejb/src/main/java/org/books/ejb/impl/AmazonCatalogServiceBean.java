/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.impl;

import com.amazon.webservice.AWSECommerceService;
import com.amazon.webservice.AWSECommerceServicePortType;
import com.amazon.webservice.Item;
import com.amazon.webservice.ItemAttributes;
import com.amazon.webservice.ItemLookup;
import com.amazon.webservice.ItemLookupRequest;
import com.amazon.webservice.ItemLookupResponse;
import com.amazon.webservice.OperationRequest;
import com.amazon.webservice.Price;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import org.apache.log4j.Logger;
import org.books.ejb.AmazonCatalogServiceLocal;
import org.books.ejb.AmazonCatalogServiceRemote;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.Book.Binding;

/**
 *
 * @author micic
 */
@Stateless(name = "AmazonCatalogService")
public class AmazonCatalogServiceBean implements AmazonCatalogServiceLocal, AmazonCatalogServiceRemote {

    private static final Logger LOGGER = Logger.getLogger(AmazonCatalogServiceBean.class);

    @WebServiceRef(AWSECommerceService.class)
    private AWSECommerceServicePortType webServicePort;

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {

        Book ret = new Book();
        ItemLookup lookupBody = new ItemLookup();
        ItemLookupRequest req = new ItemLookupRequest();

        req.setIdType("ISBN");
        req.setSearchIndex("Books");
        req.getItemId().add(isbn);
        req.getResponseGroup().add("ItemAttributes");

        lookupBody.getRequest().add(req);

        ItemLookupResponse resp = webServicePort.itemLookup(lookupBody);
        if (resp != null
                && isResponseOK(resp.getOperationRequest())
                && resp.getItems().size() == 1
                && resp.getItems().get(0).getItem().size() == 1) {

            Item item = resp.getItems().get(0).getItem().get(0);
            ret = createBook(item.getItemAttributes());

        } else {
            throw new BookNotFoundException();
        }

        return ret;
    }

    @Override
    public List<Book> searchBooks(String keywords) {
        return null;
    }

    private boolean isResponseOK(OperationRequest req) {

        boolean ret = false;

        if (req.getErrors() == null) {
            ret = true;
        } else {

            StringBuilder errBuffer = new StringBuilder();
            errBuffer.append("Got error response form amazon! Error list:\n");
            req.getErrors().getError().forEach(
                    err -> errBuffer.append("Code: ").append(err.getCode()).append(" Msg: ").append(err.getMessage())
            );

            LOGGER.error(errBuffer.toString());
        }

        return ret;
    }

    private Book createBook(ItemAttributes itemAttributes) {

        Book ret = new Book();
        ret.setIsbn(itemAttributes.getISBN());
        ret.setTitle(itemAttributes.getTitle());
        ret.setAuthors(String.join(", ", itemAttributes.getAuthor()));
        ret.setPublisher(itemAttributes.getPublisher());

        BigInteger numberOfPages = itemAttributes.getNumberOfPages();
        if (numberOfPages != null) {
            ret.setNumberOfPages(itemAttributes.getNumberOfPages().intValue());
        }
        
        Price listPrice = itemAttributes.getListPrice();
        if (listPrice != null) {
            ret.setPrice(new BigDecimal(listPrice.getAmount()).divide(BigDecimal.valueOf(100)));
        }
        
        switch(itemAttributes.getBinding()) {
            case "Paperback": ret.setBinding(Binding.Paperback); break;
            case "Hardcover": ret.setBinding(Binding.Hardcover); break;
            default: ret.setBinding(Binding.Unknown); break;
        }
        
        try {
            LocalDate dateTime = LocalDate.parse(itemAttributes.getPublicationDate());
            ret.setPublicationYear(dateTime.getYear());
        } catch (DateTimeParseException e) {
            LOGGER.warn("Cannot parse publication year for ISBN " + ret.getIsbn(), e);
        }
        
        return ret;
    }
}
