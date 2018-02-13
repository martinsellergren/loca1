import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import map.*;

public class MapRequestTests {

    boolean highQ = true;

    // @Test
    // public void fetch3_world() {
    //     double west = -180;
    //     double north = MapImageView.LATITUDE_BOUND;
    //     double east = 180;
    //     double south = -MapImageView.LATITUDE_BOUND;
    //     int zoom = 3;
    //     //boolean highQ = false;
    //     MapImageView view = new MapImageView(west, north, east, south, zoom, highQ);
    //     MapRequest req = new MapRequest(view);

    //     try {
    //         BasicImage[] imgs = req.fetch3();
    //         assertTrue(imgs[0] != null);
    //         assertTrue(imgs[1] != null);
    //         assertTrue(imgs[2] != null);

    //         imgs[0].save("test_world_full.png");
    //         imgs[1].save("test_world_label.png");
    //         imgs[2].save("test_world_box.png");
    //     }
    //     catch (IOException e) {
    //         System.out.println(e);
    //         assertTrue(false);
    //     }
    // }

    // @Test
    // public void fetch3_europe() {
    //     double west = -18.36914062;
    //     double east = 56.33789063;
    //     double north = 60.06484046;
    //     double south = 32.99023556;
    //     int zoom = 5;
    //     //boolean highQ = true;
    //     MapImageView view = new MapImageView(west, north, east, south, zoom, highQ);
    //     MapRequest req = new MapRequest(view);

    //     try {
    //         BasicImage[] imgs = req.fetch3();
    //         assertTrue(imgs[0] != null);
    //         assertTrue(imgs[1] != null);
    //         assertTrue(imgs[2] != null);

    //         imgs[0].save("test_europe_full.png");
    //         imgs[1].save("test_europe_label.png");
    //         imgs[2].save("test_europe_box.png");
    //     }
    //     catch (IOException e) {
    //         System.out.println(e);
    //         assertTrue(false);
    //     }
    // }

    @Test
    public void fetch3_rudboda_z14() {
        double west = 18.146238;
        double east = 18.191557;
        double north = 59.383059;
        double south = 59.371692;
        int zoom = 14;
        //boolean highQ = false;
        MapImageView view = new MapImageView(west, north, east, south, zoom, highQ);
        MapRequest req = new MapRequest(view);

        try {
            BasicImage[] imgs = req.fetch3();
            assertTrue(imgs[0] != null);
            assertTrue(imgs[1] != null);
            assertTrue(imgs[2] != null);

            imgs[0].save("test_rudboda_z14_full.png");
            imgs[1].save("test_rudboda_z14_label.png");
            imgs[2].save("test_rudboda_z14_box.png");
        }
        catch (IOException e) {
            System.out.println(e);
            assertTrue(false);
        }
    }

    // @Test
    // public void fetch3_rudboda_z17() {
    //     double west = 18.146238;
    //     double east = 18.191557;
    //     double north = 59.383059;
    //     double south = 59.371692;
    //     int zoom = 17;
    //     //boolean highQ = false;
    //     MapImageView view = new MapImageView(west, north, east, south, zoom, highQ);
    //     MapRequest req = new MapRequest(view);

    //     try {
    //         BasicImage[] imgs = req.fetch3();
    //         assertTrue(imgs[0] != null);
    //         assertTrue(imgs[1] != null);
    //         assertTrue(imgs[2] != null);

    //         imgs[0].save("test_rudboda_z17_full.png");
    //         imgs[1].save("test_rudboda_z17_label.png");
    //         imgs[2].save("test_rudboda_z17_box.png");
    //     }
    //     catch (IOException e) {
    //         System.out.println(e);
    //         assertTrue(false);
    //     }
    // }


    // @Test
    // public void fetch_whole() {
    //     double west = -180;
    //     double north = MapImageView.LATITUDE_BOUND;
    //     double east = 180;
    //     double south = -MapImageView.LATITUDE_BOUND;
    //     int[] zs = new int[] {0,2,3};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

    // @Test
    // public void fetch_mid() {
    //     double west = -50;
    //     double north = 60;
    //     double east = 50;
    //     double south = -60;
    //     int[] zs = new int[] {0,2,4};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

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
    // public void fetch_europe() {
    //     double west = -18.36914062;
    //     double east = 56.33789063;
    //     double north = 60.06484046;
    //     double south = 32.99023556;
    //     int[] zs = new int[] {5};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

    // @Test
    // public void fetch_lidingo() {
    //     double west = 18.08246612548828;
    //     double east = 18.27404022216797;
    //     double north = 59.39407306645033;
    //     double south = 59.33564087770051;
    //     int[] zs = new int[] {14};
    //     fetchBoundsHelper(west, north, east, south, zs);
    // }

    // @Test
    // public void fetch_rudboda() {
    //     double west = 18.146238;
    //     double east = 18.191557;
    //     double north = 59.383059;
    //     double south = 59.371692;
    //     int[] zs = new int[] {17};
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

    public/***/ void fetchBoundsHelper(double west, double north, double east, double south, int[] zlevels) {
        fetchBoundsHelper(west, north, east, south, zlevels, false, false);
    }
    public/***/ void fetchBoundsHelper(double west, double north, double east, double south, int[] zlevels, boolean highQ, boolean extend) {
        for (int z : zlevels) {
            MapImageView lowQ_noExt = new MapImageView(west, north, east, south, z, false);
            MapImageView lowQ_ext = lowQ_noExt.getExtendedView();
            MapImageView highQ_noExt = new MapImageView(west, north, east, south, z, true);
            MapImageView highQ_ext = highQ_noExt.getExtendedView();

            MapRequest r1 = new MapRequest(lowQ_noExt);
            MapRequest r2 = new MapRequest(highQ_noExt);
            MapRequest r3 = new MapRequest(lowQ_ext);
            MapRequest r4 = new MapRequest(highQ_ext);

            if (!highQ) fetchHelper(r1);
            if (highQ) fetchHelper(r2);
            if (extend && !highQ) fetchHelper(r3);
            if (extend && highQ) fetchHelper(r4);
        }
    }

    static char imgIndex = 'a';
    public/***/ void fetchHelper(MapRequest req) {
        try {
            BasicImage img = req.fetch(MapRequest.FULL_STYLE_ID);
            assertTrue(img != null);

            img.save(imgIndex++ + "." +req.toString() + ".png");
        }
        catch (IOException e) {
            assertTrue(false);
        }
    }


    // @Test
    // public void fetchRaw() {
    //     // ny
    //     MapImageView v = new MapImageView(-73.98572, 40.74843, 500, 500, 16, 512);

    //     // middle east
    //     // double west = 45.791015625;
    //     // double north = 52.802761415419674;
    //     // double east = 99.931640625;
    //     // double south = 25.799891182088334;
    //     // int z = 14;

    //     // italy
    //     // double west = 9.2724609375;
    //     // double east = 17.6220703125;
    //     // double north = 44.84029065139799;
    //     // double south = 41.70572851523752;
    //     // int z = 16;

    //     // boolean doubleQ = false;
    //     // MapImageView v = new MapImageView(west, north, east, south, z, doubleQ);

    //     boolean attrib = true;
    //     MapRequest req = new MapRequest(v, attrib);

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
