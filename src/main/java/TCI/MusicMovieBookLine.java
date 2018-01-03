package TCI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MusicMovieBookLine {
    private List<Movie> movies;
    private List<Music> musics;
    private  List<Book> books;
    private String time_elapse;
    private long id;
    public  MusicMovieBookLine()
    {
        movies = new ArrayList<>();
        musics = new ArrayList<>();
        books  = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public String  getTime_elapse() {
        return time_elapse;
    }

    public void setTime_elapse(long start_time_elapse, long end_time_elapse) {
        long diff = end_time_elapse-start_time_elapse;
        long sec = TimeUnit.NANOSECONDS.toMillis(diff);
        this.time_elapse= diff + " ns";

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
