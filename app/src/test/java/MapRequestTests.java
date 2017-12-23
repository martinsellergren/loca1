import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import map.*;

public class MapRequestTests {

    @Test
    public void fetch_whole() {
        double west = -180;
        double north = MapImageView.LATITUDE_BOUND;
        double east = 179.999;
        double south = -MapImageView.LATITUDE_BOUND;
        int[] zs = new int[] {0,2};
        fetchBoundsHelper(west, north, east, south, zs);
    }

    @Test
    public void fetch_mid() {
        double west = -50;
        double north = 60;
        double east = 50;
        double south = -60;
        int[] zs = new int[] {3};
        fetchBoundsHelper(west, north, east, south, zs);
    }

    // @Test
    // public void fetch_north() {
    //     double west = -50;
    //     double north = MapImageView.LATITUDE_BOUND;
    //     double east = 50;
    //     double south = 10;
    //     int[] zs = new int[] {2};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

    // @Test
    // public void fetch_south() {
    //     double west = -100;
    //     double north = -10;
    //     double east = 50;
    //     double south = -MapImageView.LATITUDE_BOUND;
    //     int[] zs = new int[] {2};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

    // @Test
    // public void fetch_otherside() {
    //     double west = 0;
    //     double east = 0;
    //     double north = MapImageView.LATITUDE_BOUND;
    //     double south = -MapImageView.LATITUDE_BOUND;
    //     int[] zs = new int[] {0,1,2};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

    // @Test
    // public void fetch_sweden() {
    //     double west = 9.887695;
    //     double east = 23.862305;
    //     double north = 69.446949;
    //     double south = 55.336956;
    //     int[] zs = new int[] {5};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

    // @Test
    // public void fetch_mefjard() {
    //     double west = 18.472567;
    //     double east = 18.487844;
    //     double north = 59.075697;
    //     double south = 59.042776;
    //     int[] zs = new int[] {10};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

    private void fetchBoundsHelper(double west, double north, double east, double south, int[] zlevels) {
        for (int z : zlevels) {
            MapImageView lowQ_noExt = new MapImageView(west, north, east, south, z, false);
            MapImageView lowQ_ext = lowQ_noExt.getExtendedView();
            MapImageView highQ_noExt = new MapImageView(west, north, east, south, z, true);
            MapImageView highQ_ext = highQ_noExt.getExtendedView();

            MapRequest r1 = new MapRequest(lowQ_noExt);
            MapRequest r2 = new MapRequest(highQ_noExt);
            MapRequest r3 = new MapRequest(lowQ_ext);
            MapRequest r4 = new MapRequest(highQ_ext);

            fetchHelper(r1);
            //fetchHelper(r2);
            fetchHelper(r3);
            //fetchHelper(r4);
        }
    }

    static int imgIndex = 0;
    private void fetchHelper(MapRequest req) {
        try {
            BasicImage img = req.fetch(MapRequest.FULL_STYLE_ID);
            assertTrue(img != null);

            img.save(imgIndex++ + "-" + req.toString() + ".png");
        }
        catch (IOException e) {
            assertTrue(false);
        }
    }


    // @Test
    // public void fetchRaw() {
    //     // double lon = 180;
    //     // double lat = 0;
    //     // int w = 512;
    //     // int h = 512;
    //     // int z = 0;
    //     // boolean doubleQ = false;
    //     // MapImageView v = new MapImageView(lon, lat, w, h, z, doubleQ);

    //     double west = 0;
    //     double north = MapImageView.LATITUDE_BOUND;
    //     double east = 0;
    //     double south = -MapImageView.LATITUDE_BOUND;
    //     int z = 0;
    //     boolean doubleQ = false;
    //     MapImageView v = new MapImageView(west, north, east, south, z, doubleQ);

    //     boolean attrib = true;
    //     boolean extend = false;
    //     MapRequest req = new MapRequest(v, attrib, extend);
    //     //MapRequest req = new MapRequest(lon, lat, w, h, z, doubleQ, attrib, extend);

    //     try {
    //         BasicImage img = req.fetchRaw(MapRequest.FULL_STYLE_ID);
    //         assertTrue(img != null);

    //         img.save(req.toString() + ".png");
    //     }
    //     catch (IOException e) {
    //         assertTrue(false);
    //     }
    // }
}
