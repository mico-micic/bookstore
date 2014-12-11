/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.books.persistence.util.AesEncryptor;
import org.owasp.esapi.errors.EncryptionException;

/**
 * @author Sigi
 */
@Converter(autoApply = true)
public class AesEncryptorConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return AesEncryptor.newInstance().encryptString(attribute);
        } catch (EncryptionException ex) {
            throw new IllegalArgumentException("The given String cannot be encrypted: " + attribute, ex);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return AesEncryptor.newInstance().decryptString(dbData);
        } catch (EncryptionException ex) {
            throw new IllegalStateException("The Database-Value cannot be decrypted: " + dbData, ex);
        }
    }

}
