import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import map.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapRequestTests {

    boolean highQ = true;
    Language lang = Language.EN;

    @Test
    public void fetcher() {
        // fetch3Helper(MapImageView.world(), "world");
        // fetch3Helper(MapImageView.europe(), "europe");
        // fetch3Helper(MapImageView.sweden(), "sweden");
        // fetch3Helper(MapImageView.uppsala(), "uppsala");
        // fetch3Helper(MapImageView.luthagen(), "luthagen");
        // fetch3Helper(MapImageView.lidingo(), "lidingo");
        // fetch3Helper(MapImageView.rudboda(), "rudboda");
        // fetch3Helper(MapImageView.mefjard(), "mefjard");
        // fetch3Helper(MapImageView.lonEdge(), "lonEdge");
    }

    public void fetch3Helper(MapImageView v, String saveDir) {
        try {
            //Path p = Paths.get("test_MapRequestTests_" + v.toString().replace(' ', '_'));
            Path p = Paths.get(saveDir);
            MapRequest req = new MapRequest(v, p, lang);
            TiledImage[] imgs = req.fetch3();
            try {
                imgs[0].save(p.resolve("full.png"));
                imgs[1].save(p.resolve("code.png"));
                imgs[2].save(p.resolve("box.png"));
            }
            catch (OutOfMemoryError exc) {
                System.out.print("Out of mem: " + req);
            }
        }
        catch (IOException ex) {
            assertTrue(false);
        }
    }
    // public void fetch3Helper(double w, double s, double e, double n, int z) {
    //     fetch3Helper(new MapImageView(w, s, e, n, z, this.highQ));
    // }

//     @Test
//     public void fetch_mid() {
//         double west = -50;
//         double south = -60;
//         double east = 50;
//         double north = 60;
//         int z = 4;
//         fetch3Helper(west, south, east, north, z);
//     }

//     @Test
//     public void fetch_north() {
//         double west = -50;
//         double south = 10;
//         double east = 50;
//         double north = MapImageView.LATITUDE_BOUND;
//         int z = 2;
//         fetch3Helper(west, south, east, north, z);
//     }

//     @Test
//     public void fetch_south() {
//         double west = -100;
//         double south = -MapImageView.LATITUDE_BOUND;
//         double east = 50;
//         double north = -10;
//         int z = 2;
//         fetch3Helper(west, south, east, north, z);
//     }

//     @Test
//     public void fetch_otherside() {
//         double west = 0;
//         double south = -MapImageView.LATITUDE_BOUND;
//         double east = 0;
//         double north = MapImageView.LATITUDE_BOUND;
//         int z = 2;
//         fetch3Helper(west, south, east, north, z);
//     }



    // @Test
    // public void fetch_world() {
    //     double west = -180;
    //     double south = -MapImageView.LATITUDE_BOUND;
    //     double east = 180;
    //     double north = MapImageView.LATITUDE_BOUND;
    //     int z = 3;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // @Test
    // public void fetch_europe() {
    //     double west = -18.36914062;
    //     double south = 32.99023556;
    //     double east = 56.33789063;
    //     double north = 60.06484046;
    //     int z = 5;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // @Test
    // public void fetch_sweden() {
    //     double west = 9.887695;
    //     double south = 55.336956;
    //     double east = 23.862305;
    //     double north = 69.446949;
    //     int z = 5;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // @Test
    // public void fetch_uppsala() {
    //     double west = 17.572212;
    //     double south = 59.784881;
    //     double east = 17.73529;
    //     double north = 59.879084;
    //     int z = 12;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // @Test
    // public void fetch_luthagen() {
    //     double west = 17.602767;
    //     double south = 59.85836;
    //     double east = 17.649845;
    //     double north = 59.863962;
    //     int z = 16;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // @Test
    // public void fetch_lidingo() {
    //     double west = 18.08246612548828;
    //     double south = 59.33564087770051;
    //     double east = 18.27404022216797;
    //     double north = 59.39407306645033;
    //     int z = 14;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // @Test
    // public void fetch_rudboda() {
    //     double west = 18.15;
    //     double south = 59.372;
    //     double east = 18.19;
    //     double north = 59.383;
    //     int z = 17;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // @Test
    // public void fetch_mefjard() {
    //     double west = 18.460774;
    //     double south = 58.958251;
    //     double east = 18.619389;
    //     double north = 59.080544;
    //     int z = 13;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // @Test
    // public void fetch_lonEdge() {
    //     double west = 177;
    //     double south = 54;
    //     double east = -168;
    //     double north = 66;
    //     int z = 3;
    //     fetch3Helper(west, south, east, north, z);
    // }

    // static char imgIndex = 'a';
    // public/***/ void fetchHelper(double w, double s, double e, double n, int z) {
    //     try {
    //         MapImageView v = new MapImageView(w, s, e, n, z, highQ);
    //         MapRequest req = new MapRequest(v, "test_MapRequestTests", lang);
    //         TiledImage img = req.fetch(MapRequest.FULL_STYLE_ID_ENG, ""+imgIndex);
    //         img.save(imgIndex++ + "." +req.toString() + ".png");
    //     }
    //     catch (IOException ex) {
    //         assertTrue(false);
    //     }
    // }

    // @Test
    // public void fetchRaw() {
    //     // ny
    //     MapImageView v = new MapImageView(-73.98572, 40.74843, 500, 500, 16, 512);

    //     // middle east
    //     // double west = 45.791015625;
    //     // double south = 25.799891182088334;
    //     // double east = 99.931640625;
    //     // double north = 52.802761415419674;
    //     // int z = 14;
    //     // italy
    //     // double west = 9.2724609375;
    //     // double south = 41.70572851523752;
    //     // double east = 17.6220703125;
    //     // double north = 44.84029065139799;
    //     // int z = 16;
    //     // boolean doubleQ = false;
    //     // MapImageView v = new MapImageView(west, south, east, north, z, doubleQ);

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

    // public/***/ BasicImage[][] fetcher(MapRequest req) {
    //     try {
    //         return req.fetch(MapRequest.FULL_STYLE_ID);
    //     }
    //     catch (IOException e) {
    //         assertTrue(false);
    //     }
    // }
}
