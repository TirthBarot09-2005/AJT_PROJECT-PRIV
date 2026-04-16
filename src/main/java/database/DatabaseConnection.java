package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Database URL for SQLite. This will automatically create 'library.db' in your project root!
    private static final String DB_URL = "jdbc:sqlite:library.db";

    // Auto-create database and tables if they don't exist
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("SQLite JDBC Driver not found!");
        }
    }

    /**
     * Get a connection to the database
     * @return Connection object
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Automatically sets up the database schema, removing the need for XAMPP.
     */
    private static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Turn on foreign key constraints for SQLite
            stmt.execute("PRAGMA foreign_keys = ON;");

            // 1. Admins Table
            stmt.execute("CREATE TABLE IF NOT EXISTS admins (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "username TEXT NOT NULL UNIQUE, " +
                         "password TEXT NOT NULL)");

            // Insert default admin (I saw you modified the password to 'passwordx', so I placed it here!)
            stmt.execute("INSERT OR IGNORE INTO admins (username, password) VALUES ('admin', 'passwordx')");

            // 2. Books Table
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                         "book_id INTEGER PRIMARY KEY, " +
                         "title TEXT NOT NULL, " +
                         "author TEXT NOT NULL, " +
                         "publisher TEXT, " +
                         "quantity INTEGER NOT NULL DEFAULT 1)");

            // 3. Students Table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                         "student_id INTEGER PRIMARY KEY, " +
                         "name TEXT NOT NULL, " +
                         "department TEXT, " +
                         "contact TEXT)");

            // 4. Issued Books Table
            stmt.execute("CREATE TABLE IF NOT EXISTS issued_books (" +
                         "issue_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "book_id INTEGER NOT NULL, " +
                         "student_id INTEGER NOT NULL, " +
                         "issue_date DATE NOT NULL, " +
                         "return_date DATE, " +
                         "FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE, " +
                         "FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE)");

            // 5. Insert Sample Data
            stmt.execute("INSERT OR IGNORE INTO books VALUES " +
                         "(101, 'The Pragmatic Programmer', 'David Thomas', 'Addison-Wesley', 5), " +
                         "(102, 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 3), " +
                         "(103, 'Head First Java', 'Kathy Sierra', 'O Reilly', 6)");

            stmt.execute("INSERT OR IGNORE INTO students VALUES " +
                         "(1001, 'Alice Smith', 'Computer Science', '555-0101'), " +
                         "(1002, 'Bob Johnson', 'Information Tech', '555-0102')");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to auto-initialize the database.");
        }
    }
}
