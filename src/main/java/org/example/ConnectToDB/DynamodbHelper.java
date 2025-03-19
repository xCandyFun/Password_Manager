package org.example.ConnectToDB;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.UsbConfig.UsbDetector;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DynamodbHelper {


    private final DynamoDbClient dynamoDbClient;

    UsbDetector usbDetector = new UsbDetector();

    JFrame frame = new JFrame();

    File usbPath = usbDetector.findUsb(frame);
    String filePath = usbPath.toString();

    Dotenv dotenv = Dotenv.configure().directory(filePath).filename(".env").load();

    String userUUID = dotenv.get("USER_UUID");

    private final String tableName = "MasterKeyEncrypted";

    public DynamodbHelper(){
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.EU_NORTH_1)
                .build();
    }


    public void saveEncryptedMasterKey(String encryptedPassword){

        System.out.println(userUUID);

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("Id", AttributeValue.builder().s(userUUID.toString()).build()))
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);

        if (getItemResponse.hasItem()) {

            String existingEncryptedPassword = getItemResponse.item().get("EncryptedPassword").s();

            if (!existingEncryptedPassword.equals(encryptedPassword)) {
                updateEncryptedKey(dynamoDbClient, userUUID, encryptedPassword);
                System.out.println("Update key");
            } else {
                System.out.println("Password hasn't changed. No update required.");
            }
        } else {
            insertEncryptedKey(dynamoDbClient, userUUID, encryptedPassword);
            System.out.println("Added new key");
        }
    }

    private void insertEncryptedKey(DynamoDbClient dynamoDbClient, String userUUID, String enencryptedPassword){

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(Map.of("Id", AttributeValue.builder().s(userUUID.toString()).build(),
                        "EncryptedPassword", AttributeValue.builder().s(enencryptedPassword).build()
                ))
                .build();

        dynamoDbClient.putItem(putItemRequest);
    }

    private void updateEncryptedKey(DynamoDbClient dynamoDbClient, String userUUID, String encryptedPassword) {

        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("Id", AttributeValue.builder().s(userUUID.toString()).build()))
                .updateExpression("SET EncryptedPassword = :encryptedPassword")
                .expressionAttributeValues(Map.of(":encryptedPassword", AttributeValue.builder().s(encryptedPassword).build()))
                .build();
        dynamoDbClient.updateItem(updateItemRequest);
    }



}
