package TCI;

public class Book {

    private String genre;
    private String format;
    private String year;
    private String author;
    private String publisher;
    private String isbn;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getYear() {
        return year;
    }
    public void setYear(String year)
    {
        this.year=year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Book()
    {

    }
    public Book(String genre, String format, String year, String auth, String pub, String isb)
    {
setGenre(genre);
setFormat(format);
setYear(year);
setAuthor(auth);
setPublisher(pub);
setIsbn(isb);
    }
}
