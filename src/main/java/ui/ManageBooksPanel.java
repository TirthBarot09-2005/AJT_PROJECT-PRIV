package ui;

import models.Book;
import services.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageBooksPanel extends JPanel {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField idField, titleField, authorField, publisherField, qtyField, searchField;
    private BookService bookService;

    public ManageBooksPanel() {
        bookService = new BookService();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        // Form Panel (Left side)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        formPanel.setPreferredSize(new Dimension(300, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        String[] labels = {"Book ID", "Title", "Author", "Publisher", "Quantity"};
        JTextField[] fields = new JTextField[5];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;
            formPanel.add(new JLabel(labels[i] + ":"), gbc);
            fields[i] = new JTextField(15);
            gbc.gridy = i * 2 + 1;
            formPanel.add(fields[i], gbc);
        }
        
        idField = fields[0]; titleField = fields[1]; authorField = fields[2];
        publisherField = fields[3]; qtyField = fields[4];

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton delBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        
        buttonPanel.add(addBtn); buttonPanel.add(updateBtn);
        buttonPanel.add(delBtn); buttonPanel.add(clearBtn);

        gbc.gridy = 10;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.WEST);

        // Table Panel (Center)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search (Title/Author): "));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        tablePanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Title", "Author", "Publisher", "Quantity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        bookTable = new JTable(tableModel);
        bookTable.setFillsViewportHeight(true);
        bookTable.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
        
        JScrollPane scrollPane = new JScrollPane(bookTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Action Listeners
        addBtn.addActionListener(e -> addBook());
        updateBtn.addActionListener(e -> updateBook());
        delBtn.addActionListener(e -> deleteBook());
        clearBtn.addActionListener(e -> clearForm());
        searchBtn.addActionListener(e -> searchBooks());
    }

    private void fillFormFromTable() {
        int row = bookTable.getSelectedRow();
        if (row >= 0) {
            idField.setText(tableModel.getValueAt(row, 0).toString());
            titleField.setText(tableModel.getValueAt(row, 1).toString());
            authorField.setText(tableModel.getValueAt(row, 2).toString());
            publisherField.setText(tableModel.getValueAt(row, 3).toString());
            qtyField.setText(tableModel.getValueAt(row, 4).toString());
            idField.setEditable(false);
        }
    }

    private void clearForm() {
        idField.setText(""); titleField.setText(""); authorField.setText("");
        publisherField.setText(""); qtyField.setText("");
        idField.setEditable(true);
        bookTable.clearSelection();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Book> books = bookService.getAllBooks();
        for (Book b : books) {
            tableModel.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getAuthor(), b.getPublisher(), b.getQuantity()});
        }
    }

    private void searchBooks() {
        tableModel.setRowCount(0);
        List<Book> books = bookService.searchBooks(searchField.getText().trim());
        for (Book b : books) {
            tableModel.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getAuthor(), b.getPublisher(), b.getQuantity()});
        }
    }

    private Book getBookFromForm() {
        try {
            return new Book(
                Integer.parseInt(idField.getText().trim()),
                titleField.getText().trim(),
                authorField.getText().trim(),
                publisherField.getText().trim(),
                Integer.parseInt(qtyField.getText().trim())
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID and Quantity must be valid integers.");
            return null;
        }
    }

    private void addBook() {
        Book b = getBookFromForm();
        if (b != null) {
            if (bookService.addBook(b)) {
                JOptionPane.showMessageDialog(this, "Book Added Successfully!");
                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding book (ID might already exist).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateBook() {
        Book b = getBookFromForm();
        if (b != null) {
            if (bookService.updateBook(b)) {
                JOptionPane.showMessageDialog(this, "Book Updated Successfully!");
                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteBook() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (bookService.deleteBook(id)) {
                    JOptionPane.showMessageDialog(this, "Book Deleted!");
                    clearForm();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting book.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please select a valid book to delete.");
        }
    }
}
