import TCI.MusicMovieBookLine;
import TCI.Spider;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

public class SpiderTest {


private Spider spider = mock(Spider.class);
private MusicMovieBookLine mockMBMLine = mock(MusicMovieBookLine.class);
@Test
public void testIfLinksListisBeingFilled() throws IOException
{
    Spider spider = new Spider();
    spider.GetAllLinks("http://i298537.hera.fhict.nl/TCI/index.php");
    int size = spider.Links.size();
    assertNotEquals(size, 0);
}

    @Test(expected = IllegalArgumentException.class)
    public void testIfUnsupportedMimeTypeExceptionThrown() throws IOException
    {
        Spider spider = new Spider();
        spider.GetAllLinks("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=AIzaSyCjZurK9v2FCUwiQdMGODbP4VUwWtY8qQU");

    }

    @Test
    public void testIfIdOfMusicMovieBookLineSetsCorrectly()
    {
        final AtomicLong counter = new AtomicLong();

        when(mockMBMLine.getId()).thenReturn(counter.incrementAndGet());
        assertEquals(mockMBMLine.getId(), 1);
    }

    @Test
    public  void testIfBookListFilled() throws IOException {


        spider.GetAllLinks("http://i298537.hera.fhict.nl/TCI/index.php");
        assertNotNull(spider.getLinks());
        assertNotEquals(spider.getLinks().size(),0);

    }






}
