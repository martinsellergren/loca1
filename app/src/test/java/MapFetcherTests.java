import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

//needs internet
public class MapFetcherTests {

    // @Test
    // public void fetchMapImage_smallImg() {
    //     int x = 0;
    //     int y = 0;
    //     int zoom = 4;
    //     int w = MapFetcher.mapboxMaxWidthHeight;
    //     int h = MapFetcher.mapboxMaxWidthHeight / 2;

    //     try {
    //         MapImage img = MapFetcher.fetchMapImage(new MapBasics(x, y, w, h, zoom), MapFetcher.fullStyleID, false, false);

    //         assertEquals(img.getWidth(), w);
    //         assertEquals(img.getHeight(), h);
    //         img.save("test_fetchMapImage_smallImg.png");
    //     }
    //     catch (IOException e) {
    //         assertTrue(false);
    //     }
    // }

    // @Test
    // public void fetchMapImage_bigImg() {
    //     int x = 0;
    //     int y = 0;
    //     int zoom = 9;
    //     int w = MapFetcher.mapboxMaxWidthHeight * 2;
    //     int h = MapFetcher.mapboxMaxWidthHeight * 5;

    //     try {
    //         MapImage img = MapFetcher.fetchMapImage(new MapBasics(x, y, w, h, zoom), MapFetcher.fullStyleID, false, false);

    //         assertEquals(img.getWidth(), w);
    //         assertEquals(img.getHeight(), h);
    //         img.save("test_fetchMapImage_bigImg.png");
    //     }
    //     catch (IOException e) {
    //         System.out.println(e.getMessage());
    //         assertTrue(false);
    //     }
    // }

    @Test
    public void fetchRawImage_x_fetched() {
        double lon = 0;
        double lat = 0;
        int width = MapFetcher.IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT;
        int height = MapFetcher.IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT;
        int zoom = 7;

        MapView v = new MapView(lon, lat, width, height, zoom);

        try {
            BasicImage img = MapFetcher.fetchRawImage(v, MapFetcher.FULL_STYLE_ID);
            assertTrue(img != null);

            img.save("mapTest.png");
        }
        catch (IOException e) {
            assertTrue(false);
        }
    }
}
