package org.example.Windows;

import javax.swing.*;

public class MainWindow {

    JFrame frame = new JFrame();

    public void runMainWindow(){
        frame.setSize(800, 800);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
