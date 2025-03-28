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
import java.util.*;

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
    public List<Map<String, String>> decryptData() {
        try {

            List<Map<String, String>> encryptedDataList = dynamodbHelper.getEncryptedAccountInfo();

            //System.out.println("Encrypted Data List: " + encryptedDataList);

            if (encryptedDataList.isEmpty()){
                throw new IllegalStateException("DynamoDB returned an empty or incomplete list. Cannot decrypt data.");
            }

            List<Map<String, String>> decryptedAccounts = new ArrayList<>();

            for (Map<String, String> encryptedData : encryptedDataList){

                String encryptedPlace = encryptedData.get("EncryptedPlace");
                String encryptedEmail = encryptedData.get("EncryptedEmail");
                String encryptedUsername = encryptedData.get("EncryptedUsername");
                String encryptedPassword = encryptedData.get("EncryptedPassword");
                String encryptedSalt = encryptedData.get("EncryptedSalt");

                SecretKey key = EncryptData.deriveKey(masterPassword, encryptedSalt);
                Cipher cipher = Cipher.getInstance(EncryptData.algorithm);
                cipher.init(Cipher.DECRYPT_MODE, key);

                Map<String, String> decryptedRecord = new HashMap<>();

                decryptedRecord.put("DecryptedPlace", decrypt(encryptedPlace, cipher));
                decryptedRecord.put("DecryptedEmail", decrypt(encryptedEmail, cipher));
                decryptedRecord.put("DecryptedUsername", decrypt(encryptedUsername, cipher));
                decryptedRecord.put("DecryptedPassword", decrypt(encryptedPassword, cipher));

                decryptedAccounts.add(decryptedRecord);
            }

            return decryptedAccounts;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException("Encryption failed due to " + e.getMessage(), e);
        }
    }

    private String decrypt(String encryptedData, Cipher cipher){
        try{
            //System.out.println("Encrypted Data (Before Decoding): " + encryptedData);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            //byte[]encryptedBytes = Base64.getUrlDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Decryption failed for: " + encryptedData, e);
        }
    }
}
