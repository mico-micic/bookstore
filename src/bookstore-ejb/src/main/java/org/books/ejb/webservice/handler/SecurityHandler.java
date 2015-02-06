/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.ejb.webservice.handler;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.apache.log4j.Logger;

/**
 * @author micic
 */
public class SecurityHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger LOGGER = Logger.getLogger(SecurityHandler.class);

    private static final String AMAZON_HEADER_NS = "http://security.amazonaws.com/doc/2007-01-01/";
    private static final String ASSOCIATE_TAG = "test0e5d-20";
    private static final String ACCESS_KEY = "AKIAIYFLREOYORYNAQTQ";
    private static final String SECRET_KEY = "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";
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String MAC_ALGORITHM = "HmacSHA256";

    private class SecurityInfo {

        final String associateTag;
        final String AWSAccessKeyId;
        final String timestamp;
        final String signature;

        public SecurityInfo(String associateTag, String AWSAccessKeyId, String timestamp, String signature) {
            this.associateTag = associateTag;
            this.AWSAccessKeyId = AWSAccessKeyId;
            this.timestamp = timestamp;
            this.signature = signature;
        }

        @Override
        public String toString() {
            return "SecurityInfo: "
                    + "[AssociateTag: " + ASSOCIATE_TAG + "], "
                    + "[AWSAccessKeyId: " + ACCESS_KEY + "], "
                    + "[Timestamp: " + timestamp + "], "
                    + "[Signature: " + signature + "]";
        }
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        boolean isRequest = (boolean) context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (isRequest) {
            try {
               
                // Get required SOAP message parts
                SOAPMessage message = context.getMessage();
                SOAPHeader header = message.getSOAPPart().getEnvelope().getHeader();
                SOAPBody body = message.getSOAPBody();
                String operation = body.getFirstChild().getNodeName();
                
                LOGGER.info("Adding Amazon request headers for operation '" + operation + "'");
                
                // Get security info
                SecurityInfo info = getSecurityHeaderValues(operation);
                
                LOGGER.info(info.toString());
                
                // Append security nodes to SOAP header
                header.addAttribute(new QName("xmlns"), AMAZON_HEADER_NS);
                header.addHeaderElement(new QName(AMAZON_HEADER_NS, "AWSAccessKeyId")).setTextContent(info.AWSAccessKeyId);
                header.addHeaderElement(new QName(AMAZON_HEADER_NS, "Timestamp")).setTextContent(info.timestamp);
                header.addHeaderElement(new QName(AMAZON_HEADER_NS, "Signature")).setTextContent(info.signature);

                // Append associate tag as a child node of the request operation
                ((SOAPElement)body.getFirstChild()).addChildElement(new QName("AssociateTag")).setTextContent(info.associateTag);
                
                context.getMessage().saveChanges();
                
            } catch (SOAPException ex) {
                LOGGER.error("An error occurred while adding SOAP headers!", ex);
            } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
                LOGGER.error(null, ex);
            }
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }

    private SecurityInfo getSecurityHeaderValues(String operation) throws NoSuchAlgorithmException, InvalidKeyException {

        DateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        String timestamp = dateFormat.format(Calendar.getInstance().getTime());

        Mac mac = Mac.getInstance(MAC_ALGORITHM);
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), MAC_ALGORITHM);
        mac.init(key);
        byte[] data = mac.doFinal((operation + timestamp).getBytes());
        String signature = DatatypeConverter.printBase64Binary(data);

        return new SecurityInfo(ASSOCIATE_TAG, ACCESS_KEY, timestamp, signature);
    }
}
