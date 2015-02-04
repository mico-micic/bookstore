/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.webservice.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.log4j.Logger;

/**
 * @author micic
 */
public class LogHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger LOGGER = Logger.getLogger(LogHandler.class);
    
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        boolean isOutbound = (boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
        log("handleMessage [" + (isOutbound ? "OUTBOUND" : "INBOUND") + "]", context);
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        
        boolean isOutbound = (boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
        log("handleFault [" + (isOutbound ? "OUTBOUND" : "INBOUND") + "]", context);
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }

    private void log(String msg, SOAPMessageContext context) {

        try {
            try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
                context.getMessage().writeTo(outStream);
                LOGGER.info(msg + " " + outStream.toString());
            }
        } catch (SOAPException | IOException ex) {
            LOGGER.error(null, ex);
        }
    }
}
