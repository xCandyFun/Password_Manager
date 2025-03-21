package org.example.DataTransform;

import org.example.ConnectToDB.DynamodbHelper;

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
    DynamodbHelper dynamodbHelper = new DynamodbHelper();

    AWS_KMS_DecryptData awsKmsDecryptData = new AWS_KMS_DecryptData();

    //TODO text time encrypt the data
    //TODO NEED MASTER password
    public static final String algorithm = "AES";
    private static final String pbkAlgorithm = "PBKDF2WithHmacSHA256";
    private static final int iterationCount = 1000;
    private static final int keyLength = 256;





    public void getEncryptedKey(List<String> accountInfo){

        // get encrypted key
        String encryptedMasterKey = dynamodbHelper.getEncryptedKey();

        //decrypted key
        String decryptedMasterKey = awsKmsDecryptData.decryptMasterPassword(encryptedMasterKey);

        storeAccountData(accountInfo, decryptedMasterKey);

    }

    // Method to generate the encryption key from the master password and salt
    public static SecretKey deriveKey(String masterPassword, String salt){
        try {
            PBEKeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt.getBytes(),
                    iterationCount, keyLength);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(pbkAlgorithm);

            byte[] derivedKey = factory.generateSecret(spec).getEncoded();

            return new SecretKeySpec(derivedKey, algorithm);

        }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }
    }

    // Encrypt the account info using the master password and salt
    public String encryptList(List<String> accountInfo, String masterPassword, String salt) {
        try {
            SecretKey key = deriveKey(masterPassword, salt);
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
    private void storeAccountData(List<String> encryptedAccountInfoList, String masterPassword) {

        try {

            String salt = generateSalt();

            String encryptedData = encryptList(encryptedAccountInfoList, masterPassword, salt);

            List<String> encryptedDataAndSalt = new ArrayList<>();

            encryptedDataAndSalt.add(encryptedData);
            encryptedDataAndSalt.add(salt);

            // Store the encrypted data and salt (in DynamoDB, database, or secure storage)
            dynamodbHelper.insertEncryptedAccountInfo(encryptedDataAndSalt);


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
