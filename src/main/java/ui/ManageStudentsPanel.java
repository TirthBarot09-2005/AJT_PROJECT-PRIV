package ui;

import models.Student;
import services.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageStudentsPanel extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, deptField, contactField, searchField;
    private StudentService studentService;

    public ManageStudentsPanel() {
        studentService = new StudentService();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        formPanel.setPreferredSize(new Dimension(300, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        String[] labels = {"Student ID", "Name", "Department", "Contact"};
        JTextField[] fields = new JTextField[4];
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;
            formPanel.add(new JLabel(labels[i] + ":"), gbc);
            fields[i] = new JTextField(15);
            gbc.gridy = i * 2 + 1;
            formPanel.add(fields[i], gbc);
        }
        
        idField = fields[0]; nameField = fields[1]; deptField = fields[2]; contactField = fields[3];

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

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search (ID/Name): "));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        tablePanel.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Department", "Contact"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(tableModel);
        studentTable.setFillsViewportHeight(true);
        studentTable.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
        
        tablePanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Action Listeners
        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        delBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearForm());
        searchBtn.addActionListener(e -> searchStudents());
    }

    private void fillFormFromTable() {
        int row = studentTable.getSelectedRow();
        if (row >= 0) {
            idField.setText(tableModel.getValueAt(row, 0).toString());
            nameField.setText(tableModel.getValueAt(row, 1).toString());
            deptField.setText(tableModel.getValueAt(row, 2).toString());
            contactField.setText(tableModel.getValueAt(row, 3).toString());
            idField.setEditable(false);
        }
    }

    private void clearForm() {
        idField.setText(""); nameField.setText(""); deptField.setText(""); contactField.setText("");
        idField.setEditable(true);
        studentTable.clearSelection();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = studentService.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getDepartment(), s.getContact()});
        }
    }

    private void searchStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentService.searchStudents(searchField.getText().trim());
        for (Student s : students) {
            tableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getDepartment(), s.getContact()});
        }
    }

    private Student getStudentFromForm() {
        try {
            return new Student(
                Integer.parseInt(idField.getText().trim()),
                nameField.getText().trim(),
                deptField.getText().trim(),
                contactField.getText().trim()
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID must be a valid integer.");
            return null;
        }
    }

    private void addStudent() {
        Student s = getStudentFromForm();
        if (s != null) {
            if (studentService.addStudent(s)) {
                JOptionPane.showMessageDialog(this, "Student Added!");
                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateStudent() {
        Student s = getStudentFromForm();
        if (s != null) {
            if (studentService.updateStudent(s)) {
                JOptionPane.showMessageDialog(this, "Student Updated!");
                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating student.");
            }
        }
    }

    private void deleteStudent() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this student?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (studentService.deleteStudent(id)) {
                    JOptionPane.showMessageDialog(this, "Student Deleted!");
                    clearForm();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting.");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Select a valid student.");
        }
    }
}
