package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.UsbConfig.UsbDetector;
import org.example.Windows.LoginWindow;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileHandler {

    UsbDetector usbDetector = new UsbDetector();

    private final File usbPath = usbDetector.findUsb(LoginWindow.frame);
    private final String fileName = ".env";
    private final String fullPath = usbPath.toString() + fileName;
    private final String envPath = fullPath;
    private final File envFile = new File(envPath);
    private final Map<Object, Object> envMapUsername = new HashMap<>();
    private final Map<Object, Object> envMapPassword = new HashMap<>();

    Dotenv dotenv;

    public void EnvEditor(List<Object> account, JFrame frame) {

        Object key = account.get(0);
        Object value = account.get(1);

        System.out.println(key);
        System.out.println(value);

        long isEmpty = 0;

        if (envFile.length() == isEmpty) {
            SaveEnvFile(account, frame);
        } else {
            JOptionPane.showMessageDialog(frame, "ERROR: The file has content");
        }

    }

    private void SaveEnvFile(List<Object> account, JFrame frame) {

        Object keyUsername = "USERNAME_LOGIN";
        Object valueUsername = account.get(0);

        Object keyPassword = "PASSWORD_LOGIN";
        Object valuePassword = account.get(1);

        if ((keyUsername.equals("") || valueUsername.equals("")) && (keyPassword.equals("") || valuePassword.equals(""))) {
            JOptionPane.showMessageDialog(frame, "Key and Value cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        envMapUsername.put(keyUsername, valueUsername);
        envMapPassword.put(keyPassword, valuePassword);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(envPath))) {
            for (Map.Entry<Object, Object> entry : envMapUsername.entrySet()) {
                writer.write(entry.getKey() + " = " + entry.getValue());
                writer.newLine();
            }
            for (Map.Entry<Object, Object> entry : envMapPassword.entrySet()) {
                writer.write(entry.getKey() + " = " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Filed to save to file: " + e);
        }
    }

    public void envChecker(JFrame frame) {

        if (isEnvEmpty(envPath, frame)) {
            System.out.println("Error: .env file is empty! Closing application.");
            System.exit(1);
        }


    }

    private boolean isEnvEmpty(String envPathFile, JFrame frame) {

        dotenv = Dotenv.configure().directory(usbPath.toString()).filename(fileName).load();

        String username = dotenv.get("USERNAME_LOGIN");
        String password = dotenv.get("PASSWORD_LOGIN");

        Path path = Paths.get(envPathFile);

        try (Stream<String> lines = Files.lines(path)) {

            boolean hasContent = lines.anyMatch(line -> line.trim().matches("^[a-zA-Z_]+\\s*.*$"));

            if (!hasContent || username == null || password == null) {
                Files.delete(path);
                JOptionPane.showMessageDialog(frame, "Deleted empty .env file.");
                return true;
            }
        } catch (IOException e) {
            System.out.println("File doesn't exist or can't be read");
            return true;
        }


        return false;
    }
}
