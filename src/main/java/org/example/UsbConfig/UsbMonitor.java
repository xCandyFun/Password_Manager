package org.example.UsbConfig;

import org.example.Windows.LoginWindow;
import org.example.Windows.MainWindow;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class UsbMonitor {

    UsbDetector usbDetector = new UsbDetector();

    String fileName = ".env";

    File usbPath = usbDetector.findUsb(LoginWindow.frame);

    String fullPath = usbPath.toString() + fileName;

    public void checkUsb() {

        if (!Files.exists(Path.of(fullPath))) {
            System.out.println("USB disconnected! Exiting program...");
            System.exit(0);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Have no ide on what I should have here
        }

    }
}
