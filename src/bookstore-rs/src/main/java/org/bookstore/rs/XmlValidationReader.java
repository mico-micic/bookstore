/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
public class XmlValidationReader implements MessageBodyReader<Book> {

    private static final Logger LOGGER = Logger.getLogger(XmlValidationReader.class);
    
    private Schema schema;
    private JAXBContext jaxbContext;
    
    public XmlValidationReader() {
       
        try {
            jaxbContext = JAXBContext.newInstance(Book.class);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            URL schemaURL = Thread.currentThread().getContextClassLoader().getResource("catalog.xsd"); 
            
            LOGGER.info("Loaded XML schema: " + schemaURL);
            schema = sf.newSchema(schemaURL); 
        } catch(JAXBException | SAXException e) {
            LOGGER.error("XmlValidationReader initialization error", e);
            throw new RuntimeException(e);
        }
    }
   
    public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
        System.out.println("isReadable: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return arg0 == Book.class;
    }

    public Book readFrom(Class<Book> arg0, Type arg1, Annotation[] arg2, MediaType arg3, MultivaluedMap<String, String> arg4, InputStream arg5)
            throws IOException, WebApplicationException {
        
        System.out.println("readFrom: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            return (Book) unmarshaller.unmarshal(arg5);
        } catch(JAXBException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    } 
}
