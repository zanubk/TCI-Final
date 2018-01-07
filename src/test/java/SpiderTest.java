import TCI.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.*;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

@RunWith(JUnitParamsRunner.class)
public class SpiderTest {


private MusicMovieBookLine mockMBMLine = mock(MusicMovieBookLine.class);
private  Spider spider=null;

int i=0;
@Before
public void BeforeEachTest()
{
    spider = new Spider();

}


private static final Object[] getInvalidUrls()
{
    return new String[][]{{"https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=AIzaSyCjZurK9v2FCUwiQdMGODbP4VUwWtY8qQU"}};    }
    private static final Object[] validUrls()
    {
        return new String[][]{
                {"http://i298537.hera.fhict.nl/TCI/catalog.php?cat=movies"},
                {"http://i298537.hera.fhict.nl/TCI/catalog.php?cat=musics"},
                {"http://i298537.hera.fhict.nl/TCI/catalog.php?cat=books"},
                {"http://i298537.hera.fhict.nl/TCI/details.php?id=201"}

        };    }

    private static final Object[] getBooks()
    {


        return new Book[][]{
                 {new Book("Tech","Ebook","2008","Robert C. Martin","Prentice Hall","978-0132350884")}
                ,{new Book("Tech","Audio","2011","Robert C. Martin","Prentice Hall","007-6092046981")}
                ,{new Book("Tech","Paperback","1994","Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides","Prentice Hall","978-0201633610")}
                ,{new Book("Tech","Hardcover","1999","Martin Fowler, Kent Beck, John Brant, William Opdyke, Don Roberts","Addison-Wesley Professional","978-0201485677")}

        };
    }
    private static final Object[] getMoviesDirector()
    {


        return new String[][]{
                {"Robert Zemeckis"}
                ,{"Peter Jackson"}
                ,{"Mike Judge"}
                ,{"Rob Reiner"}
        };
    }
    private static final Object[] getMusicArtist()
    {


        return new String[][]{
                {"Ludwig van Beethoven"}
                ,{"Elvis Presley"}
                ,{"Garth Brooks"}
                ,{"Nat King Cole"}
        };
    }

    @Test
public void testIfLinksListisBeingFilled() throws IOException
{
    spider = new Spider();
    spider.GetAllLinks("http://i298537.hera.fhict.nl/TCI/index.php");
    int size = spider.Links.size();
    assertNotEquals(size, 0);
  }


    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "getInvalidUrls")
    public void testIfUnsupportedMimeTypeExceptionThrown(String  invalidUrl) throws IOException
    {

        spider.GetAllLinks(invalidUrl);

    }

    @Test
    public void testIfIdOfMusicMovieBookLineSetsCorrectly()
    {
        final AtomicLong counter = new AtomicLong();

        when(mockMBMLine.getId()).thenReturn(counter.incrementAndGet());
        assertEquals(mockMBMLine.getId(), 1);
    }

    @Test
    @Parameters(method = "getBooks")
    public  void testIfBookListFilledExactlyWithBooks(Book book) throws IOException {

        spider.GetAllLinks("http://i298537.hera.fhict.nl/TCI/catalog.php?cat=books");
        MusicMovieBookLine bookLine = spider.GetAll();
        assertEquals(4,bookLine.getBooks().size());
        List<String> formats= new ArrayList<>();
         for(Book b : bookLine.getBooks())
         {
             formats.add(b.getFormat());
         }
         assertTrue(formats.contains(book.getFormat()));
    }
    @Test
    @Parameters(method = "getMoviesDirector")
    public  void testIfMovieListFilledExactlyWithMovies(String movieDirector) throws IOException {

        spider.GetAllLinks("http://i298537.hera.fhict.nl/TCI/catalog.php?cat=movies");
        MusicMovieBookLine musicMovieBookLine = spider.GetAll();
        List<String> formats= new ArrayList<>();
        for(Movie b : musicMovieBookLine.getMovies())
        {
            formats.add(b.getDirector());
        }
        assertTrue(formats.contains(movieDirector));
    }
    @Test
    @Parameters(method = "getMusicArtist")
    public  void testIfMusicListFilledExactlyWithMusics(String musicArtist) throws IOException {

        spider.GetAllLinks("http://i298537.hera.fhict.nl/TCI/catalog.php?cat=movies");
        MusicMovieBookLine musicMovieBookLine = spider.GetAll();
        List<String> artists= new ArrayList<>();
        for(Music b : musicMovieBookLine.getMusics())
        {
            artists.add(b.getArtist());
        }
        assertTrue(artists.contains(musicArtist));
    }


    @Test
    @Parameters(method = "validUrls")
    public  void testIfBookMusicMovieListFilledFromOtherUrls(String Url) throws IOException {

        spider.GetAllLinks(Url);
        MusicMovieBookLine bookLine = spider.GetAll();
        assertEquals(12,bookLine.getBooks().size()+bookLine.getMovies().size()+bookLine.getMusics().size());


    }

    @Test
    public void testIfSetTimeMethodIsCalled() throws IOException {

        MusicMovieBookLine bookLine = mock(MusicMovieBookLine.class);
        bookLine.setTime_elapse(12,13);
        verify(bookLine, atLeast(2)).setTime_elapse(12,13);

    }

    @Test
    public void testIfCorrectBookHasBeenFetched()
    {
         Book mockBook = mock(Book.class);
         //when(mockBook.getFormat()).thenReturn("")
    }






}
