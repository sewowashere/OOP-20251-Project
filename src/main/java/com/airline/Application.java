package com.airline;

import javax.swing.*;
import com.airline.gui.LoginScreen;

public class Application {
    public static void main(String[] args) {
        // Ensure Swing components run on EDT
        SwingUtilities.invokeLater(() -> {
            // Set system look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Launch login screen
            new LoginScreen();
        });
    }
}






