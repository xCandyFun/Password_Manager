package org.example.DataTransform;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.ConnectToDB.DynamodbHelper;
import org.example.UsbConfig.UsbDetector;
import software.amazon.awssdk.services.kms.endpoints.internal.Value;

import javax.crypto.*;
import javax.swing.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class DecryptData {

    DynamodbHelper dynamodbHelper = new DynamodbHelper();

    UsbDetector usbDetector = new UsbDetector();

    JFrame frame = new JFrame();

    File usbPath = usbDetector.findUsb(frame);
    String filePath = usbPath.toString();

    //List<String> encryptedData = dynamodbHelper.getEncryptedAccountInfo();

    Dotenv dotenv = Dotenv.configure().directory(filePath).filename(".env").load();

    String masterPassword = dotenv.get("MASTER_PASSWORD");

    private final int listSize = 6;

    // Decrypt the account info using the master password and salt
    public List<String> decryptData() {
        try {

            List<String> encryptedData = dynamodbHelper.getEncryptedAccountInfo();

            if (encryptedData.isEmpty() || encryptedData.size() < listSize){
                throw new IllegalStateException("DynamoDB returned an empty or incomplete list. Cannot decrypt data.");
            }

            //String encryptedAccountInfo = encryptedData.get(1);
            String encryptedPlace = encryptedData.get(1);
            String encryptedEmail = encryptedData.get(2);
            String encryptedUsername = encryptedData.get(3);
            String encryptedPassword = encryptedData.get(4);
            String encryptedSalt = encryptedData.get(5);

            System.out.println("Raw Encrypted Data: " + encryptedData);
            System.out.println(encryptedPlace);
            //String encryptedSalt = encryptedData.get(2);

            //TODO Remove Log
            System.out.println("Encrypted Data (Decryption): " + encryptedData);
            System.out.println("Retrieved Salt (Decryption): " + encryptedSalt);

            //NEED KEY FROM THE DATA BASE
            SecretKey key = EncryptData.deriveKey(masterPassword, encryptedSalt);

            System.out.println("Derived Key (Decryption): " + Base64.getEncoder().encodeToString(key.getEncoded()));

            Cipher cipher = Cipher.getInstance(EncryptData.algorithm);

            cipher.init(Cipher.DECRYPT_MODE, key);

            //byte[] encryptedBytes = Base64.getDecoder().decode(encryptedAccountInfo);
            String decryptedPlace = decrypt(encryptedPlace, cipher);
            String decryptedEmail = decrypt(encryptedEmail, cipher);
            String decryptedUsername = decrypt(encryptedUsername, cipher);
            String decryptedPassword = decrypt(encryptedPassword, cipher);

            System.out.println("Decrypted Data:");
            System.out.println("Place: " + decryptedPlace);
            System.out.println("Email: " + decryptedEmail);
            System.out.println("Username: " + decryptedUsername);
            System.out.println("Password: " + decryptedPassword);

            return Arrays.asList(decryptedPlace, decryptedEmail, decryptedUsername, decryptedPassword);

            //String decryptedString = new String(decryptedBytes);

            //System.out.println("Decrypted data: " + decryptedString);

            //return Arrays.asList(decryptedString.split(","));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException("Encryption failed due to " + e.getMessage(), e);
        }
    }

    private String decrypt(String encryptedData, Cipher cipher){
        try{
            System.out.println("Encrypted Data (Before Decoding): " + encryptedData);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            //byte[]encryptedBytes = Base64.getUrlDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Decryption failed for: " + encryptedData, e);
        }
    }
}
