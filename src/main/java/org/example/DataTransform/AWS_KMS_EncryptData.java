package org.example.DataTransform;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.UsbConfig.UsbDetector;
import org.example.Windows.MainWindow;
import org.example.Windows.NewAccountWindow;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;
import software.amazon.awssdk.services.kms.model.KmsException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AWS_KMS_EncryptData {

    JFrame frame = new JFrame();

    UsbDetector usbDetector = new UsbDetector();

    private final File usbPath = usbDetector.findUsb(frame);
    private final String filePath = usbPath.toString();

    Dotenv dotenv = Dotenv.configure().directory(filePath).filename(".env").load();

    String masterPassword = dotenv.get("MASTER_PASSWORD");

    private final String KMSKeyId = dotenv.get("KMS_KEY_ID");

    private static final KmsClient kmsClient = KmsClient.builder().region(Region.EU_NORTH_1).build();

    public String encryptMasterPassword(){
        try {
            SdkBytes plaintextByte = SdkBytes.fromString(masterPassword, StandardCharsets.UTF_8);

            EncryptRequest encryptRequest = EncryptRequest.builder()
                    .keyId(KMSKeyId)
                    .plaintext(plaintextByte)
                    .build();

            EncryptResponse encryptResponse = kmsClient.encrypt(encryptRequest);
            SdkBytes encryptedData = encryptResponse.ciphertextBlob();

            return Base64.getEncoder().encodeToString(encryptedData.asByteArray());

        }catch (KmsException e){
            System.err.println("Error during encryption: " + e.getMessage());
            throw e;
        }
    }
}
