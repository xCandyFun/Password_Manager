package org.example.ConnectToDB;


import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.UUID;

public class ConnectDynamoDB {

    private final UUID uuid = UUID.randomUUID();

    private final String tableName = "MasterKeyEncrypted";

    private final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.EU_NORTH_1)
            .build();

    public void storeEncryptedPassword(String encryptedPassword){

        // Create a map for the item
        HashMap<String, AttributeValue> item = new HashMap<>();

        item.put("Id", AttributeValue.builder().s(uuid.toString()).build());
        item.put("encryptedPassword", AttributeValue.builder().s(encryptedPassword).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        try {
            dynamoDbClient.putItem(putItemRequest);
            System.out.println("Encrypted password stored successfully.");
        }catch (DynamoDbException e){
            System.err.println("Error storing encrypted password: " + e.getMessage());
        }
    }


}
