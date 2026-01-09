package com.airline.gui;

import javax.swing.*;
import java.awt.*;
import com.airline.services.AuthService;
import com.airline.models.User;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthService authService;

    public LoginScreen() {
        authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Airline Reservation System - Login");
        setSize(1200, 850); // Kayıt butonu için boyutu biraz artırdık
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- ANA PANEL ---
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        mainPanel.setBackground(new Color(240, 248, 255));

        // --- KUZEY: BAŞLIK ---
        JLabel titleLabel = new JLabel("Airline Reservation System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(25, 25, 112));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // --- MERKEZ: FORM (Giriş Alanları) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 32));
        usernameField.setPreferredSize(new Dimension(450, 60));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 32));
        passwordField.setPreferredSize(new Dimension(450, 60));
        formPanel.add(passwordField, gbc);

        // Login Butonu
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(40, 15, 15, 15);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 36));
        loginButton.setPreferredSize(new Dimension(300, 70));
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(Color.BLACK);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- GÜNEY: AKSİYONLAR VE BİLGİ ---
        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.setOpaque(false);

        // Register Kısmı
        JPanel registerPanel = new JPanel();
        registerPanel.setOpaque(false);
        JButton registerBtn = new JButton("Don't have an account? Register here");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 22));
        registerBtn.setForeground(new Color(0, 51, 153));
        registerBtn.setContentAreaFilled(false); // Sadece yazı gibi görünsün
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> showRegisterDialog());
        registerPanel.add(registerBtn);

        // Grup Bilgisi
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JLabel infoLabel = new JLabel("<html><center>Made by Group 19<br>K. B. ARI - S. CABUK</center></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);

        southContainer.add(registerPanel, BorderLayout.NORTH);
        southContainer.add(infoPanel, BorderLayout.SOUTH);

        mainPanel.add(southContainer, BorderLayout.SOUTH);

        add(mainPanel);

        // Olay Dinleyiciler
        loginButton.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password!", "Login Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = authService.login(username, password);

        if (success) {
            User currentUser = AuthService.getCurrentUser();
            showMessage("Welcome, " + currentUser.getUsername() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);

            if (currentUser.getRole().equalsIgnoreCase("ADMIN")) new AdminGUI();
            else if (currentUser.getRole().equalsIgnoreCase("PASSENGER")) new PassengerGUI();
            else if (currentUser.getRole().equalsIgnoreCase("STAFF")) new StaffGUI();

            dispose();
        } else {
            showMessage("Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }

    private void showRegisterDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        panel.add(new JLabel("New Username:"));
        panel.add(userField);
        panel.add(new JLabel("New Password:"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Passenger Registration",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newUser = userField.getText().trim();
            String newPass = new String(passField.getPassword());

            if (newUser.isEmpty() || newPass.isEmpty()) {
                showMessage("Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                AuthService.register(newUser, newPass, "PASSENGER");
                showMessage("Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showMessage("Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 24));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 22));
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}