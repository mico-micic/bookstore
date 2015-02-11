/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.books.ejb.CatalogServiceLocal;
import org.books.ejb.exception.BookNotFoundException;
import org.books.persistence.entity.Book;

/**
 * @author Sigi
 */
@Path("books")
public class BooksResource {

    @EJB
    private CatalogServiceLocal catalogService;

    @GET
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Book findById(@PathParam("id") Long id) {
        validateNotNull(id);
        try {
            return catalogService.findBook(id);
        } catch (BookNotFoundException ex) {
            throw new NotFoundException();
        }
    }

    @GET
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public Book findByIsbn(@QueryParam("isbn") String isbn) {
        validateNotBlank(isbn);
        try {
            return catalogService.findBook(isbn);
        } catch (BookNotFoundException ex) {
            throw new NotFoundException();
        }
    }

    @GET
    @Path("search")
    @Consumes({"application/xml", "application/json"})
    @Produces({"application/xml", "application/json"})
    public List<Book> search(@QueryParam("keywords") String keywords) {
        return catalogService.searchBooks(keywords);
    }

    private void validateNotNull(Object id) throws BadRequestException {
        if (id == null) {
            throw new BadRequestException();
        }
    }

    private void validateNotBlank(String str) {
        if (str == null || "".equals(str.trim())) {
            throw new BadRequestException();
        }
    }

}
