package services;

import database.DatabaseConnection;
import models.IssuedBook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueService {

    /**
     * Issues a book to a student if available.
     */
    public boolean issueBook(int bookId, int studentId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check if the book is available
            String checkQuery = "SELECT quantity FROM books WHERE book_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next() || rs.getInt("quantity") <= 0) {
                    return false; // Book not found or out of stock
                }
            }

            // 2. Check if student exists
            String studentCheck = "SELECT student_id FROM students WHERE student_id = ?";
            try (PreparedStatement studentStmt = conn.prepareStatement(studentCheck)) {
                studentStmt.setInt(1, studentId);
                ResultSet rs = studentStmt.executeQuery();
                if (!rs.next()) {
                    return false; // Student not found
                }
            }

            // 3. Check if this student already has this book issued and not returned
            String issueCheck = "SELECT issue_id FROM issued_books WHERE book_id = ? AND student_id = ? AND return_date IS NULL";
            try (PreparedStatement checkIssued = conn.prepareStatement(issueCheck)) {
                checkIssued.setInt(1, bookId);
                checkIssued.setInt(2, studentId);
                ResultSet rs = checkIssued.executeQuery();
                if (rs.next()) {
                    return false; // Already issued the same book
                }
            }

            // 4. Update quantity
            String updateQuery = "UPDATE books SET quantity = quantity - 1 WHERE book_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }

            // 5. Insert to issued_books
            String insertQuery = "INSERT INTO issued_books (book_id, student_id, issue_date) VALUES (?, ?, CURRENT_DATE)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, bookId);
                insertStmt.setInt(2, studentId);
                insertStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns a specified book.
     */
    public boolean returnBook(int bookId, int studentId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Find the issue record
            String findRecord = "SELECT issue_id FROM issued_books WHERE book_id = ? AND student_id = ? AND return_date IS NULL";
            int issueId = -1;
            try (PreparedStatement findStmt = conn.prepareStatement(findRecord)) {
                findStmt.setInt(1, bookId);
                findStmt.setInt(2, studentId);
                ResultSet rs = findStmt.executeQuery();
                if (rs.next()) {
                    issueId = rs.getInt("issue_id");
                } else {
                    return false; // No record found of this issue that hasn't been returned
                }
            }

            // 2. Update issue record
            String updateIssue = "UPDATE issued_books SET return_date = CURRENT_DATE WHERE issue_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateIssue)) {
                updateStmt.setInt(1, issueId);
                updateStmt.executeUpdate();
            }

            // 3. Update book quantity
            String updateBook = "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateBook)) {
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<IssuedBook> getPendingIssues() {
        List<IssuedBook> issues = new ArrayList<>();
        String query = "SELECT * FROM issued_books WHERE return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                issues.add(new IssuedBook(
                    rs.getInt("issue_id"),
                    rs.getInt("book_id"),
                    rs.getInt("student_id"),
                    rs.getDate("issue_date"),
                    rs.getDate("return_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }
}
