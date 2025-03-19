package org.example;

import org.example.ConnectToDB.DynamodbHelper;
import org.example.DataTransform.AWS_KMS_EncryptData;
import org.example.Windows.LoginWindow;

public class Main {
    static LoginWindow loginWindow = new LoginWindow();

    static  AWS_KMS_EncryptData awsKmsEncryptData = new AWS_KMS_EncryptData();
    static DynamodbHelper dynamodbHelper = new DynamodbHelper();

    public static void main(String[] args) {
        connectToDynamoDb();
        loginWindow.RunWindow();
    }

    private static void connectToDynamoDb(){
        String encryptedMasterKey = awsKmsEncryptData.encryptMasterPassword();
        //connectDynamoDB.storeEncryptedPassword(encryptedMasterKey);
        dynamodbHelper.saveEncryptedMasterKey(encryptedMasterKey);

    }
}