import TCI.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(JUnitParamsRunner.class)
public class SpiderTest {


private MusicMovieBookLine mockMBMLine;
private  Spider spider;
private  Spider spiderWithCrawlInfo ;
private CrawlInformation crawlInformation;

int i=0;
@Before
public void setup() throws IOException {
    spider = new Spider();
    mockMBMLine = mock(MusicMovieBookLine.class);
    crawlInformation = mock(CrawlInformation.class);
    spiderWithCrawlInfo = new Spider(crawlInformation);
    spider.GetAllLinks("http://i298537.hera.fhict.nl/TCI/index.php");
    spiderWithCrawlInfo.GetAllLinks("http://i298537.hera.fhict.nl/TCI/index.php");
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
        verify(bookLine, atLeast(1)).setTime_elapse(12,13);

    }

    @Test
    public void testIfCorrectBookHasBeenFetchedFromID102() throws IOException {
         Book mockBook = mock(Book.class);
         when(mockBook.getIsbn()).thenReturn("978-0132350884");
         Book actualBook = spider.GetBook("http://i298537.hera.fhict.nl/TCI/details.php?id=102");
         assertEquals(mockBook.getIsbn(),actualBook.getIsbn());
    }
    @Test
    public void testIfCorrectMovieHasBeenFetchedFromID201() throws IOException {
        Movie mockMovie = mock(Movie.class);
        when(mockMovie.getDirector()).thenReturn("Robert Zemeckis");
        Movie actualMovie = spider.GetMovie("http://i298537.hera.fhict.nl/TCI/details.php?id=201");
        assertThat("Is Not Null ",actualMovie,not(is(nullValue())));

        assertThat("Same Directors Means Same movies ",actualMovie.getDirector(),is(mockMovie
                .getDirector()));

    }
          @Test(expected = NullPointerException.class)
          public void  testIfNullPointerExceptionThrownWithNullCrawlInformation() throws IOException {
              SearchedItemLine searchedItem = spider.GetBySearch("Forrest Gump");


          }

    @Test
    public void  testIfSearchForWorks() throws IOException {

       boolean exists = spider.searchForWord("Forrest Gump","http://i298537.hera.fhict.nl/TCI/details.php?id=201");
       boolean NotExists = spider.searchForWord("Elvis Forever","http://i298537.hera.fhict.nl/TCI/details.php?id=303");
       assertEquals(true,exists);
        assertEquals(false,NotExists);

    }
    @Test
    public void testIfFoundMusicOnGivingMusicName() throws IOException {
       SearchedItemLine searchedItemLine= spiderWithCrawlInfo.GetBySearch("The Very Thought of You");
      assertThat(searchedItemLine.getFounditem(), instanceOf(Music.class));

    }

    @Test
    public void testIfFoundCorrectMovie() throws IOException {
        SearchedItemLine searchedItemLine= spiderWithCrawlInfo.GetBySearch("The Princess Bride");
        assertEquals("Rob Reiner",((Movie)searchedItemLine.getFounditem()).getDirector());

    }
    @Test
    public void testIfCrawlInformationGetsTimeElapsed() throws IOException {

        when(crawlInformation.getTime_elapse()).thenCallRealMethod();
        doCallRealMethod().when(crawlInformation).setTime_elapse(Mockito.anyLong(),Mockito.anyLong());
        spiderWithCrawlInfo.GetBySearch("Elvis Forever");
        assertNotEquals(0,crawlInformation.getTime_elapse());

    }


    @Test
    public void testIfCrawlInformationGetsCorrectPageDepth() throws IOException {

        when(crawlInformation.GetDepth()).thenCallRealMethod();
        doCallRealMethod().when(crawlInformation).SetExplorer(Mockito.anyInt());
        spiderWithCrawlInfo.GetBySearch("No Fences");
        assertNotEquals(2,crawlInformation.GetDepth());

    }

    private static final Object[] getItemsToSearchFor()
    {


        return $($("No Fences",18),$("Forrest Gump",9),$("The Clean Coder: A Code of Conduct for Professional Programmers",3));



            }




    @Test
    @Parameters(method="getItemsToSearchFor")
    public void   testIfCrawlInformationGetsCorrectPageExplored(String name, int i) throws IOException {
        when(crawlInformation.GetEXplorer()).thenCallRealMethod();
        doCallRealMethod().when(crawlInformation).SetExplorer(Mockito.anyInt());
        spiderWithCrawlInfo.GetBySearch(name);
        assertEquals(i,crawlInformation.GetEXplorer());
    }

}
