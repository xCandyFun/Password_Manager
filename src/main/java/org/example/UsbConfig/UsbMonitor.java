package org.example.UsbConfig;

import java.nio.file.Files;
import java.nio.file.Path;

public class UsbMonitor {

    String fullPath = "D:\\.env";

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
