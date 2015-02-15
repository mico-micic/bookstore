/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.rest;

import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.books.persistence.entity.Book;
import org.books.persistence.testdata.IsbnNumber;

/**
 * @author Sigi
 */
public class BooksRestServiceTest {

    private final WebTarget booksTarget = ClientBuilder.newClient().target("http://localhost:8080/bookstore/rest/books");

    @Test
    public void testFindBookById() {
        Response response = booksTarget
                .path(String.valueOf(1))
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Book book = response.readEntity(Book.class);
        assertThat(book).isNotNull();
        assertThat(book.getId()).isEqualTo(1);
    }

    @Test
    public void testFindBookWithInvalidId() {
        Response response = booksTarget
                .path(String.valueOf(0))
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testFindBookByISBN() {
        Response response = booksTarget
                .queryParam("isbn", IsbnNumber.ISBN_978_3527710706.number())
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Book book = response.readEntity(Book.class);
        assertThat(book).isNotNull();
        assertThat(book.getIsbn()).isEqualTo(IsbnNumber.ISBN_978_3527710706.number());
    }

    @Test
    public void testFindBookWithInvalidISBN() {
        Response response = booksTarget
                .queryParam("isbn", 124)
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testFindBookWithEmptyISBN() {
        Response response = booksTarget
                // No ISBN: .queryParam("isbn", 124)
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testSearchBooksWithKeywords_LondonByTube() {
        String keywords = "London by Tube";
        int expectedResults = 75;
        searchByKeywordsAndCheckResult(keywords, expectedResults);
    }

    @Test
    public void testSearchBooksWithKeywords_JavaEE() {
        String keywords = "Java EE";
        int expectedResults = 85;
        searchByKeywordsAndCheckResult(keywords, expectedResults);
    }

    @Test
    public void testSearchBooksWithKeywords_ParisAtNight() {
        String keywords = "Paris at Night";
        int expectedResults = 66;
        searchByKeywordsAndCheckResult(keywords, expectedResults);
    }

    @Test
    public void testSearchBooksWithKeywords_HomerSimpson() {
        String keywords = "Homer Simpson";
        int expectedResults = 59;
        searchByKeywordsAndCheckResult(keywords, expectedResults);
    }

    @Test
    public void testSearchBooksWithKeywords_JavaFlanaganReilly() {
        String keywords = "Java Flanagan Reilly";
        int expectedResults = 21;
        searchByKeywordsAndCheckResult(keywords, expectedResults);
    }

    private void searchByKeywordsAndCheckResult(String keywords, int expectedResults) {
        Response response = booksTarget
                .path("search")
                .queryParam("keywords", keywords)
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        List<Book> books = response.readEntity(new GenericType<List<Book>>() {
        });
        assertThat(books).isNotNull();

        int numberOfResults = books.size();
        System.out.println("Number of Results for: <" + keywords + "> = " + numberOfResults);
        int expectedMin = (int) Math.floor(expectedResults * 0.9);
        assertThat(numberOfResults).isGreaterThan(expectedMin);
        int expectedMax = (int) Math.ceil(expectedResults * 1.1);
        assertThat(numberOfResults).isLessThan(expectedMax);
    }

}
