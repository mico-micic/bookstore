/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.books.persistence.entity.Book;
import org.xml.sax.SAXException;

/**
 *
 * @author micic
 */
@Provider
@Consumes(MediaType.APPLICATION_XML)
public class XmlValidationWriter implements MessageBodyWriter<Book> {

    private static final Logger LOGGER = Logger.getLogger(XmlValidationWriter.class);

    private Schema schema;
    private JAXBContext jaxbContext;

    public XmlValidationWriter() {

        try {
            jaxbContext = JAXBContext.newInstance(Book.class);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            URL schemaURL = Thread.currentThread().getContextClassLoader().getResource("catalog.xsd");

            LOGGER.info("Loaded XML schema: " + schemaURL);
            schema = sf.newSchema(schemaURL);
        } catch (JAXBException | SAXException e) {
            LOGGER.error("XmlValidationReader initialization error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Book.class;
    }

    @Override
    public long getSize(Book t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Book t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setSchema(schema);
            marshaller.marshal(t, entityStream);
        } catch (JAXBException e) {
            LOGGER.error(e);
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }

    }
}
