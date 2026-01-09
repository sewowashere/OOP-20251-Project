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
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Ana panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(30, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 120, 60, 120));
        mainPanel.setBackground(new Color(240, 248, 255));

        // Başlık
        JLabel titleLabel = new JLabel("Airline Reservation System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form paneli
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label ve field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Kutunun yatayda yayılmasını sağlar
        gbc.weightx = 1.0; // Panelin genişlemesine uyum sağlar
        usernameField = new JTextField(25);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 32));
        usernameField.setPreferredSize(new Dimension(500, 60)); // Minimum 60 yükseklik
        formPanel.add(usernameField, gbc);

        // Password label ve field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 32));
        passwordField.setPreferredSize(new Dimension(500, 60));
        formPanel.add(passwordField, gbc);

        // Login butonu
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(50, 25, 25, 25);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 36));
        loginButton.setPreferredSize(new Dimension(400, 80));
        loginButton.setBackground(new Color(0, 0, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Info paneli
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 248, 255));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel infoLabel = new JLabel("<html><center>Made by Group 19<br><br>K. B. ARI <br><br>S. CABUK</center></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 24));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event listeners
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

        AuthService auth = new AuthService(); // Önce nesneyi oluştur (içindeki userDao başlasın)
        boolean success = auth.login(username, password);

        if (success) {
            User currentUser = AuthService.getCurrentUser();

            showMessage("Welcome, " + currentUser.getUsername() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);

            // Role göre ilgili GUI'yi aç
            if (currentUser.getRole().equalsIgnoreCase("ADMIN")) {
                new AdminGUI();
            } else if (currentUser.getRole().equalsIgnoreCase("PASSENGER")) {
                new PassengerGUI();
            } else if (currentUser.getRole().equalsIgnoreCase("STAFF")) {
                new StaffGUI(); // Artık kendi özel class'ını açıyor!
            }

            // Login ekranını kapat
            dispose();

        } else {
            showMessage("Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }

    private void showMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 24));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 22));

        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public static void main(String[] args) {
        // Linux için HiDPI ayarları
        System.setProperty("sun.java2d.uiScale", "1.0");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Font bigFont = new Font("Arial", Font.PLAIN, 24);
            UIManager.put("Label.font", bigFont);
            UIManager.put("Button.font", bigFont);
            UIManager.put("TextField.font", bigFont);
            UIManager.put("PasswordField.font", bigFont);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}