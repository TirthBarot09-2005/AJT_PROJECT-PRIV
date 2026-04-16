package ui;

import models.IssuedBook;
import services.IssueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReturnBookPanel extends JPanel {
    private JTextField bookIdField, studentIdField;
    private JTable pendingTable;
    private DefaultTableModel tableModel;
    private IssueService issueService;

    public ReturnBookPanel() {
        issueService = new IssueService();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // Form Panel (North)
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Return Book"));

        formPanel.add(new JLabel("Book ID:"));
        bookIdField = new JTextField(10);
        formPanel.add(bookIdField);

        formPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField(10);
        formPanel.add(studentIdField);

        JButton returnBtn = new JButton("Return Book");
        returnBtn.setBackground(new Color(231, 76, 60));
        returnBtn.setForeground(Color.WHITE);
        formPanel.add(returnBtn);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel (Center) - Shows books currently issued
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Pending Returns"));

        String[] cols = {"Issue ID", "Book ID", "Student ID", "Issue Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        pendingTable = new JTable(tableModel);
        pendingTable.getSelectionModel().addListSelectionListener(e -> {
            int row = pendingTable.getSelectedRow();
            if(row >= 0) {
                bookIdField.setText(tableModel.getValueAt(row, 1).toString());
                studentIdField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });

        tablePanel.add(new JScrollPane(pendingTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        returnBtn.addActionListener(e -> returnBook());
    }

    private void returnBook() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText().trim());
            int studentId = Integer.parseInt(studentIdField.getText().trim());

            if (issueService.returnBook(bookId, studentId)) {
                JOptionPane.showMessageDialog(this, "Book Returned Successfully!");
                bookIdField.setText("");
                studentIdField.setText("");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return. No matching pending record found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid IDs.");
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<IssuedBook> pending = issueService.getPendingIssues();
        for (IssuedBook ib : pending) {
            tableModel.addRow(new Object[]{ib.getIssueId(), ib.getBookId(), ib.getStudentId(), ib.getIssueDate()});
        }
    }
}
