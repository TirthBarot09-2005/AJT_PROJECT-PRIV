package ui;

import services.AuthService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private AuthService authService;

    public LoginFrame() {
        authService = new AuthService();
        setTitle("Library Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        userField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(userField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        passField = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(passField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 120, 215)); // Accent color
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        // Action Listener for Login
        loginButton.addActionListener(this::handleLogin);
        // Enable enter key login
        getRootPane().setDefaultButton(loginButton);

        add(mainPanel);
    }

    private void handleLogin(ActionEvent e) {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authService.login(username, password)) {
            // Open Dashboard
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.setVisible(true);
            this.dispose(); // Close login frame
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
