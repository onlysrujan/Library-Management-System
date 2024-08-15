import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Librarian {
    String id;
    String name;
    String password;

    public Librarian(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}

class Book {
    String id;
    String title;
    String author;
    boolean isIssued;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }
}

class LibrarySystem {
    private HashMap<String, Librarian> librarians = new HashMap<>();
    private HashMap<String, Book> books = new HashMap<>();
    private HashMap<String, String> issuedBooks = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    // Admin Functions
    public void addLibrarian() {
        System.out.print("Enter Librarian ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Librarian Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Librarian Password: ");
        String password = scanner.nextLine();

        Librarian librarian = new Librarian(id, name, password);
        librarians.put(id, librarian);
        System.out.println("Librarian added successfully!");
    }

    public void viewLibrarians() {
        System.out.println("List of Librarians:");
        for (Librarian librarian : librarians.values()) {
            System.out.println("ID: " + librarian.id + ", Name: " + librarian.name);
        }
    }

    public void deleteLibrarian() {
        System.out.print("Enter Librarian ID to delete: ");
        String id = scanner.nextLine();
        if (librarians.containsKey(id)) {
            librarians.remove(id);
            System.out.println("Librarian deleted successfully!");
        } else {
            System.out.println("Librarian not found!");
        }
    }

    // Librarian Functions
    public void addBook() {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Book Author: ");
        String author = scanner.nextLine();

        Book book = new Book(id, title, author);
        books.put(id, book);
        System.out.println("Book added successfully!");
    }

    public void viewBooks() {
        System.out.println("List of Books:");
        for (Book book : books.values()) {
            System.out.println("ID: " + book.id + ", Title: " + book.title + ", Author: " + book.author + ", Issued: " + (book.isIssued ? "Yes" : "No"));
        }
    }

    public void issueBook() {
        System.out.print("Enter Book ID to issue: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();

        if (books.containsKey(bookId) && !books.get(bookId).isIssued) {
            books.get(bookId).isIssued = true;
            issuedBooks.put(bookId, memberId);
            System.out.println("Book issued successfully!");
        } else {
            System.out.println("Book not available or already issued!");
        }
    }

    public void viewIssuedBooks() {
        System.out.println("List of Issued Books:");
        for (String bookId : issuedBooks.keySet()) {
            Book book = books.get(bookId);
            System.out.println("Book ID: " + bookId + ", Title: " + book.title + ", Issued to: " + issuedBooks.get(bookId));
        }
    }

    public void returnBook() {
        System.out.print("Enter Book ID to return: ");
        String bookId = scanner.nextLine();

        if (issuedBooks.containsKey(bookId)) {
            books.get(bookId).isIssued = false;
            issuedBooks.remove(bookId);
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Book not found in issued list!");
        }
    }
}

public class LibraryManagementSystem {
    private static LibrarySystem librarySystem = new LibrarySystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Admin Login");
            System.out.println("2. Librarian Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    adminMenu();
                    break;
                case 2:
                    librarianMenu();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Librarian");
            System.out.println("2. View Librarians");
            System.out.println("3. Delete Librarian");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    librarySystem.addLibrarian();
                    break;
                case 2:
                    librarySystem.viewLibrarians();
                    break;
                case 3:
                    librarySystem.deleteLibrarian();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void librarianMenu() {
        while (true) {
            System.out.println("\nLibrarian Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Issue Book");
            System.out.println("4. View Issued Books");
            System.out.println("5. Return Book");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    librarySystem.addBook();
                    break;
                case 2:
                    librarySystem.viewBooks();
                    break;
                case 3:
                    librarySystem.issueBook();
                    break;
                case 4:
                    librarySystem.viewIssuedBooks();
                    break;
                case 5:
                    librarySystem.returnBook();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
