package org.example;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHandler {

    private final String envPath = "D:\\.env";
    private final File envFile = new File(envPath);
    private final Map<Object, Object> envMapUsername = new HashMap<>();
    private final Map<Object, Object> envMapPassword = new HashMap<>();

    public void EnvEditor(List<Object> account, JFrame frame){

        Object key = account.get(0);
        Object value = account.get(1);

        System.out.println(key);
        System.out.println(value);

        long isEmpty = 0;

        if (envFile.length() == isEmpty){
            SaveEnvFile(account,frame);
        }else {
            JOptionPane.showMessageDialog(frame, "ERROR: The file has content");
        }

    }

    private void SaveEnvFile(List<Object> account, JFrame frame){

        Object keyUsername = "USERNAME_LOGIN";
        Object valueUsername = account.get(0);

        Object keyPassword = "PASSWORD_LOGIN";
        Object valuePassword = account.get(1);

        if ((keyUsername.equals("") || valueUsername.equals("")) && (keyPassword.equals("") || valuePassword.equals(""))){
            JOptionPane.showMessageDialog(frame,"Key and Value cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        envMapUsername.put(keyUsername, valueUsername);
        envMapPassword.put(keyPassword, valuePassword);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(envPath))){
            for (Map.Entry<Object, Object> entry : envMapUsername.entrySet()){
                writer.write(entry.getKey() + " = " + entry.getValue());
                writer.newLine();
            }
            for (Map.Entry<Object, Object> entry : envMapPassword.entrySet()){
                writer.write(entry.getKey() + " = " + entry.getValue());
                writer.newLine();
            }
        }catch (IOException e){
            System.out.println("Filed to save to file: " + e);
        }
    }
}
