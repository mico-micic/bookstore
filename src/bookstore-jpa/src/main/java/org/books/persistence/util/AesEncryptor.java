package org.books.persistence.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.owasp.esapi.Encryptor;
import org.owasp.esapi.codecs.Base64;
import org.owasp.esapi.crypto.CipherText;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.reference.crypto.JavaEncryptor;

public class AesEncryptor {

    private static final String PBE_ALGORITHM = "PBEWITHSHA256AND128BITAES-CBC-BC";
    private static final String ALGORITHM = "AES";

    // hardcoded for use in our simple case. In 'real world' this info would
    // come with JNDI.
    private static final String SALT = "WR9bdtN3tMHg75PDK9PoIQ==";
    private static final char[] PASSWORD = "bookstore$2014".toCharArray();

    private final SecretKey key;

    /**
     * Constructor creates secret key. In production we may want to avoid
     * keeping the secret key hanging around in memory for very long.
     */
    private AesEncryptor(SecretKey key) {
        this.key = key;

    }

    /**
     * @return A new AesEncryptor Instance with the generated Key as its
     * Property.
     * @throws IllegalStateException if the Key for Encription can not be
     * initialized.
     */
    public static AesEncryptor newInstance() {
        try {
            // create the PBE key
            KeySpec spec = new PBEKeySpec(PASSWORD, Base64.decode(SALT), 1024);
            SecretKey skey = SecretKeyFactory.getInstance(PBE_ALGORITHM).generateSecret(spec);
            // recast key as straightforward AES without padding.
            SecretKey key = new SecretKeySpec(skey.getEncoded(), ALGORITHM);
            return new AesEncryptor(key);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "Es ist ein kofigurativer Fehler beim initialisieren des AES-Encryptors aufgetreten!", e
            );
        }
    }

    /**
     * @param ciphertext The encrypted String.
     * @return The decrypted String.
     * @throws EncryptionException if the Decryption was not successful.
     */
    public String decryptString(String ciphertext) throws EncryptionException {
        String plaintext = null;

        if (ciphertext != null) {
            Encryptor encryptor = JavaEncryptor.getInstance();
            CipherText ct = CipherText.fromPortableSerializedBytes(Base64.decode(ciphertext));
            plaintext = encryptor.decrypt(key, ct).toString();
        }

        return plaintext;
    }

    /**
     * @param plaintext The plain String.
     * @return The encrypted String.
     * @throws EncryptionException If the Encryption was not successful.
     */
    public String encryptString(String plaintext) throws EncryptionException {
        String ciphertext = null;

        if (plaintext != null) {
            Encryptor encryptor = JavaEncryptor.getInstance();
            CipherText ct = encryptor.encrypt(key, new PlainText(plaintext));
            ciphertext = Base64.encodeBytes(ct.asPortableSerializedByteArray());
        }

        return ciphertext;
    }
}
