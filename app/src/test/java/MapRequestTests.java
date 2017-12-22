import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import map.*;

public class MapRequestTests {

    // @Test
    // public void fetch() {
    //     double lon = 0;
    //     double lat = 0;
    //     int width = MapRequest.IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT*2;
    //     int height = (int) (MapRequest.IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT * 6.4)*2;
    //     int zoom = 4;
    //     boolean doubleQ = true;
    //     boolean attrib = false;
    //     boolean extend = false;

    //     MapImageView v = new MapImageView(lon, lat, width, height, zoom, doubleQ);
    //     MapRequest req = new MapRequest(v, attrib, extend);

    //     try {
    //         BasicImage img = req.fetch(MapRequest.FULL_STYLE_ID);
    //         assertTrue(img != null);

    //         img.save("mapTest_big2.png");
    //     }
    //     catch (IOException e) {
    //         assertTrue(false);
    //     }
    // }

    @Test
    public void fetchRaw() {
        // double lon = 180;
        // double lat = 0;
        // int w = 512;
        // int h = 512;
        // int z = 0;
        // boolean doubleQ = false;
        // MapImageView v = new MapImageView(lon, lat, w, h, z, doubleQ);

        double west = 0;
        double north = MapImageView.LATITUDE_BOUND;
        double east = 0;
        double south = -MapImageView.LATITUDE_BOUND;
        int z = 0;
        boolean doubleQ = false;
        MapImageView v = new MapImageView(west, north, east, south, z, doubleQ);

        boolean attrib = false;
        boolean extend = false;
        MapRequest req = new MapRequest(v, attrib, extend);
        //MapRequest req = new MapRequest(lon, lat, w, h, z, doubleQ, attrib, extend);

        try {
            BasicImage img = req.fetchRaw(MapRequest.FULL_STYLE_ID);
            assertTrue(img != null);

            img.save("mapTest_small2.png");
        }
        catch (IOException e) {
            assertTrue(false);
        }
    }
}
