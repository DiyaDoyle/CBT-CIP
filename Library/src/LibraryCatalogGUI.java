import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

public class LibraryCatalogGUI extends JFrame implements ActionListener {

    private List<Book> books;
    private final String DATA_FILE = "library.txt";

    // GUI Components
    private JTextField titleField, authorField, isbnField, searchField;
    private JTextArea displayArea; // Declared here
    private JButton addButton, searchButton, listButton, deleteButton;
    private JComboBox<String> searchCategory;

    public LibraryCatalogGUI() {
        super("Library Catalog System");
        books = new ArrayList<>();
        
        // --- FIX STARTS HERE ---
        // Initialize displayArea and other GUI components FIRST
        // before calling any method that might interact with them.

        // Set up the main frame
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create panels for organization
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel displayPanel = new JPanel(new BorderLayout());

        // Input Fields (initialize before use)
        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField(20);
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Author:"));
        authorField = new JTextField(20);
        inputPanel.add(authorField);

        inputPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField(20);
        inputPanel.add(isbnField);

        // Buttons (initialize before use)
        addButton = new JButton("Add Book");
        searchButton = new JButton("Search Books");
        listButton = new JButton("List All Books");
        deleteButton = new JButton("Delete Book");

        addButton.addActionListener(this);
        searchButton.addActionListener(this);
        listButton.addActionListener(this);
        deleteButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(listButton);
        buttonPanel.add(deleteButton);

        // Search Panel (initialize before use)
        searchPanel.add(new JLabel("Search By:"));
        String[] searchOptions = {"Title", "Author", "ISBN"};
        searchCategory = new JComboBox<>(searchOptions);
        searchPanel.add(searchCategory);
        
        searchPanel.add(new JLabel("Keyword:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        // Display Area (initialize before use)
        displayArea = new JTextArea(15, 60); // <--- THIS IS THE CRUCIAL LINE TO BE EXECUTED EARLIER
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to the frame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.add(inputPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(displayPanel, BorderLayout.CENTER);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Now that all GUI components are initialized, it's safe to load books
        // because loadBooksFromFile() calls listAllBooks(), which uses displayArea.
        loadBooksFromFile(); // Call this AFTER displayArea is initialized

        // Add a window listener to save data on close
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveBooksToFile();
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addBook();
        } else if (e.getSource() == searchButton) {
            searchBook();
        } else if (e.getSource() == listButton) {
            listAllBooks();
        } else if (e.getSource() == deleteButton) {
            deleteBook();
        }
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields (Title, Author, ISBN) must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isbnExists = books.stream()
                                 .anyMatch(book -> book.getIsbn().equalsIgnoreCase(isbn));
        if (isbnExists) {
            JOptionPane.showMessageDialog(this, "A book with this ISBN already exists.", "Duplicate ISBN", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book newBook = new Book(title, author, isbn);
        books.add(newBook);
        JOptionPane.showMessageDialog(this, "Book '" + title + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearInputFields();
        listAllBooks();
    }

    private void searchBook() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String category = (String) searchCategory.getSelectedItem();
        List<Book> searchResults = new ArrayList<>();

        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search keyword.", "Search Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (category) {
            case "Title":
                searchResults = books.stream()
                                    .filter(book -> book.getTitle().toLowerCase().contains(searchTerm))
                                    .collect(Collectors.toList());
                break;
            case "Author":
                searchResults = books.stream()
                                    .filter(book -> book.getAuthor().toLowerCase().contains(searchTerm))
                                    .collect(Collectors.toList());
                break;
            case "ISBN":
                searchResults = books.stream()
                                    .filter(book -> book.getIsbn().toLowerCase().contains(searchTerm))
                                    .collect(Collectors.toList());
                break;
        }

        displayArea.setText("");
        if (searchResults.isEmpty()) {
            displayArea.append("No books found matching your criteria.");
        } else {
            displayArea.append("--- Search Results ---\n");
            searchResults.stream()
                         .sorted((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()))
                         .forEach(book -> displayArea.append(book.toString() + "\n"));
        }
    }

    private void listAllBooks() {
        displayArea.setText("");
        if (books.isEmpty()) {
            displayArea.append("The catalog is empty. No books to display.");
        } else {
            displayArea.append("--- All Books in Catalog ---\n");
            books.stream()
                 .sorted((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()))
                 .forEach(book -> displayArea.append(book.toString() + "\n"));
        }
    }

    private void deleteBook() {
        String isbnInput = JOptionPane.showInputDialog(this, "Enter the ISBN of the book to delete:", "Delete Book", JOptionPane.PLAIN_MESSAGE);

        if (isbnInput == null || isbnInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Deletion cancelled or ISBN was empty.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        final String isbnToDelete = isbnInput.trim(); 

        boolean removed = books.removeIf(book -> book.getIsbn().equalsIgnoreCase(isbnToDelete));

        if (removed) {
            JOptionPane.showMessageDialog(this, "Book with ISBN '" + isbnToDelete + "' deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            listAllBooks();
        } else {
            JOptionPane.showMessageDialog(this, "No book found with ISBN '" + isbnToDelete + "'.", "Not Found", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearInputFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        searchField.setText("");
        searchCategory.setSelectedIndex(0);
    }

    private void loadBooksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Book book = Book.fromFileString(line);
                if (book != null) {
                    books.add(book);
                }
            }
            System.out.println("Books loaded from " + DATA_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("No existing library file found. Starting with an empty catalog.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
        listAllBooks(); // This call is now safe because displayArea is initialized.
    }

    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Book book : books) {
                writer.write(book.toFileString());
                writer.newLine();
            }
            System.out.println("Books saved to " + DATA_FILE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving books: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryCatalogGUI::new);
    }
}