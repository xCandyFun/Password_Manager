package org.example.Windows;

import org.example.UsbConfig.UsbMonitor;

import javax.swing.*;

public class MainWindow {

    JFrame frame = new JFrame();

    UsbMonitor usbMonitor = new UsbMonitor();

    public void runMainWindow() {

        frame.setSize(800, 800);

        //TODO Time for save,encrypt,display and copy for logins

        //but not today

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        while (true) {
            usbMonitor.checkUsb();
        }
    }
}
