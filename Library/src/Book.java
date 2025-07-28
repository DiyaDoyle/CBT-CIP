import java.io.Serializable;

class Book implements Serializable {
    private String title;
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + isbn;
    }

    public String toFileString() {
        return title + "|" + author + "|" + isbn;
    }

    public static Book fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 3) {
            return new Book(parts[0], parts[1], parts[2]);
        }
        return null;
    }
}