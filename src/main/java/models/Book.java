package models;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String publisher;
    private int quantity;

    public Book() {}

    public Book(int bookId, String title, String author, String publisher, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.quantity = quantity;
    }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    @Override
    public String toString() {
        return title; // Useful for ComboBoxes if needed
    }
}
