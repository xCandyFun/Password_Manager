package org.example.Windows;

import org.example.UsbConfig.UsbDetector;
import org.example.UsbConfig.UsbMonitor;

import javax.swing.*;

public class ShowAllAccountsWindow {

    UsbMonitor usbMonitor = new UsbMonitor();

    JFrame frame = new JFrame();

    int width = 800, height = 800;

    public void run() {



        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        checkUsb();
    }

    private void checkUsb(){
        while (true){
            usbMonitor.checkUsb();
        }
    }
}
