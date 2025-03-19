package org.example.DataTransform;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class EncryptData {
    AWS_KMS_EncryptData awsKms = new AWS_KMS_EncryptData();
    AWS_KMS_DecryptData awsKmsDecryptData = new AWS_KMS_DecryptData();

    //TODO text time encrypt the data
    //TODO NEED MASTER password
    public static final String algorithm = "AES";
    private static final String pbkAlgorithm = "PBKDF2WithHmacSHA256";
    private static final int iterationCount = 1000;
    private static final int keyLength = 256;

    String masterPassword = "TEST";
    //The encrypted password is returned in Base64 format, so it can be safely stored (in a database, for example).
    //String encryptedPassword = awsKms.encryptMasterPassword(masterPassword);

    // Example: Simulate retrieving encrypted password from DynamoDB
    String encryptedPasswordBase64 = "TEST";

    // Decrypt the password
    //String decryptedPassword = awsKmsDecryptData.decryptMasterPassword(encryptedPasswordBase64);

    // Method to generate the encryption key from the master password and salt
    public static SecretKey deriveKey(String password, String salt){
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(),
                    iterationCount, keyLength);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(pbkAlgorithm);

            byte[] derivedKey = factory.generateSecret(spec).getEncoded();

            return new SecretKeySpec(derivedKey, algorithm);

        }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }
    }

    // Encrypt the account info using the master password and salt
    public String encryptList(List<String> accountInfo, String password, String salt) {
        try {
            SecretKey key = deriveKey(password, salt);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Convert account info to bytes and encrypt
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (String str : accountInfo) {
                outputStream.write(str.getBytes());
            }
            byte[] inputBytes = outputStream.toByteArray();

            byte[] encryptedBytes = cipher.doFinal(inputBytes);

            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (IOException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
                 | NoSuchPaddingException | InvalidKeyException e){
            throw new RuntimeException("Encryption failed due to " + e.getMessage(), e);
        }
    }

    // Store encrypted account data (and salt) in a secure way
    private void storeAccountData(List<String> accountInfo, String password) {

        try {

            String salt = generateSalt();

            String encyptedData = encryptList(accountInfo, password, salt);

            List<String> encyptedDataAndSalt = new ArrayList<>();

            encyptedDataAndSalt.add(encyptedData);
            encyptedDataAndSalt.add(salt);

            // Store the encrypted data and salt (in DynamoDB, database, or secure storage)


        } catch (Exception e) {
            throw new RuntimeException("Encryption failed due to " + e.getMessage(), e);
        }
    }

    public SecretKey generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Encryption failed due to " + e.getMessage(), e);
        }
    }

    // Generate a unique salt for each account (used for key derivation)
    public String generateSalt(){
        SecureRandom random = new SecureRandom();

        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
