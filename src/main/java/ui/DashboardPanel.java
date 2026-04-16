package ui;

import services.AuthService;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private JLabel totalBooksVal, totalStudentsVal, issuedBooksVal;
    private AuthService authService;

    public DashboardPanel() {
        authService = new AuthService();
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));
        initComponents();
        refreshStats();
    }

    private void initComponents() {
        JLabel title = new JLabel("Welcome to the Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        cardsPanel.setBackground(new Color(236, 240, 241));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 100, 50));

        // Create cards
        JPanel booksCard = createStatCard("Total Books", new Color(52, 152, 219));
        totalBooksVal = (JLabel) booksCard.getClientProperty("valueLabel");
        
        JPanel studentsCard = createStatCard("Total Students", new Color(46, 204, 113));
        totalStudentsVal = (JLabel) studentsCard.getClientProperty("valueLabel");
        
        JPanel issuedCard = createStatCard("Issued Books", new Color(231, 76, 60));
        issuedBooksVal = (JLabel) issuedCard.getClientProperty("valueLabel");

        cardsPanel.add(booksCard);
        cardsPanel.add(studentsCard);
        cardsPanel.add(issuedCard);

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel("0", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        // Store label for updating later
        panel.putClientProperty("valueLabel", valueLabel);

        return panel;
    }

    public void refreshStats() {
        int[] stats = authService.getDashboardStats();
        totalBooksVal.setText(String.valueOf(stats[0]));
        totalStudentsVal.setText(String.valueOf(stats[1]));
        issuedBooksVal.setText(String.valueOf(stats[2]));
    }
}
