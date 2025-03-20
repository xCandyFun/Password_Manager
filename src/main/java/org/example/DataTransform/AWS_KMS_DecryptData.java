package org.example.DataTransform;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.UsbConfig.UsbDetector;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.KmsException;

import javax.swing.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class AWS_KMS_DecryptData {

    private static final KmsClient kmsClient = KmsClient.builder().region(Region.EU_NORTH_1).build();

    public String decryptMasterPassword(String encryptedMasterPassword) {
        try {
            // Decode Base64 string back into bytes
            SdkBytes encryptedBytes = SdkBytes.fromByteArray(Base64.getDecoder().decode(encryptedMasterPassword));

            DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(encryptedBytes)
                    .encryptionContext(Map.of("Id", "MasterKey"))
                    .build();

            DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);

            System.out.println(decryptResponse);

            return decryptResponse.plaintext().asString(StandardCharsets.UTF_8);

        }catch (KmsException e){
            System.err.println("Error during decryption: " + e.getMessage());
            throw e;
        }
    }
}
