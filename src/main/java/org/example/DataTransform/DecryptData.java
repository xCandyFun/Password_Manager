package org.example.DataTransform;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DecryptData {

    // Decrypt the account info using the master password and salt
    private String decryptData(String encryptedData, String password, String salt) {
        try {
            SecretKey key = EncryptData.deriveKey(password, salt);
            Cipher cipher = Cipher.getInstance(EncryptData.algorithm);

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException("Encryption failed due to " + e.getMessage(), e);
        }
    }
}
