package org.example.Windows;

import org.example.UsbConfig.UsbMonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewAccountWindow {

    private MainWindow mainWindow;

    public NewAccountWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    JFrame frame = new JFrame();

    int rows = 16;
    int cols = 4;

    GridLayout gridLayout = new GridLayout(rows, cols);

    JPanel testPanel = new JPanel(gridLayout);

    JButton button = new JButton("GO BACK");

    JTextField textField = new JTextField();

    JLabel TEST = new JLabel();

    int width = 800, height = 800;

    public void run(){

        button = new JButton("TEST");

        placeholderLayout();

        //testPanel.add(button);

        actionButtonGoBack();

        frame.add(testPanel);

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void placeholderLayout() {
        int totalCells = rows * cols;
        Component[] components = new Component[totalCells];

        for (int i = 0; i < totalCells; i++) {
            components[i] = new JLabel("TEST");
        }

        // [Location on components]
        // [4 * 4 + 1 is the 9 cell]
        components[4 * 4 + 1] = textField;
        components[2 * 2 + 1] = TEST;
        components[totalCells - 1] = button;

        for (Component comp : components) {
            testPanel.add(comp);
        }
    }

    public void actionButtonGoBack(){

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //frame.dispose();
                frame.setVisible(false);
                mainWindow.frame.setVisible(true);

                mainWindow.runMainWindow();
            }
        });
    }
}
