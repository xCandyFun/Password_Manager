package org.example.UsbConfig;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class UsbDetector {

    private static final FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private static final File[] roots = File.listRoots();

    public void listAllDrives() {
        System.out.println("Detected drives:");

        for (File root : roots){
            System.out.println(root.getAbsoluteFile() + " - " + fileSystemView.getSystemTypeDescription(root));
        }

    }

    public File findUsb(JFrame frame) {

        for (File root : roots) {
            String description = fileSystemView.getSystemTypeDescription(root);
            //System.out.println("Checking: " + root.getAbsolutePath() + " - " + description);

            if (fileSystemView.isDrive(root) && description != null &&
                    (description.toLowerCase().contains("removable") || description.toLowerCase().contains("usb drive"))) {
                return root;
            }
        }

        JOptionPane.showMessageDialog(frame, "USB is NOT connected");
        System.exit(0);

        return null;
    }
}
