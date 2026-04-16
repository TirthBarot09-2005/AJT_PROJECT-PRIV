package ui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    
    // Cache the panels
    private DashboardPanel dashboardPanel;
    private ManageBooksPanel manageBooksPanel;
    private ManageStudentsPanel manageStudentsPanel;
    private IssueBookPanel issueBookPanel;
    private ReturnBookPanel returnBookPanel;

    public DashboardFrame() {
        setTitle("Library Management System - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Top Header bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        JLabel headerTitle = new JLabel(" Library Management System Workspace");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerTitle.setForeground(Color.WHITE);
        headerPanel.add(headerTitle, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Left Sidebar setup
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(new Color(44, 62, 80));

        // Create Panel Container for CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // Initialize panels
        dashboardPanel = new DashboardPanel();
        manageBooksPanel = new ManageBooksPanel();
        manageStudentsPanel = new ManageStudentsPanel();
        issueBookPanel = new IssueBookPanel();
        returnBookPanel = new ReturnBookPanel();

        // Add panels to card layout
        mainContentPanel.add(dashboardPanel, "Dashboard");
        mainContentPanel.add(manageBooksPanel, "Manage Books");
        mainContentPanel.add(manageStudentsPanel, "Manage Students");
        mainContentPanel.add(issueBookPanel, "Issue Book");
        mainContentPanel.add(returnBookPanel, "Return Book");

        // Add Sidebar Buttons
        String[] menuItems = {
            "Dashboard", "Manage Books", "Manage Students", "Issue Book", "Return Book", "Logout"
        };
        
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Padding

        for (String item : menuItems) {
            JButton btn = createSidebarButton(item);
            btn.addActionListener(e -> {
                if (item.equals("Logout")) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        new LoginFrame().setVisible(true);
                        this.dispose();
                    }
                } else {
                    cardLayout.show(mainContentPanel, item);
                    if (item.equals("Dashboard")) {
                        dashboardPanel.refreshStats();
                    } else if (item.equals("Manage Books")) {
                        manageBooksPanel.refreshTable();
                    } else if (item.equals("Manage Students")) {
                        manageStudentsPanel.refreshTable();
                    }
                }
            });
            sidebarPanel.add(btn);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Add components to JFrame
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 91, 118));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });
        
        return button;
    }
}
