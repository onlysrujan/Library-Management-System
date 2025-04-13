package lms;
import java.sql.*;
import java.util.*;
public class LibraryManagementSystem {

	private static final String URL = "jdbc:oracle:thin:@//localhost:1522/xepdb1"; // Update with your DB details
    private static final String USER = "system"; // Update username
    private static final String PASSWORD = "system"; // Update password

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Add Librarian");
            System.out.println("2. View Librarians");
            System.out.println("3. Add Book");
            System.out.println("4. View Books");
            System.out.println("5. Issue Book");
            System.out.println("6. Return Book");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addLibrarian();
                case 2 -> viewLibrarians();
                case 3 -> addBook();
                case 4 -> viewBooks();
                case 5 -> issueBook();
                case 6 -> returnBook();
                case 7 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addLibrarian() {
        System.out.print("Enter Librarian ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Librarian Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        String sql = "INSERT INTO Librarians (id, name, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, password);
            stmt.executeUpdate();
            System.out.println("Librarian added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewLibrarians() {
        String sql = "SELECT * FROM Librarians";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("\nLibrarians:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("id") + ", Name: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addBook() {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        String sql = "INSERT INTO Books (id, title, author) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewBooks() {
        String sql = "SELECT * FROM Books";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("\nBooks:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("id") + ", Title: " + rs.getString("title") +
                        ", Author: " + rs.getString("author") + ", Issued: " + (rs.getInt("isIssued") == 1 ? "Yes" : "No"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void issueBook() {
        System.out.print("Enter Book ID to issue: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();

        String checkSql = "SELECT isIssued FROM Books WHERE id = ?";
        String issueSql = "INSERT INTO IssuedBooks (book_id, member_id) VALUES (?, ?)";
        String updateSql = "UPDATE Books SET isIssued = 1 WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("isIssued") == 0) {
                try (PreparedStatement issueStmt = conn.prepareStatement(issueSql);
                     PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    issueStmt.setString(1, bookId);
                    issueStmt.setString(2, memberId);
                    issueStmt.executeUpdate();
                    updateStmt.setString(1, bookId);
                    updateStmt.executeUpdate();
                    System.out.println("Book issued successfully!");
                }
            } else {
                System.out.println("Book is not available or already issued!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void returnBook() {
        System.out.print("Enter Book ID to return: ");
        String bookId = scanner.nextLine();

        String checkSql = "SELECT * FROM IssuedBooks WHERE book_id = ?";
        String deleteSql = "DELETE FROM IssuedBooks WHERE book_id = ?";
        String updateSql = "UPDATE Books SET isIssued = 0 WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                     PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    deleteStmt.setString(1, bookId);
                    deleteStmt.executeUpdate();
                    updateStmt.setString(1, bookId);
                    updateStmt.executeUpdate();
                    System.out.println("Book returned successfully!");
                }
            } else {
                System.out.println("Book is not issued!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
