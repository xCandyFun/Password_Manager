package org.example.DataTransform;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.UsbConfig.UsbDetector;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.KmsException;

import javax.swing.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AWS_KMS_DecryptData {

    UsbDetector usbDetector = new UsbDetector();

    JFrame frame = new JFrame();

    File usbPath = usbDetector.findUsb(frame);
    String filePath = usbPath.toString();

    Dotenv dotenv = Dotenv.configure().directory(filePath).filename(".env").load();

    String KMSKeyID = dotenv.get("KMS_KEY_ID");
    String encryptedMasterPassword;



    public String decryptMasterPassword() {
        try (KmsClient kmsClient = KmsClient.create()){
            SdkBytes encryptedBytes = SdkBytes.fromByteArray(Base64.getDecoder().decode(encryptedMasterPassword));

            DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(encryptedBytes)
                    .build();

            DecryptResponse decryptResponse = DecryptResponse.builder()
                    .ciphertextForRecipient(encryptedBytes)
                    .build();
            SdkBytes decryptedData = decryptResponse.plaintext();

            return decryptedData.asString(StandardCharsets.UTF_8);
        }catch (KmsException e){
            System.err.println("Error during decryption: " + e.getMessage());
            throw e;
        }
    }
}
