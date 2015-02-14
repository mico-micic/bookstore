/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bookstore.rs.validation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.bookstore.rs.exception.BadRequestException;
import org.xml.sax.SAXException;

/**
 *
 * @author micic
 */
public abstract class AbstractXmlValidationReader<T> implements MessageBodyReader<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractXmlValidationReader.class);

    private URL schemaURL;
    private Schema schema;
    private JAXBContext jaxbContext;
    private Class<?> type;

    public AbstractXmlValidationReader(String xsdFile) {

        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        
        try {
            jaxbContext = JAXBContext.newInstance(this.type);
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            schemaURL = Thread.currentThread().getContextClassLoader().getResource(xsdFile);

            LOGGER.info("Loaded XML schema: " + schemaURL);
            schema = sf.newSchema(schemaURL);

        } catch (JAXBException | SAXException e) {
            LOGGER.error("XmlValidationReader initialization error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
        return arg0 == this.type;
    }

    @Override
    public T readFrom(Class<T> arg0, Type arg1, Annotation[] arg2, MediaType arg3, 
            MultivaluedMap<String, String> arg4, InputStream input)
            throws IOException, WebApplicationException {

        LOGGER.info("Unmarshalling object with XML schema: " + new File(schemaURL.getFile()).getName());

        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            return (T) unmarshaller.unmarshal(input);
        } catch (JAXBException e) {
            LOGGER.error(e);
            Throwable linkedEx = e.getLinkedException();
            String msg = e.getLocalizedMessage() == null ? "" : e.getLocalizedMessage() + " ";
            throw new BadRequestException(msg + (linkedEx != null ? linkedEx.getLocalizedMessage() : ""));
        }
    }
}
