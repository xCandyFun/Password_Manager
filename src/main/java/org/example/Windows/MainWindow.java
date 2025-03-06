package org.example.Windows;

import org.example.UsbMonitor;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainWindow {

    JFrame frame = new JFrame();

    private final String filePath = "D:\\";

    UsbMonitor usbMonitor = new UsbMonitor();

    public void runMainWindow() {

        frame.setSize(800, 800);

        //TODO Time for save,encrypt,display and copy for logins

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (true) {
            usbMonitor.checkUsb();
        }
    }
}
