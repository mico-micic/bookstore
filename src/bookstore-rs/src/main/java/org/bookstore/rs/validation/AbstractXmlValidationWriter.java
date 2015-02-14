/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs.validation;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author micic
 */
public abstract class AbstractXmlValidationWriter<T> implements MessageBodyWriter<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractXmlValidationWriter.class);

    private URL schemaURL;
    private Schema schema;
    private JAXBContext jaxbContext;
    private Class<?> type;

    public AbstractXmlValidationWriter(String xsdFile) {

        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        try {
            this.jaxbContext = JAXBContext.newInstance(type);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            this.schemaURL = Thread.currentThread().getContextClassLoader().getResource(xsdFile);

            LOGGER.info("Loaded XML schema: " + schemaURL);
            this.schema = sf.newSchema(this.schemaURL);
        } catch (JAXBException | SAXException e) {
            LOGGER.error("XmlValidationReader initialization error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

        LOGGER.info("Marshalling object " + t + " with XML schema: " + new File(schemaURL.getFile()).getName());

        try {
            Marshaller marshaller = this.jaxbContext.createMarshaller();
            marshaller.setSchema(this.schema);
            marshaller.marshal(t, entityStream);
        } catch (JAXBException e) {
            LOGGER.error(e);
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
