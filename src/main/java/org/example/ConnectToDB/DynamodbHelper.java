package org.example.ConnectToDB;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.UsbConfig.UsbDetector;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class DynamodbHelper {

    private final DynamoDbClient dynamoDbClient;

    UsbDetector usbDetector = new UsbDetector();

    JFrame frame = new JFrame();

    File usbPath = usbDetector.findUsb(frame);
    String filePath = usbPath.toString();

    Dotenv dotenv = Dotenv.configure().directory(filePath).filename(".env").load();

    String userUUID = dotenv.get("USER_UUID");

    UUID accountsUUID = UUID.randomUUID();

    private final String tableName = "MasterKeyEncrypted";

    private final String tableNameAccounts = "AccountsInfo";

    private final int listSize = 5;

    public DynamodbHelper(){
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.EU_NORTH_1)
                .build();
    }

    public boolean saveEncryptedMasterKey(String encryptedPassword){

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("Id", AttributeValue.builder().s(userUUID).build()))
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);

        if (getItemResponse.hasItem()) {

            String existingEncryptedPassword = getItemResponse.item().get("EncryptedPassword").s();

            if (!existingEncryptedPassword.equals(encryptedPassword)) {
                updateEncryptedKey(dynamoDbClient, userUUID, encryptedPassword);
                System.out.println("Update key");
                getEncryptedKey();
                return true;
            } else {
                System.out.println("Password hasn't changed. No update required.");
                return false;
            }
        } else {
            insertEncryptedKey(dynamoDbClient, userUUID, encryptedPassword);
            System.out.println("Added new key");
            return true;
        }
    }

    private void insertEncryptedKey(DynamoDbClient dynamoDbClient, String userUUID, String enencryptedPassword){

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(Map.of("Id", AttributeValue.builder().s(userUUID).build(),
                        "EncryptedPassword", AttributeValue.builder().s(enencryptedPassword).build()
                ))
                .build();

        dynamoDbClient.putItem(putItemRequest);
    }

    private void updateEncryptedKey(DynamoDbClient dynamoDbClient, String userUUID, String encryptedPassword) {

        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("Id", AttributeValue.builder().s(userUUID).build()))
                .updateExpression("SET EncryptedPassword = :encryptedPassword")
                .expressionAttributeValues(Map.of(":encryptedPassword", AttributeValue.builder().s(encryptedPassword).build()))
                .build();
        dynamoDbClient.updateItem(updateItemRequest);
    }

    public String getEncryptedKey(){

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("Id", AttributeValue.builder().s(userUUID).build()))
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);

        Map<String, AttributeValue> item = getItemResponse.item();

        if (item != null && item.containsKey("EncryptedPassword")){
            return item.get("EncryptedPassword").s();
        }else {
            return null;
        }

    }

    public void insertEncryptedAccountInfo(List<String> encryptedDataAndSalt){
        if (encryptedDataAndSalt.size() < listSize){
            throw new IllegalArgumentException("Invalid data: Expected at least 5 elements (4 encrypted values + salt).");
        }

        //String encryptedData = encryptedDataAndSalt.get(0);
        String encryptedPlace = encryptedDataAndSalt.get(0);
        String encryptedEmail = encryptedDataAndSalt.get(1);
        String encryptedUsername = encryptedDataAndSalt.get(2);
        String encryptedPassword = encryptedDataAndSalt.get(3);
        String encryptedSalt = encryptedDataAndSalt.get(4);

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableNameAccounts)
                .item(Map.of("Id", AttributeValue.builder().s(accountsUUID.toString()).build(),
                        "EncryptedPlace", AttributeValue.builder().s(encryptedPlace).build(),
                        "EncryptedEmail", AttributeValue.builder().s(encryptedEmail).build(),
                        "EncryptedUsername", AttributeValue.builder().s(encryptedUsername).build(),
                        "EncryptedPassword", AttributeValue.builder().s(encryptedPassword).build(),
                        "EncryptedSalt", AttributeValue.builder().s(encryptedSalt).build()
                ))
                .build();

        dynamoDbClient.putItem(putItemRequest);
    }

    public List<Map<String, String>> getEncryptedAccountInfo(){

        List<Map<String, AttributeValue>> AllItems = scanTable();

        List<Map<String, String>> encryptedAccountList = new ArrayList<>();

        for (Map<String, AttributeValue> item : AllItems){

            Map<String, String> record = new HashMap<>();

            record.put("Id", item.get("Id").s());
            record.put("EncryptedPlace", item.get("EncryptedPlace").s());
            record.put("EncryptedEmail", item.get("EncryptedEmail").s());
            record.put("EncryptedUsername", item.get("EncryptedUsername").s());
            record.put("EncryptedPassword", item.get("EncryptedPassword").s());
            record.put("EncryptedSalt", item.get("EncryptedSalt").s());

            encryptedAccountList.add(record);

//            String Id = item.get("Id").s();
//            //String encryptedAccount = item.get("EncryptedAccount").s();
//            String encryptedPlace = item.get("EncryptedPlace").s();
//            String encryptedEmail = item.get("EncryptedEmail").s();
//            String encryptedUsername = item.get("EncryptedUsername").s();
//            String encryptedPassword = item.get("EncryptedPassword").s();
//            String encryptedSalt = item.get("EncryptedSalt").s();
//
//            encryptedAccountList.add(Id);
//            //encryptedAccountList.add(encryptedAccount);
//            encryptedAccountList.add(encryptedPlace);
//            encryptedAccountList.add(encryptedEmail);
//            encryptedAccountList.add(encryptedUsername);
//            encryptedAccountList.add(encryptedPassword);
//            encryptedAccountList.add(encryptedSalt);

        }

        return encryptedAccountList;
    }

    private List<Map<String, AttributeValue>> scanTable(){

        List<Map<String, AttributeValue>> allItems = new ArrayList<>();
        Map<String, AttributeValue> lastEvaluatedKey = null;

        do {
            ScanRequest.Builder scanRequestBuilder = ScanRequest.builder().tableName(tableNameAccounts);
            if (lastEvaluatedKey != null){
                scanRequestBuilder.exclusiveStartKey(lastEvaluatedKey);
            }

            ScanResponse scanResponse = dynamoDbClient.scan(scanRequestBuilder.build());
            allItems.addAll(scanResponse.items());

            lastEvaluatedKey = scanResponse.hasLastEvaluatedKey() ? scanResponse.lastEvaluatedKey() : null;

        }while (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty());

        return allItems;

    }



}
