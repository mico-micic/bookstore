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
import com.amazon.webservice.ItemSearch;
import com.amazon.webservice.ItemSearchRequest;
import com.amazon.webservice.ItemSearchResponse;
import com.amazon.webservice.OperationRequest;
import com.amazon.webservice.Price;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;
import org.apache.log4j.Logger;
import org.books.ejb.AmazonCatalogServiceLocal;
import org.books.ejb.AmazonCatalogServiceRemote;
import org.books.ejb.AmazonRequestGrinchServiceLocal;
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

    private static final String AMAZON_ID_TYPE_ISBN = "ISBN";
    private static final String AMAZON_RESPONSE_GROUP = "ItemAttributes";
    private static final String AMAZON_SEARCH_INDEX = "Books";
    private static final BigInteger AMAZON_MAX_PAGES = BigInteger.valueOf(10);

    @WebServiceRef(AWSECommerceService.class)
    private AWSECommerceServicePortType webServicePort;

    @EJB
    private AmazonRequestGrinchServiceLocal amazonRequestGrinch;

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {

        Book ret = null;
        ItemLookup lookupBody = new ItemLookup();
        ItemLookupRequest req = new ItemLookupRequest();

        req.setIdType(AMAZON_ID_TYPE_ISBN);
        req.setSearchIndex(AMAZON_SEARCH_INDEX);
        req.getItemId().add(isbn);
        req.getResponseGroup().add(AMAZON_RESPONSE_GROUP);

        lookupBody.getRequest().add(req);
        ItemLookupResponse resp;

        long start = System.currentTimeMillis();
        try {
            amazonRequestGrinch.lockAndWaitForNextRequest();
            resp = webServicePort.itemLookup(lookupBody);
        } finally {
            amazonRequestGrinch.unlockAndUpdateLastRequest(System.currentTimeMillis() - start);
        }

        if (resp != null
                && isResponseOK(resp.getOperationRequest())
                && resp.getItems().size() == 1
                && resp.getItems().get(0).getItem().size() > 0) {

            // Get first item with a supported binding (in some cases there may be
            // multiple items with the same ISBN but different bindings)
            for (Item item : resp.getItems().get(0).getItem()) {
                String binding = item.getItemAttributes().getBinding();
                if (binding != null && Binding.valueOf(binding) != null) {
                    ret = createBook(item.getItemAttributes());
                    break;
                }
            }
            
            // Fallback to first item if there is no item with hardcover or paperback binding
            // (In this case the item will be added with "unknown" binding...)
            if (ret == null) {
                ret = createBook(resp.getItems().get(0).getItem().get(0).getItemAttributes());
            }
        }

        // For the case that createBook() returns null or the response was invalid
        if (ret == null) {
            throw new BookNotFoundException();
        }
        
        return ret;
    }

    @Override
    public List<Book> searchBooks(String keywords) {

        List<Book> ret = new ArrayList<>();
        internalSearchBooks(keywords, BigInteger.ONE, ret);
        return ret;
    }

    private void internalSearchBooks(String keywords, BigInteger nextPage, List<Book> result) {

        ItemSearch searchBody = new ItemSearch();
        ItemSearchRequest req = new ItemSearchRequest();

        req.setSearchIndex(AMAZON_SEARCH_INDEX);
        req.getResponseGroup().add(AMAZON_RESPONSE_GROUP);
        req.setKeywords(keywords);
        req.setItemPage(nextPage);

        searchBody.getRequest().add(req);
        ItemSearchResponse resp;

        long start = System.currentTimeMillis();
        try {
            amazonRequestGrinch.lockAndWaitForNextRequest();
            LOGGER.info("Sending search request for page number: " + nextPage.intValue());
            resp = webServicePort.itemSearch(searchBody);
        } finally {
            amazonRequestGrinch.unlockAndUpdateLastRequest(System.currentTimeMillis() - start);
        }
        
        if (resp != null
                && isResponseOK(resp.getOperationRequest())
                && resp.getItems().size() == 1
                && resp.getItems().get(0).getTotalResults().compareTo(BigInteger.ZERO) > 0) {

            BigInteger totalPages = resp.getItems().get(0).getTotalPages();
            LOGGER.info("Total pages: " + totalPages);

            for (Item item : resp.getItems().get(0).getItem()) {
                Book book = createBook(item.getItemAttributes());
                if (book != null) {
                    result.add(book);
                }
            }

            nextPage = nextPage.add(BigInteger.ONE);
            if (totalPages.compareTo(nextPage) >= 0
                    && nextPage.compareTo(AMAZON_MAX_PAGES) <= 0) {
                internalSearchBooks(keywords, nextPage, result);
            }
        }
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

        switch (itemAttributes.getBinding()) {
            case "Paperback":
                ret.setBinding(Binding.Paperback);
                break;
            case "Hardcover":
                ret.setBinding(Binding.Hardcover);
                break;
            default:
                ret.setBinding(Binding.Unknown);
                break;
        }

        try {
            LocalDate dateTime = LocalDate.parse(itemAttributes.getPublicationDate());
            ret.setPublicationYear(dateTime.getYear());
        } catch (DateTimeParseException | NullPointerException e) {
            LOGGER.warn("Cannot parse publication year for ISBN " + ret.getIsbn()
                    + " Received value: " + itemAttributes.getPublicationDate(), e);
        }

        return ret.isComplete() ? ret : null;
    }
}
