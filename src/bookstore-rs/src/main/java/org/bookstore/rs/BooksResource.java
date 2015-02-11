/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import java.util.List;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import org.books.ejb.CatalogServiceRemote;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.entity.Book;
import org.bookstore.rs.exception.BookNotFoundWebAppException;

/**
 * @author Sigi
 */
@Path("books")
public class BooksResource {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore-ear/bookstore-ejb/CatalogService!org.books.ejb.CatalogServiceRemote";

    private static CatalogServiceRemote catalogService;

    public BooksResource() {
        try {
            catalogService = (CatalogServiceRemote) new InitialContext().lookup(CATALOG_SERVICE_NAME);
        } catch (Throwable ex) {
            throw new IllegalStateException("Fehler beim initialisieren der BookResource!", ex);
        }
    }

    @GET
    @Path("{id}")
    public Book find(@PathParam("id") Long id) {
        System.out.println("Triggered BookResource.find(ID)");
        try {
            return catalogService.findBook(id);
        } catch (Throwable t) {
            throw extractedException(t);
        }
    }

    @GET
    @Path("search")
    public List<Book> search(@QueryParam("keywords") String keywords) {
        System.out.println("Triggered BookResource.search(keywords)");
        return catalogService.searchBooks(keywords);
    }

    private WebApplicationException extractedException(Throwable t) {
        if (t instanceof EJBException) {
            return extractEJBExceptions((EJBException) t);
        } else {
            throw new WebApplicationException(t);
        }
    }

    private WebApplicationException extractEJBExceptions(EJBException ex) {
        if (ex.getCause() instanceof BookNotFoundException) {
            return new BookNotFoundWebAppException();
        } else {
            return new WebApplicationException(ex);
        }
    }

}
