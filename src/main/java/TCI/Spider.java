package TCI;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class Spider {

    public int numberofvisitedpages = 0;
    public int Currentdepth = 0;
    public int PreviousDepth = 0;
    public List<String> Links;
    CrawlInformation crawl;
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private long start_Time;
    private long end_Time;
    private final AtomicLong counter = new AtomicLong();

    public Spider(CrawlInformation c) {
        Links = new ArrayList<>();
        crawl =c;
    }
    public Spider() {
        Links = new ArrayList<>();
        crawl =null;
    }

    public List<String> getLinks() {
        return Links;
    }

    public void setLinks(List<String> links) {
        Links = links;
    }

    public void GetAllLinks(String url) throws IOException {
        try {

            List<String> links = new ArrayList<>();
            start_Time = System.nanoTime();
            Connection connection = Jsoup.connect(url);
            Document htmlDocument = connection.get();
            Elements linksOnPage = htmlDocument.select("a[href]");
            for (Element link : linksOnPage) {
                links.add(link.absUrl("href"));
            }
            setLinks(links);
        } catch (UnsupportedMimeTypeException u) {
            throw new IllegalArgumentException(u.getMessage());

        }
    }

    public MusicMovieBookLine GetAll() throws IOException {
        MusicMovieBookLine bookLine = new MusicMovieBookLine();
        List<Book> books = new ArrayList<>();
        List<Movie> movies = new ArrayList<>();
        List<Music> musics = new ArrayList<>();
        List<String> subLinks = new ArrayList<>();
        int pageTovisit = 0;
        int visitedpage = 0;
        for (String link : Links) {
            if (link.endsWith("?cat=books")) {

                Connection connection = Jsoup.connect(link);
                Document htmlDocument = connection.get();
                Elements linksOnPage = htmlDocument.select("a[href]");
                pageTovisit = linksOnPage.size();
                visitedpage = 6;
                Currentdepth++;
                for (Element sub : linksOnPage) {
                    subLinks.add(sub.absUrl("href"));
                }
                while (visitedpage < pageTovisit) {

                    Book book = GetBook(subLinks.get(visitedpage));
                    if(visitedpage==6){ Currentdepth++;}
                    numberofvisitedpages = numberofvisitedpages + 1 ;
                    if (book != null) {
                        books.add(book);
                    }

                    visitedpage++;

                }
                PreviousDepth=Currentdepth;
                Currentdepth =0;
                subLinks.clear();
            }

            if (link.endsWith("?cat=movies")) {
                Connection connection = Jsoup.connect(link);
                Document htmlDocument = connection.get();
                Elements linksOnPage = htmlDocument.select("a[href]");
                pageTovisit = linksOnPage.size();
                visitedpage = 6;
                Currentdepth++;
                for (Element sub : linksOnPage) {
                    subLinks.add(sub.absUrl("href"));
                }
                while (visitedpage < pageTovisit) {
                    Movie movie = GetMovie(subLinks.get(visitedpage));
                    numberofvisitedpages ++;
                    if (movie != null) {
                        movies.add(movie);
                    }
                    if(visitedpage==6){ Currentdepth++;}
                    visitedpage++;

                }
                if(Currentdepth> PreviousDepth)
                {
                    PreviousDepth = Currentdepth;
                }
                Currentdepth=0;
                subLinks.clear();

            }

            if (link.endsWith("?cat=music")) {
                Connection connection = Jsoup.connect(link);
                Document htmlDocument = connection.get();
                Elements linksOnPage = htmlDocument.select("a[href]");
                pageTovisit = linksOnPage.size();
                visitedpage = 6;
                Currentdepth++;
                for (Element sub : linksOnPage) {
                    subLinks.add(sub.absUrl("href"));
                }
                while (visitedpage < pageTovisit) {
                    Music music = GetMusic(subLinks.get(visitedpage));
                    numberofvisitedpages++;
                    if (music != null) {
                        musics.add(music);
                    }
                    if(visitedpage==6){ Currentdepth++;}
                    visitedpage++;

                }
                if(Currentdepth > PreviousDepth)
                {
                    PreviousDepth = Currentdepth;
                }
                Currentdepth=0;

                subLinks.clear();
            }

        }
        numberofvisitedpages++;
        bookLine.setBooks(books);
        bookLine.setMovies(movies);
        bookLine.setMusics(musics);
        end_Time = System.nanoTime();
        bookLine.setTime_elapse(start_Time, end_Time);
        bookLine.setId(counter.incrementAndGet());
        System.out.println(PreviousDepth);
        if(crawl!=null)
        {crawl.SetDepth(PreviousDepth);
        crawl.SetExplorer(numberofvisitedpages);
        crawl.setTime_elapse(start_Time,end_Time);}
        numberofvisitedpages=0;
        PreviousDepth=0;

        return bookLine;
    }

    public Movie GetMovie(String url) throws IOException {
        if (url.contains("?id=")) {
            Movie movie = new Movie();
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            Elements table = htmlDocument.select("table");
            Elements rows = table.select("tr");
            for (int i = 0; i < rows.size(); i++) {
                Element th = rows.get(i).child(0);
                Element tr = rows.get(i).child(1);
                if (th.text().equals("Category") && !tr.text().equals("Movies")) {
                    return null;
                }
                if (th.text().equals("Genre")) {
                    movie.setGenre(tr.text());
                }
                if (th.text().equals("Format")) {
                    movie.setFormat(tr.text());
                }
                if (th.text().equals("Year")) {
                    movie.setYear(tr.text());
                }
                if (th.text().equals("Director")) {
                    movie.setDirector(tr.text());
                }
                if (th.text().equals("Writers")) {
                    movie.setWriters(tr.text());
                }
                if (th.text().equals("Stars")) {
                    movie.setStars(tr.text());
                }

            }
            return movie;
        } else {
            return null;
        }

    }

    public Book GetBook(String url) throws IOException {
        if (url.contains("?id=")) {
            Book book = new Book();
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            Elements table = htmlDocument.select("table");
            Elements rows = table.select("tr");
            for (int i = 0; i < rows.size(); i++) {
                Element th = rows.get(i).child(0);
                Element tr = rows.get(i).child(1);
                if (th.text().equals("Category") && !tr.text().equals("Books")) {
                    return null;
                }
                if (th.text().equals("Genre")) {
                    book.setGenre(tr.text());
                }
                if (th.text().equals("Format")) {
                    book.setFormat(tr.text());
                }
                if (th.text().equals("Year")) {
                    book.setYear(tr.text());
                }
                if (th.text().equals("Authors")) {
                    book.setAuthor(tr.text());
                }
                if (th.text().equals("Publisher")) {
                    book.setPublisher(tr.text());
                }
                if (th.text().equals("ISBN")) {
                    book.setIsbn(tr.text());
                }

            }
            return book;
        } else {
            return null;
        }

    }

    public Music GetMusic(String url) throws IOException {
        if (url.contains("?id=")) {
            Music music = new Music();
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            Elements table = htmlDocument.select("table");
            Elements rows = table.select("tr");
            for (int i = 0; i < rows.size(); i++) {
                Element th = rows.get(i).child(0);
                Element tr = rows.get(i).child(1);
                if (th.text().equals("Category") && !tr.text().equals("Music")) {
                    return null;
                }
                if (th.text().equals("Genre")) {
                    music.setGenre(tr.text());
                }
                if (th.text().equals("Format")) {
                    music.setFormat(tr.text());
                }
                if (th.text().equals("Year")) {
                    music.setYear(tr.text());
                }
                if (th.text().equals("Artist")) {
                    music.setArtist(tr.text());
                }


            }
            return music;
        } else {
            return null;
        }

    }


    public SearchedItemLine GetBySearch(String searchWord) throws IOException {

        List<String> subLinks = new ArrayList<>();
        SearchedItemLine searchedItemLine=null;
        int pageTovisit = 0;
        int visitedpage = 0;
        for (String link : Links) {
            if (link.endsWith("?cat=books") || link.endsWith("?cat=movies") || link.endsWith("?cat=music") ) {
                Elements linksOnPage = GetSubLinks(link);
                pageTovisit = linksOnPage.size();
                visitedpage = 6;
                numberofvisitedpages++;
                Currentdepth++;
                for (Element sub : linksOnPage) {
                    subLinks.add(sub.absUrl("href"));
                }
                while (visitedpage < pageTovisit) {

                    if(visitedpage==6){ Currentdepth++;}
                    numberofvisitedpages++;

                    if(searchForWord( searchWord, subLinks.get(visitedpage)))
                    {

                        Music music = GetMusic(subLinks.get(visitedpage));

                        if(music!=null){
                            searchedItemLine=new SearchedItemLine<Music>(music);
                            searchedItemLine.setId(counter.incrementAndGet());
                            end_Time = System.nanoTime();
                            searchedItemLine.setTime_elapse(start_Time,end_Time);
                            PreviousDepth=Currentdepth;
                            Currentdepth=0;
                            crawl.SetExplorer(numberofvisitedpages);
                            crawl.SetDepth(PreviousDepth);
                            crawl.setTime_elapse(start_Time,end_Time);
                            numberofvisitedpages=0;
                            return searchedItemLine; }
                        else
                        {
                            Book book = GetBook(subLinks.get(visitedpage));

                        if(book!=null)
                        {
                            searchedItemLine=new SearchedItemLine<Book>(book);
                            searchedItemLine.setId(counter.incrementAndGet());
                            end_Time = System.nanoTime();
                            searchedItemLine.setTime_elapse(start_Time,end_Time);
                            crawl.setTime_elapse(start_Time,end_Time);
                            crawl.SetExplorer(numberofvisitedpages);
                            crawl.SetDepth(Currentdepth);
                            numberofvisitedpages=0;
                            Currentdepth=0;
                            return searchedItemLine;
                        }
                        else
                        {
                            Movie movie = GetMovie((subLinks.get(visitedpage)));

                            if(visitedpage==6){ Currentdepth++;}
                            if(movie!=null)
                            {
                                searchedItemLine=new SearchedItemLine<Movie>(movie);
                                searchedItemLine.setId(counter.incrementAndGet());
                                end_Time = System.nanoTime();
                                searchedItemLine.setTime_elapse(start_Time,end_Time);
                                crawl.setTime_elapse(start_Time,end_Time);
                                crawl.SetExplorer(numberofvisitedpages);
                                crawl.SetDepth(Currentdepth);
                                numberofvisitedpages=0;
                                Currentdepth=0;
                                return searchedItemLine;
                            }
                        }
                        }


                    }
                    visitedpage++;
                }
                Currentdepth=0;

                subLinks.clear();
            }







            }




        return null;
    }

    private  Elements GetSubLinks(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        Document htmlDocument = connection.get();
        Elements linksOnPage = htmlDocument.select("a[href]");
        return  linksOnPage;
    }
    public boolean searchForWord(String searchWord, String Url) throws IOException {
        System.out.println("Searching for the word " + searchWord + "...");
        Connection connection = Jsoup.connect(Url);
        Document htmlDocument = connection.get();
        String bodyText = htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());

    }
}