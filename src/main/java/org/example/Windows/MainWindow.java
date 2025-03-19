package org.example.Windows;

import org.example.ConnectToDB.ConnectDynamoDB;
import org.example.ConnectToDB.DynamodbHelper;
import org.example.DataTransform.AWS_KMS_EncryptData;
import org.example.UsbConfig.UsbMonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {

    UsbMonitor usbMonitor = new UsbMonitor();
    private NewAccountWindow newAccountWindow;
    ShowAllAccountsWindow showAllAccountsWindow = new ShowAllAccountsWindow();
    ConnectDynamoDB connectDynamoDB = new ConnectDynamoDB();
    DynamodbHelper dynamodbHelper = new DynamodbHelper();
    AWS_KMS_EncryptData awsKmsEncryptData = new AWS_KMS_EncryptData();

    JFrame frame;

    GridLayout gridLayout = new GridLayout(10,0);

    private final int width = 800, height = 800;

    Panel menu;

    Button addNewAccount;
    Button showAllAccounts;

    public MainWindow(){
        newAccountWindow = new NewAccountWindow(this);

        frame = new JFrame();

        menu = new Panel(gridLayout);

        addNewAccount = new Button("ADD NEW ACCOUNT");
        showAllAccounts = new Button("SHOW ALL ACCOUNTS"); // copy and delete inside show all accounts

        menu.add(addNewAccount);
        menu.add(showAllAccounts);

        frame.add(menu);



        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }




    public void runMainWindow() {

        //TODO Time for save,encrypt,display and copy for logins

        actionButtonForAddNew();
        actionButtonShowAll();





        while (true) {
            usbMonitor.checkUsb();
        }
    }

    private void actionButtonForAddNew(){

        addNewAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //frame.dispose();
                frame.setVisible(false );

                newAccountWindow.run();

            }
        });
    }

    private void actionButtonShowAll(){
        showAllAccounts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.dispose();

                showAllAccountsWindow.run();
            }
        });
    }

}
