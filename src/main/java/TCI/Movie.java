package TCI;

public class Movie {
    private String genre;
    private String format;
    private String year;
    private String director;
    private String writers;
    private String stars;

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

    public String getDirector() {
        return director;
    }

    public void setDirector(String author) {
        this.director = author;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }



    public Movie()
    {}
    public Movie(String genre, String format, String year, String dir, String wri, String sta)
    {
        setGenre(genre);
        setFormat(format);
        setYear(year);
        setDirector(dir);
        setWriters(wri);
        setStars(sta);
    }
}
