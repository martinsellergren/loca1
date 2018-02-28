import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import map.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapRequestTests {

    boolean highQ = true;
    Language lang = Language.ENG;
    Path saveDir = Paths.get("test_MapRequestTests");

//     @Test
//     public void fetch_whole() {
//         double west = -180;
//         double north = MapImageView.LATITUDE_BOUND;
//         double east = 180;
//         double south = -MapImageView.LATITUDE_BOUND;
//         int z = 3;
//         fetchHelper(west, north, east, south, z);
//     }

//     @Test
//     public void fetch_mid() {
//         double west = -50;
//         double north = 60;
//         double east = 50;
//         double south = -60;
//         int z = 4;
//         fetchHelper(west, north, east, south, z);
//     }

//     @Test
//     public void fetch_north() {
//         double west = -50;
//         double north = MapImageView.LATITUDE_BOUND;
//         double east = 50;
//         double south = 10;
//         int z = 2;
//         fetchHelper(west, north, east, south, z);
//     }

//     @Test
//     public void fetch_south() {
//         double west = -100;
//         double north = -10;
//         double east = 50;
//         double south = -MapImageView.LATITUDE_BOUND;
//         int z = 2;
//         fetchHelper(west, north, east, south, z);
//     }

//     @Test
//     public void fetch_otherside() {
//         double west = 0;
//         double east = 0;
//         double north = MapImageView.LATITUDE_BOUND;
//         double south = -MapImageView.LATITUDE_BOUND;
//         int z = 2;
//         fetchHelper(west, north, east, south, z);
//     }

    // @Test
    // public void fetch_sweden() {
    //     double west = 9.887695;
    //     double north = 69.446949;
    //     double east = 23.862305;
    //     double south = 55.336956;
    //     int z = 5;
    //     fetch3Helper(west, north, east, south, z);
    // }

//     @Test
//     public void fetch_europe() {
//         double west = -18.36914062;
//         double east = 56.33789063;
//         double north = 60.06484046;
//         double south = 32.99023556;
//         int z = 5;
//         fetchHelper(west, north, east, south, z);
//     }

//     @Test
//     public void fetch_lidingo() {
//         double west = 18.08246612548828;
//         double east = 18.27404022216797;
//         double north = 59.39407306645033;
//         double south = 59.33564087770051;
//         int z = 14;
//         fetchHelper(west, north, east, south, z);
//     }

//     @Test
//     public void fetch_rudboda() {
//         double west = 18.146238;
//         double east = 18.191557;
//         double north = 59.383059;
//         double south = 59.371692;
//         int z = 17;
//         fetchHelper(west, north, east, south, z);
//     }

//     @Test
//     public void fetch_mefjard() {
//         double west = 18.472567;
//         double east = 18.487844;
//         double north = 59.075697;
//         double south = 59.042776;
//         int z = 10;
//         fetchHelper(west, north, east, south, z);
//     }


    static char imgIndex = 'a';
    public/***/ void fetchHelper(double w, double n, double e, double s, int z) {
        try {
            MapImageView v = new MapImageView(w, n, e, s, z, highQ);
            MapRequest req = new MapRequest(v, saveDir, lang);
            TiledImage img = req.fetch(MapRequest.FULL_STYLE_ID_ENG, ""+imgIndex);
            img.save(imgIndex++ + "." +req.toString() + ".png");
        }
        catch (IOException ex) {
            assertTrue(false);
        }
    }

    public void fetch3Helper(double w, double n, double e, double s, int z) {
        try {
            MapImageView v = new MapImageView(w, n, e, s, z, highQ);
            MapRequest req = new MapRequest(v, saveDir, lang);
            TiledImage[] imgs = req.fetch3();
            imgs[0].save("test_MapRequestTests_fetch3Helper_full.png");
            imgs[1].save("test_MapRequestTests_fetch3Helper_label.png");
            imgs[2].save("test_MapRequestTests_fetch3Helper_box.png");
        }
        catch (IOException ex) {
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

    //     MapRequest req = new MapRequest(v);

    //     try {
    //         BasicImage img = req.fetchRaw(MapRequest.FULL_STYLE_ID);
    //         assertTrue(img != null);

    //         img.save(req.toString() + ".png");
    //     }
    //     catch (IOException e) {
    //         assertTrue(false);
    //     }
    // }

    // @Test
    // public void fetchRaw_maxsize() {
    //     boolean highQ = true;
    //     int zoom = 3;
    //     MapImageView v = new MapImageView(0, 0, MapRequest.IMAGE_REQUEST_SIZE_LIMIT*2, MapRequest.IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT*2, zoom, highQ);
    //     MapRequest req = new MapRequest(v);

    //     try {
    //         BasicImage img = req.fetchRaw(MapRequest.FULL_STYLE_ID);
    //         assertTrue(img != null);

    //         img.save(req.toString() + ".png");
    //     }
    //     catch (IOException e) {
    //         assertTrue(false);
    //     }
    // }

    // @Test
    // public void fetch_tileSizes() {
    //     int zoom = 7;
    //     boolean doubleQ = false;
    //     MapImageView v = new MapImageView(0, 0, MapRequest.IMAGE_REQUEST_SIZE_LIMIT*5, MapRequest.IMAGE_REQUEST_SIZE_LIMIT*5, 7, highQ);

    //     MapRequest req = new MapRequest(v, saveDir);
    //     BasicImage[][] imgs = fetcher(req);

    //     assertEquals(512, imgs[0][0].getWidth());
    //     assertEquals(512, imgs[0][0].getHeight());
    // }

    // private BasicImage[][] fetcher(MapRequest req) {
    //     try {
    //         return req.fetch(MapRequest.FULL_STYLE_ID);
    //     }
    //     catch (IOException e) {
    //         assertTrue(false);
    //     }
    // }
}
