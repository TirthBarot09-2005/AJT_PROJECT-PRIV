package services;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    /**
     * Validates admin login credentials.
     * @param username
     * @param password
     * @return true if valid, false otherwise
     */
    public boolean login(String username, String password) {
        String query = "SELECT id FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            return rs.next(); // True if a matching record is found
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // A helper method for dashboard statistics
    public int[] getDashboardStats() {
        int[] stats = new int[3]; // [0]=Total Books, [1]=Total Students, [2]=Issued Books
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Total Books
            try (PreparedStatement pstBooks = conn.prepareStatement("SELECT SUM(quantity) FROM books");
                 ResultSet rs1 = pstBooks.executeQuery()) {
                if (rs1.next()) stats[0] = rs1.getInt(1);
            }
            
            // Total Students
            try (PreparedStatement pstStudents = conn.prepareStatement("SELECT COUNT(*) FROM students");
                 ResultSet rs2 = pstStudents.executeQuery()) {
                if (rs2.next()) stats[1] = rs2.getInt(1);
            }
            
            // Distributed Books (Issued)
            try (PreparedStatement pstIssued = conn.prepareStatement("SELECT COUNT(*) FROM issued_books WHERE return_date IS NULL");
                 ResultSet rs3 = pstIssued.executeQuery()) {
                if (rs3.next()) stats[2] = rs3.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}
