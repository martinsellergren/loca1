import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

//needs internet
public class MapFetcherTests {

    @Test
    public void fetchMapImage_smallImg() {
        int x = 0;
        int y = 0;
        int zoom = 4;
        int w = MapFetcher.mapboxMaxWidthHeight;
        int h = MapFetcher.mapboxMaxWidthHeight / 2;

        try {
            MapImage img = MapFetcher.fetchMapImage(new MapBasics(x, y, w, h, zoom), MapFetcher.fullStyleID, false, false);

            assertEquals(img.getWidth(), w);
            assertEquals(img.getHeight(), h);
            img.save("test_fetchMapImage_smallImg.png");
        }
        catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void fetchMapImage_bigImg() {
        int x = 0;
        int y = 0;
        int zoom = 9;
        int w = MapFetcher.mapboxMaxWidthHeight * 2;
        int h = MapFetcher.mapboxMaxWidthHeight * 5;

        try {
            MapImage img = MapFetcher.fetchMapImage(new MapBasics(x, y, w, h, zoom), MapFetcher.fullStyleID, false, false);

            assertEquals(img.getWidth(), w);
            assertEquals(img.getHeight(), h);
            img.save("test_fetchMapImage_bigImg.png");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void fetchRawImage_x_fetched() {
        MapBasics mb = new MapBasics(0, 0, MapFetcher.mapboxMaxWidthHeight, MapFetcher.mapboxMaxWidthHeight, 0);

        try {
            BufferedImage img = MapFetcher.fetchRawImage(mb, MapFetcher.fullStyleID, false, false);
            assertTrue(img != null);
        }
        catch (IOException e) {
            assertTrue(false);
        }
    }
}
