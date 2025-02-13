package org.example.Windows;

import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainWindow {

    private static JFrame frame = new JFrame();
    private JPanel topPanel = new JPanel();
    private JPanel nextTopPanel = new JPanel();
    private JPanel middlePanel = new JPanel();
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private String username;
    private String password;
    private JButton saveButton = new JButton("Save");
    GridLayout gridLayout = new GridLayout(5,1);
    Dotenv dotenv;

    List<Object> account;

    public void RunWindow(){

        frame.setSize(800,800);
        frame.setLayout(gridLayout);

        usernameLabel = new JLabel("Username: ");
        JTextField usernameInput = new JTextField(20);

        passwordLabel = new JLabel("Password: ");
        JTextField passwordInput = new JTextField(20);


        ContentInTheWindow(usernameInput, passwordInput);



        ButtonAction(usernameInput, passwordInput);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    private void ContentInTheWindow(JTextField usernameInput, JTextField passwordInput) {
        topPanel.add(usernameLabel);
        topPanel.add(usernameInput);

        nextTopPanel.add(passwordLabel);
        nextTopPanel.add(passwordInput);

        middlePanel.add(saveButton);

        frame.add(topPanel);
        frame.add(nextTopPanel);
        frame.add(middlePanel);
    }

    private void ButtonAction(JTextField usernameInput, JTextField passwordInput) {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                username = usernameInput.getText();
                password = passwordInput.getText();

                dotenv = Dotenv.configure().load();

                String usernameLogin = dotenv.get("USERNAME_LOGIN");
                String passwordLogin = dotenv.get("PASSWORD_LOGIN");

                if (!Objects.equals(username, "") && !Objects.equals(password, "")){

                    account = new ArrayList<>();

                    account.add(username);
                    account.add(password);

                    if (username.equals(usernameLogin) && password.equals(passwordLogin)){
                        System.out.println("Welcome User");
                    }


                }else {
                    System.out.println("The text field is empty");
                }
            }
        });
    }
}
