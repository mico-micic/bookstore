package org.books.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.books.persistence.util.AesEncryptor;

/**
 * @author Sigi
 */
@Converter(autoApply = true)
public class AesEncryptorConverter implements AttributeConverter<String, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(String attribute) {
        try {
            return AesEncryptor.newInstance().encrypt(attribute);
        } catch (Exception ex) {
            throw new IllegalArgumentException("The given String cannot be encrypted: " + attribute, ex);
        }
    }

    @Override
    public String convertToEntityAttribute(byte[] dbData) {
        try {
            return AesEncryptor.newInstance().decrypt(dbData);
        } catch (Exception ex) {
            throw new IllegalStateException("The Database-Value cannot be decrypted: " + Arrays.toString(dbData), ex);
        }
    }

}
