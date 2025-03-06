package org.example.Windows;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginWindow {

    //todo env editor so that can change the env file when running

    MainWindow mainWindow = new MainWindow();
    FileHandler fileHandler = new FileHandler();

    private static JFrame frame = new JFrame();
    GridLayout gridLayout = new GridLayout(5, 1);

    private JPanel loginPanel = new JPanel(gridLayout);

    private JPanel usernamePanel = new JPanel();
    private JPanel passwordPanel = new JPanel();
    private JPanel obsPanel = new JPanel();
    private JPanel loginButtonPanel = new JPanel();
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel obsLabel;
    private String email;
    private String password;

    private JTextField usernameInput;
    private JPasswordField passwordInput;

    private JButton loginButton = new JButton("Login");
    private JButton resignButton = new JButton("Resign");

    private final String filePath = "D:\\";
    private final String fileName = ".env";
    private final String fullPath = "D:\\.env";
    private final File envFile = new File(fullPath);

    Dotenv dotenv;

    List<Object> account;

    public void RunWindow() {

        if (Files.exists(Path.of(fullPath))) {

            frame.dispose();
            mainWindow.runMainWindow();

        } else if (!Files.exists(Path.of(fullPath))){

            try{

            frame.setSize(800, 800);

            emailLabel = new JLabel("Email: ");
            usernameInput = new JTextField(20);

            passwordLabel = new JLabel("Password: ");
            passwordInput = new JPasswordField(20);

            obsLabel = new JLabel("<html>*OBS: password need <br> -least one digit " +
                    "<br> -least one lowercase and uppercase letter <br> -least one special character (@#$%^&+=!_) " +
                    "<br> -no whitespace characters <br> -total length least 8 characters</html>");

            obsLabel.setFont(new Font("Arial", Font.BOLD, 12));

                if (envFile.createNewFile()){

                    ContentInTheWindow(usernameInput, passwordInput, obsLabel);

                    ButtonAction(usernameInput, passwordInput);

                    Resign();
                }

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            }catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Did not create the file");
            }
        }
    }

    private void ContentInTheWindow(JTextField usernameInput, JTextField passwordInput, JLabel obsLabel) {
        usernamePanel.add(emailLabel);
        usernamePanel.add(usernameInput);

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordInput);
        passwordPanel.add(obsLabel);
        obsPanel.add(obsLabel);

        loginButtonPanel.add(loginButton);
        loginButtonPanel.add(resignButton);

        loginPanel.add(usernamePanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(obsPanel);
        loginPanel.add(loginButtonPanel);

        frame.add(loginPanel);
    }

    private void ButtonAction(JTextField usernameInput, JTextField passwordInput) {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                getTextFromTextField(usernameInput, passwordInput);

                dotenv = Dotenv.configure().directory(filePath).filename(fileName).load();

                //TODO unencrypt the password

                String usernameLogin = dotenv.get("USERNAME_LOGIN");
                String passwordLogin = dotenv.get("PASSWORD_LOGIN");

                // checks if email and password it's right
                if ((!Objects.equals(email, "") && !Objects.equals(password, "") &&
                        (email.equals(usernameLogin) && password.equals(passwordLogin)))) {

                    frame.dispose();
                    mainWindow.runMainWindow();
                } else {
                    String message = "Text fields are empty or \n Wrong username and password";
                    JOptionPane.showMessageDialog(loginPanel, message);
                }
            }
        });
    }

    private void Resign() {

        resignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                getTextFromTextField(usernameInput, passwordInput);

                if (!email.isEmpty() && !password.isEmpty()) {

                    account = new ArrayList<>();

                    if (isValidEmail(email) && isValidPassword(password)) {
                        account.add(email);
                        account.add(password);
                        JOptionPane.showMessageDialog(frame, "Try now to login");
                        fileHandler.EnvEditor(account, frame);
                    } else {
                        System.out.println("The email or password is not valid");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "ERROR: The fields are empty !");
                }
            }
        });
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9+_,-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        //System.out.println(email.matches(regex));

        return matcher.matches();
    }

    public static boolean isValidPassword(String passwordInput) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=-]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passwordInput);

        //System.out.println(passwordInput.matches(regex));

        return matcher.matches();
    }

    private void getTextFromTextField(JTextField usernameInput, JTextField passwordInput) {
        email = usernameInput.getText();
        password = passwordInput.getText();
    }
}
