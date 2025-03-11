package org.example.Windows;

import org.example.UsbConfig.UsbMonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {

    UsbMonitor usbMonitor = new UsbMonitor();
    NewAccountWindow newAccountWindow = new NewAccountWindow();
    ShowAllAccountsWindow showAllAccountsWindow = new ShowAllAccountsWindow();

    JFrame frame = new JFrame();

    int width = 800, height = 800;

    GridLayout gridLayout = new GridLayout(10,0);

    Panel menu = new Panel(gridLayout);

    Button addNewAccount;
    Button showAllAccounts;


    public void runMainWindow() {


        //TODO Time for save,encrypt,display and copy for logins

        //but not today
        //but today

        addNewAccount = new Button("ADD NEW ACCOUNT");
        showAllAccounts = new Button("SHOW ALL ACCOUNTS"); // copy and delete inside show all accounts

        menu.add(addNewAccount);
        menu.add(showAllAccounts);

        frame.add(menu);

        actionButtonForAddNew();
        actionButtonShowAll();

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (true) {
            usbMonitor.checkUsb();
        }
    }

    private void actionButtonForAddNew(){

        addNewAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.dispose();

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
