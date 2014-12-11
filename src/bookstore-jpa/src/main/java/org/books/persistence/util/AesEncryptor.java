package org.books.persistence.util;

import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * This is a simple AES-Encryptor based on the DeSede-Algorithm. For serious
 * use, we would change the Algorithm to a Password protected Key / Salt based
 * Encription. But for our educational Project this simple 'encryption' will do
 * it's job.
 *
 * @author Sigi
 */
public class AesEncryptor {

    private final Cipher cipher;
    private final SecretKey secretKey;

    private AesEncryptor(Cipher cipher, SecretKey secretKey) {
        this.cipher = cipher;
        this.secretKey = secretKey;
    }

    public static AesEncryptor newInstance() throws NoSuchAlgorithmException, NoSuchPaddingException {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
        // keysize must be equal to 112 or 168 for this provider
        keyGenerator.init(168);
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("DESede");
        return new AesEncryptor(cipher, secretKey);
    }

    public byte[] encrypt(String plainText) throws Exception {
        byte[] plainTextByte = plainText.getBytes("UTF8");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainTextByte);
        return encryptedBytes;
    }

    public String decrypt(byte[] encryptedBytes) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return Arrays.toString(decryptedBytes);
    }
}
