package org.example.Windows;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginWindow {

    //todo env editor so that can change the env file when running

    MainWindow mainWindow = new MainWindow();
    FileHandler fileHandler = new FileHandler();

    private static JFrame frame = new JFrame();
    GridLayout gridLayout = new GridLayout(5, 1);

    private JPanel loginPanel = new JPanel(gridLayout);

    private JPanel usernamePanel = new JPanel();
    private JPanel passwordPanel = new JPanel();
    private JPanel loginButtonPanel = new JPanel();
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private String username;
    private String password;

    private JTextField usernameInput;
    private JTextField passwordInput;

    private JButton loginButton = new JButton("Login");
    private JButton resignButton = new JButton("Resign");

    private final String filePath = "D:\\";
    private final String fileName = ".env";
    private final String fullPath = "D:\\.env";
    private final File envFile = new File(fullPath);

    Dotenv dotenv;

    List<Object> account;

    public void RunWindow() {

        if (!Files.exists(Path.of(filePath + fileName))) {
            JOptionPane.showMessageDialog(frame, ".env file is missing");
            System.exit(0);
        }

        frame.setSize(800, 800);

        usernameLabel = new JLabel("Username: ");
        usernameInput = new JTextField(20);

        passwordLabel = new JLabel("Password: ");
        passwordInput = new JTextField(20);


        ContentInTheWindow(usernameInput, passwordInput);

        ButtonAction(usernameInput, passwordInput);

        Resign();


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    private void ContentInTheWindow(JTextField usernameInput, JTextField passwordInput) {
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameInput);

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordInput);

        loginButtonPanel.add(loginButton);
        loginButtonPanel.add(resignButton);

        loginPanel.add(usernamePanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(loginButtonPanel);

        frame.add(loginPanel);
    }

    private void ButtonAction(JTextField usernameInput, JTextField passwordInput) {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                getTextFromTextField(usernameInput, passwordInput);

                dotenv = Dotenv.configure().directory(filePath).filename(fileName).load();

                String usernameLogin = dotenv.get("USERNAME_LOGIN");
                String passwordLogin = dotenv.get("PASSWORD_LOGIN");

                if ((!Objects.equals(username, "") && !Objects.equals(password, "") &&
                        (username.equals(usernameLogin) && password.equals(passwordLogin)))) {

                    frame.dispose();
                    mainWindow.runMainWindow();
                } else {
                    String message = "Text fields are empty or \n Wrong username and password";
                    JOptionPane.showMessageDialog(loginPanel, message);
                }


            }
        });
    }

    private void Resign(){

        resignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                getTextFromTextField(usernameInput, passwordInput);

                if (!username.isEmpty() && !password.isEmpty()){

                    account = new ArrayList<>();

                    //TODO have a regex to remove unwanted characters
                    account.add(username);
                    account.add(password);

                    //TODO Don't override content in env file
                    if (!envFile.exists()){
                        fileHandler.EnvEditor(account, frame);
                    }else {
                        JOptionPane.showMessageDialog(frame, "under progress");
                    }

                }else {
                    JOptionPane.showMessageDialog(frame, "ERROR: The fields are empty !");
                }


            }
        });
    }

    private void getTextFromTextField(JTextField usernameInput, JTextField passwordInput){
        username = usernameInput.getText();
        password = passwordInput.getText();
    }
}
