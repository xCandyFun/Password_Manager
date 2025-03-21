package org.example.Windows;


import org.example.DataTransform.AWS_KMS_EncryptData;
import org.example.DataTransform.EncryptData;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewAccountWindow {

    private MainWindow mainWindow;

    AWS_KMS_EncryptData awsKmsEncryptData = new AWS_KMS_EncryptData();
    EncryptData encryptData = new EncryptData();

    JFrame frame;

    private final int rows = 16, cols = 4;

    private final int width = 800, height = 800;

    GridLayout gridLayout;
    JPanel testPanel;
    JButton button;
    JTextField textField;
    JLabel TESTOnPlaceInCell;
    JLabel accountToWhereLabel;
    JLabel emailInputLabel;
    JLabel usernameInputLabel;
    JLabel passwordInputLabel;
    JLabel passwordSecondLabel;

    JTextField accountToWhereInput;
    JTextField emailInput;
    JTextField usernameInput;
    JPasswordField passwordInput;
    JPasswordField passwordSecondInput;

    JButton sendButton;

    List<String> accountInfo = new ArrayList<>();

    //TODO get the data from usb
    String masterPassword;


    public NewAccountWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;

        gridLayout = new GridLayout(rows, cols);
        frame = new JFrame();
        testPanel = new JPanel(gridLayout);
        button = new JButton("GO BACK");
        textField = new JTextField();
        TESTOnPlaceInCell = new JLabel();
        accountToWhereLabel = new JLabel("Place: ");
        emailInputLabel = new JLabel("Email: ");
        usernameInputLabel = new JLabel("Optional Username: ");
        passwordInputLabel = new JLabel("Password: ");
        passwordSecondLabel = new JLabel("Type password Again: ");


        accountToWhereInput = new JTextField();
        emailInput = new JTextField();
        usernameInput = new JTextField();
        passwordInput = new JPasswordField();
        passwordSecondInput = new JPasswordField();

        sendButton = new JButton("SAVE");

    }

    public void run(){

        placeholderLayout();

        actionButtonGoBack();
        actionButtonSend();

        frame.add(testPanel);

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void placeholderLayout() {
        testPanel.removeAll();
        int totalCells = rows * cols;
        Component[] components = new Component[totalCells];

        for (int i = 0; i < totalCells; i++) {
            components[i] = new JLabel("");
        }

        // [Location on components]
        // [4 * 4 + 1 is the 16 cell]

        components[3 * 3] = accountToWhereLabel;
        components[3 * 3 + 1] = accountToWhereInput;

        components[6 * 2 + 1] = emailInputLabel;
        components[6 * 2 + 2] = emailInput;

        components[8 * 2 + 1] = usernameInputLabel;
        components[8 * 2 + 2] = usernameInput;

        components[10 * 2 + 1] = passwordInputLabel;
        components[10 * 2 + 2] = passwordInput;

        components[12 * 2 + 1] = passwordSecondLabel;
        components[12 * 2 + 2] = passwordSecondInput;

        components[16 * 2 +1 ] = sendButton;

        components[totalCells - 1] = button;

        for (Component comp : components) {
            testPanel.add(comp);
        }

        sendButton.setEnabled(false);

        ActionListener checkPassword = e -> {

            char[] passwordData = passwordInput.getPassword();
            char[] passwordSecondData = passwordSecondInput.getPassword();

            boolean bothEmpty = passwordData.length == 0 && passwordSecondData.length == 0;
            boolean passwordMatch = Arrays.equals(passwordData, passwordSecondData) && !bothEmpty;

            sendButton.setEnabled(passwordMatch);

            Arrays.fill(passwordData,' ');
            Arrays.fill(passwordSecondData,' ');
        };

        passwordInput.addActionListener(checkPassword);
        passwordSecondInput.addActionListener(checkPassword);

        //ActionListener triggers on pressing Enter, but CaretListener detects every keystroke.
        passwordInput.addCaretListener(e -> checkPassword.actionPerformed(null));
        passwordSecondInput.addCaretListener( e -> checkPassword.actionPerformed(null));

    }

    public void actionButtonGoBack(){

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //frame.dispose();
                mainWindow.frame.setVisible(true);
                mainWindow.frame.toFront();

                mainWindow.frame.validate();
                mainWindow.frame.repaint();

            }
        });
    }

    public void actionButtonSend() {

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String accountToWhereData = accountToWhereInput.getText();
                String emailData = emailInput.getText();
                String usernameData = usernameInput.getText();

                char[] passwordData = passwordInput.getPassword();
                char getPasswordData = passwordData[0];

                String passwordDataString = Character.toString(getPasswordData);

                accountInfo.add(accountToWhereData);
                accountInfo.add(emailData);
                accountInfo.add(usernameData);
                accountInfo.add(passwordDataString);

                //TODO call encrypt class to encrypt the data in the List
                //call the class that take care of the database

                encryptData.getEncryptedKey(accountInfo);


            }
        });
    }
}
