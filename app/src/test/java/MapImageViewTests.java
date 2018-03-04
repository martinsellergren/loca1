import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import map.*;

public class MapImageViewTests {

    @Test
    public void getGlobalPixelMax() {
        int[] xy = MapImageView.getGlobalPixelMax(0, false);
        assertEquals(xy[0], 512);
        assertEquals(xy[1], 512);
        xy = MapImageView.getGlobalPixelMax(0, true);
        assertEquals(xy[0], 1024);
        assertEquals(xy[1], 1024);
    }

    @Test
    public void okLatutudeBound() {
        boolean ok = MapImageView.okLatitudeBound(0, 100, 0, false);
        assertEquals(ok, true);
        ok = MapImageView.okLatitudeBound(0, 513, 0, false);
        assertEquals(ok, false);
    }

    // @Test
    // public void split_bigMap_multipleBlocks() {
    //     MapImageView v = new MapImageView(0, 0, 100, 90, 0);
    //     MapImageView[][] layout = v.split(10);
    //     assertTrue(layout.length == 9);
    //     assertTrue(layout[0].length == 10);
    //     double[] xy = v.getPixelCoordinates(layout[3][3].lon, layout[3][3].lat);
    //     assertEquals(xy[0], 35, 0.0001);
    //     assertEquals(xy[1], 35, 0.0001);
    //     xy = v.getPixelCoordinates(layout[7][3].lon, layout[7][3].lat);
    //     assertEquals(xy[0], 35, 0.0001);
    //     assertEquals(xy[1], 75, 0.0001);

    //     v = new MapImageView(0, 0, 101, 91, 0);
    //     layout = v.split(10);
    //     assertTrue(layout.length == 10);
    //     assertTrue(layout[0].length == 11);
    //     xy = v.getPixelCoordinates(layout[3][3].lon, layout[3][3].lat);
    //     assertEquals(xy[0], 35, 0.0001);
    //     assertEquals(xy[1], 35, 0.0001);
    //     xy = v.getPixelCoordinates(layout[7][3].lon, layout[7][3].lat);
    //     assertEquals(xy[0], 35, 0.0001);
    //     assertEquals(xy[1], 75, 0.0001);

    //     assertTrue(layout[9][10].width == 1);
    //     assertTrue(layout[9][10].height == 1);
    //     xy = v.getPixelCoordinates(layout[9][10].lon, layout[9][10].lat);
    //     assertEquals(xy[0], 100.5, 0.0001);
    //     assertEquals(xy[1], 90.5, 0.0001);
    // }

    // @Test
    // public void split_smallMap_1x1() {
    //     MapImageView v = new MapImageView(12345, 54321, 10, 10, 0);
    //     MapImageView[][] layout = v.split(10);
    //     assertEquals(layout.length, 1);
    //     assertEquals(layout[0].length, 1);
    //     assertEquals(layout[0][0].width, 10);
    //     assertEquals(layout[0][0].height, 10);

    //     v = new MapImageView(12345, 54321, 1, 1, 0);
    //     layout = v.split(10);
    //     assertEquals(layout.length, 1);
    //     assertEquals(layout[0].length, 1);
    //     assertEquals(layout[0][0].width, 1);
    //     assertEquals(layout[0][0].height, 1);
    // }

    // @Test
    // public void split_shortMap_multipleBlocksInX() {
    //     MapImageView v = new MapImageView(0, 0, 100, 10, 0);
    //     MapImageView[][] layout = v.split(10);
    //     assertTrue(layout.length == 1);
    //     assertTrue(layout[0].length == 10);

    //     v = new MapImageView(0, 0, 101, 9, 0);
    //     layout = v.split(10);
    //     assertTrue(layout.length == 1);
    //     assertTrue(layout[0].length == 11);
    // }

    // @Test
    // public void split_thinMap_multipleBlocksInY() {
    //     MapImageView v = new MapImageView(0, 0, 10, 100, 0);
    //     MapImageView[][] layout = v.split(10);
    //     assertTrue(layout.length == 10);
    //     assertTrue(layout[0].length == 1);

    //     v = new MapImageView(0, 0, 9, 101, 0);
    //     layout = v.split(10);
    //     assertTrue(layout.length == 11);
    //     assertTrue(layout[0].length == 1);
    // }

    @Test
    public void getGeoCoordinates() {
        double mlon = 0;
        double mlat = 0;
        int w = 100;
        int h = 100;
        int z = 0;
        boolean x2 = false;
        MapImageView v = new MapImageView(mlon, mlat, w, h, z, x2);
        double x = 50;
        double y = 50;
        double[] ll = v.getGeoCoordinates(x, y);
        double[] xy = v.getPixelCoordinates(ll[0], ll[1]);
        assertEquals(xy[0], x, 0.0001);
        assertEquals(xy[1], y, 0.0001);
        assertEquals(ll[0], mlon, 0.0001);
        assertEquals(ll[1], mlat, 0.0001);
    }

    @Test
    public void getPixelCoordinates() {
        double mlon = 0;
        double mlat = 0;
        int w = 100;
        int h = 100;
        int z = 0;
        boolean x2 = false;
        MapImageView v = new MapImageView(mlon, mlat, w, h, z, x2);
        double lon = 0;
        double lat = 0;
        double[] xy = v.getPixelCoordinates(lon, lat);
        double[] ll = v.getGeoCoordinates(xy[0], xy[1]);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 50, 0.0001);
        assertEquals(xy[1], 50, 0.0001);

        z = 10;
        v = new MapImageView(mlon, mlat, w, h, z, x2);
        lon = 170;
        lat = -80;
        xy = v.getPixelCoordinates(lon, lat);
        ll = v.getGeoCoordinates(xy[0], xy[1]);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assert(xy[0] > w);
        assert(xy[1] > h);

        lon = -170;
        lat = 80;
        xy = v.getPixelCoordinates(lon, lat);
        ll = v.getGeoCoordinates(xy[0], xy[1]);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assert(xy[0] < w);
        assert(xy[1] < h);

        mlon = 166.12332;
        mlat = -59.034323;
        w = 1234;
        h = 54321;
        z = 21;
        v = new MapImageView(mlon, mlat, w, h, z, x2);
        lon = 166.19;
        lat = -60.100001;
        xy = v.getPixelCoordinates(lon, lat);
        ll = v.getGeoCoordinates(xy[0], xy[1]);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
    }

    @Test
    public void getGeoCoordinates_global() {
        double x = 9899136;
        double y = 1816135296;
        int zoom = 22;
        boolean x2 = false;
        double[] lonlat = MapImageView.getGeoCoordinates_global(x, y, zoom, x2);
        double[] xy = MapImageView.getPixelCoordinates_global(lonlat[0], lonlat[1], zoom, x2);
        assertEquals(x, xy[0], 0.0001);
        assertEquals(y, xy[1], 0.0001);

        x = 0;
        y = 0;
        lonlat = MapImageView.getGeoCoordinates_global(x, y, zoom, x2);
        xy = MapImageView.getPixelCoordinates_global(lonlat[0], lonlat[1], zoom, x2);
        assertEquals(x, xy[0], 0.0001);
        assertEquals(y, xy[1], 0.0001);
        assertEquals(lonlat[0], -180, 0.0001);
        assertEquals(lonlat[1], MapImageView.LATITUDE_BOUND, 0.0001);
    }

    @Test
    public void getPixelCoordinates_global() {
        double lon = 0;
        double lat = 0;
        int zoom = 0;
        boolean x2 = false;
        double[] xy = MapImageView.getPixelCoordinates_global(lon, lat, zoom, x2);
        double[] ll = MapImageView.getGeoCoordinates_global(xy[0], xy[1], zoom, x2);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 256, 0.0001);
        assertEquals(xy[1], 256, 0.0001);

        lon = 0;
        lat = 0;
        zoom = 0;
        x2 = true;
        xy = MapImageView.getPixelCoordinates_global(lon, lat, zoom, x2);
        ll = MapImageView.getGeoCoordinates_global(xy[0], xy[1], zoom, x2);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 512, 0.0001);
        assertEquals(xy[1], 512, 0.0001);

        lon = -180;
        lat = MapImageView.LATITUDE_BOUND;
        zoom = 0;
        xy = MapImageView.getPixelCoordinates_global(lon, lat, zoom, x2);
        ll = MapImageView.getGeoCoordinates_global(xy[0], xy[1], zoom, x2);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 0, 0.0001);
        assertEquals(xy[1], 0, 0.0001);

        lon = 180 - 0.00000001;
        lat = -MapImageView.LATITUDE_BOUND;
        zoom = 0;
        xy = MapImageView.getPixelCoordinates_global(lon, lat, zoom, x2);
        ll = MapImageView.getGeoCoordinates_global(xy[0], xy[1], zoom, x2);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 1024, 0.0001);
        assertEquals(xy[1], 1024, 0.0001);
    }

    // @Test
    // public void getGeoBounds_fromPxBounds() {
    //     double w = -18.36914062;
    //     double s = 32.99023556;
    //     double e = 56.33789063;
    //     double n = 60.06484046;
    //     int z = 5;
    //     boolean highQ = true;
    //     MapImageView view = new MapImageView(w, s, e, n, z, highQ);
    //     BasicImage img = BasicImage.load("../test_europe_full.png");

    //     int[] pxBs = new int[]{3326, 195, 3344, 207};
    //     double[] wsen = view.getGeoBounds(pxBs);
    //     System.out.println(Arrays.toString(wsen));

    //     //img.drawBounds(wsen);
    // }
}
