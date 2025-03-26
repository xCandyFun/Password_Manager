package org.example.Windows;

import org.example.ConnectToDB.DynamodbHelper;
import org.example.DataTransform.AWS_KMS_EncryptData;
import org.example.DataTransform.DecryptData;
import org.example.DataTransform.EncryptData;
import org.example.UsbConfig.UsbDetector;
import org.example.UsbConfig.UsbMonitor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowAllAccountsWindow {

    private final MainWindow mainWindow;

    private final DecryptData decryptData = new DecryptData();

    private final JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private DynamodbHelper dynamodbHelper;

    private final int rows = 16, cols = 4;

    private final int width = 800, height = 800;


    //JButton sendButton;

    List<String> accountInfo = new ArrayList<>();

    public ShowAllAccountsWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;

        frame = new JFrame();

        dynamodbHelper = new DynamodbHelper();



        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void run(){

        showData();

    }

    private void showData(){
        List<String> accountInfo = decryptData.decryptData();

        //TODO Need to split the account info
        System.out.println(accountInfo);
    }
}
