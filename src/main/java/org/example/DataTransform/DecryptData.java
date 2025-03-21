package org.example.DataTransform;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.ConnectToDB.DynamodbHelper;
import org.example.UsbConfig.UsbDetector;

import javax.crypto.*;
import javax.swing.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class DecryptData {

    DynamodbHelper dynamodbHelper = new DynamodbHelper();

    UsbDetector usbDetector = new UsbDetector();

    JFrame frame = new JFrame();

    File usbPath = usbDetector.findUsb(frame);
    String filePath = usbPath.toString();

    List<String> encryptedData = dynamodbHelper.getEncryptedAccountInfo();

    Dotenv dotenv = Dotenv.configure().directory(filePath).filename(".env").load();

    String encryptedAccountInfo = encryptedData.get(1);
    String encryptedSalt = encryptedData.get(2);
    String masterPassword = dotenv.get("MASTER_PASSWORD");

    // Decrypt the account info using the master password and salt
    public List<String> decryptData() {
        try {
            SecretKey key = EncryptData.deriveKey(masterPassword, encryptedSalt);
            Cipher cipher = Cipher.getInstance(EncryptData.algorithm);

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedAccountInfo));
            String decryptedString = new String(decryptedBytes);

            return Arrays.asList(decryptedString.split(","));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException("Encryption failed due to " + e.getMessage(), e);
        }
    }
}
