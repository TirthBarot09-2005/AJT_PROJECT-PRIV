package ui;

import services.IssueService;

import javax.swing.*;
import java.awt.*;

public class IssueBookPanel extends JPanel {
    private JTextField bookIdField, studentIdField;
    private IssueService issueService;

    public IssueBookPanel() {
        issueService = new IssueService();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        JLabel title = new JLabel("Issue Book", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Book ID:"), gbc);
        bookIdField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(bookIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Student ID:"), gbc);
        studentIdField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(studentIdField, gbc);

        JButton issueBtn = new JButton("Issue Book");
        issueBtn.setBackground(new Color(46, 204, 113));
        issueBtn.setForeground(Color.WHITE);
        issueBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(issueBtn, gbc);

        issueBtn.addActionListener(e -> issueBook());

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(formPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void issueBook() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText().trim());
            int studentId = Integer.parseInt(studentIdField.getText().trim());

            if (issueService.issueBook(bookId, studentId)) {
                JOptionPane.showMessageDialog(this, "Book Issued Successfully!");
                bookIdField.setText("");
                studentIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to issue book. Check if Book ID/Student ID are valid and Book is in stock.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid IDs.");
        }
    }
}
