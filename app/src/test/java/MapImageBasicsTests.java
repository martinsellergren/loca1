import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import map.*;

public class MapImageBasicsTests {

    // @Test
    // public void getCornerPoints() {
    //     MapBasics mb = new MapBasics(0, 0, 10, 10, 0);
    //     Point2D.Double[] cps = mb.getCornerPoints();
    //     assertTrue(cps[0].equals(new Point2D.Double(-5, -5)));
    //     assertTrue(cps[1].equals(new Point2D.Double(5, -5)));
    //     assertTrue(cps[2].equals(new Point2D.Double(5, 5)));
    //     assertTrue(cps[3].equals(new Point2D.Double(-5, 5)));
    // }

    @Test
    public void split_bigMap_multipleBlocks() {
        MapImageBasics b = new MapImageBasics(0, 0, 100, 90, 0);
        MapImageBasics[][] layout = b.split(10);
        assertTrue(layout.length == 9);
        assertTrue(layout[0].length == 10);
        double[] xy = b.getPixelCoordinates(layout[3][3].lon, layout[3][3].lat);
        assertEquals(xy[0], 35, 0.0001);
        assertEquals(xy[1], 35, 0.0001);
        xy = b.getPixelCoordinates(layout[7][3].lon, layout[7][3].lat);
        assertEquals(xy[0], 35, 0.0001);
        assertEquals(xy[1], 75, 0.0001);

        b = new MapImageBasics(0, 0, 101, 91, 0);
        layout = b.split(10);
        assertTrue(layout.length == 10);
        assertTrue(layout[0].length == 11);
        xy = b.getPixelCoordinates(layout[3][3].lon, layout[3][3].lat);
        assertEquals(xy[0], 35, 0.0001);
        assertEquals(xy[1], 35, 0.0001);
        xy = b.getPixelCoordinates(layout[7][3].lon, layout[7][3].lat);
        assertEquals(xy[0], 35, 0.0001);
        assertEquals(xy[1], 75, 0.0001);

        assertTrue(layout[9][10].width == 1);
        assertTrue(layout[9][10].height == 1);
        xy = b.getPixelCoordinates(layout[9][10].lon, layout[9][10].lat);
        assertEquals(xy[0], 100.5, 0.0001);
        assertEquals(xy[1], 90.5, 0.0001);
    }

    // @Test
    // public void split_smallMap_1x1() {
    //     MapBasics mb = new MapBasics(12345, 54321, 10, 10, 0);
    //     MapBasics[][] layout = mb.split(10);
    //     assertEquals(layout.length, 1);
    //     assertEquals(layout[0].length, 1);
    //     assertTrue(Math.abs(layout[0][0].x - 12345) < 0.000001);
    //     assertTrue(Math.abs(layout[0][0].y - 54321) < 0.000001);
    //     assertEquals(layout[0][0].width, 10);
    //     assertEquals(layout[0][0].height, 10);

    //     mb = new MapBasics(12345, 54321, 1, 1, 0);
    //     layout = mb.split(10);
    //     assertEquals(layout.length, 1);
    //     assertEquals(layout[0].length, 1);
    //     assertTrue(Math.abs(layout[0][0].x - 12345) < 0.000001);
    //     assertTrue(Math.abs(layout[0][0].y - 54321) < 0.000001);
    //     assertEquals(layout[0][0].width, 1);
    //     assertEquals(layout[0][0].height, 1);
    // }

    // @Test
    // public void split_shortMap_multipleBlocksInX() {
    //     MapBasics mb = new MapBasics(0, 0, 100, 10, 0);
    //     MapBasics[][] layout = mb.split(10);
    //     assertTrue(layout.length == 1);
    //     assertTrue(layout[0].length == 10);

    //     mb = new MapBasics(0, 0, 101, 9, 0);
    //     layout = mb.split(10);
    //     assertTrue(layout.length == 1);
    //     assertTrue(layout[0].length == 11);
    // }

    // @Test
    // public void split_thinMap_multipleBlocksInY() {
    //     MapBasics mb = new MapBasics(0, 0, 10, 100, 0);
    //     MapBasics[][] layout = mb.split(10);
    //     assertTrue(layout.length == 10);
    //     assertTrue(layout[0].length == 1);

    //     mb = new MapBasics(0, 0, 9, 101, 0);
    //     layout = mb.split(10);
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
        MapImageBasics b = new MapImageBasics(mlon, mlat, w, h, z);
        double x = 50;
        double y = 50;
        double[] ll = b.getGeoCoordinates(x, y);
        double[] xy = b.getPixelCoordinates(ll[0], ll[1]);
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
        MapImageBasics b = new MapImageBasics(mlon, mlat, w, h, z);
        double lon = 0;
        double lat = 0;
        double[] xy = b.getPixelCoordinates(lon, lat);
        double[] ll = b.getGeoCoordinates(xy[0], xy[1]);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 50, 0.0001);
        assertEquals(xy[1], 50, 0.0001);

        z = 10;
        b = new MapImageBasics(mlon, mlat, w, h, z);
        lon = 170;
        lat = -80;
        xy = b.getPixelCoordinates(lon, lat);
        ll = b.getGeoCoordinates(xy[0], xy[1]);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assert(xy[0] > w);
        assert(xy[1] > h);

        lon = -170;
        lat = 80;
        xy = b.getPixelCoordinates(lon, lat);
        ll = b.getGeoCoordinates(xy[0], xy[1]);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assert(xy[0] < w);
        assert(xy[1] < h);

        mlon = 166.12332;
        mlat = -59.034323;
        w = 1234;
        h = 54321;
        z = 21;
        b = new MapImageBasics(mlon, mlat, w, h, z);
        lon = 166.19;
        lat = -60.100001;
        xy = b.getPixelCoordinates(lon, lat);
        ll = b.getGeoCoordinates(xy[0], xy[1]);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
    }

    @Test
    public void getGeoCoordinates_global() {
        double x = 9899136;
        double y = 1816135296;
        int zoom = 22;
        int tileSize = 512;
        double[] lonlat = MapImageBasics.getGeoCoordinates_global(x, y, zoom, tileSize);
        double[] xy = MapImageBasics.getPixelCoordinates_global(lonlat[0], lonlat[1], zoom, tileSize);
        assertEquals(x, xy[0], 0.0001);
        assertEquals(y, xy[1], 0.0001);

        x = 0;
        y = 0;
        lonlat = MapImageBasics.getGeoCoordinates_global(x, y, zoom, tileSize);
        xy = MapImageBasics.getPixelCoordinates_global(lonlat[0], lonlat[1], zoom, tileSize);
        assertEquals(x, xy[0], 0.0001);
        assertEquals(y, xy[1], 0.0001);
        assertEquals(lonlat[0], -180, 0.0001);
        assertEquals(lonlat[1], MapImageBasics.LATITUDE_BOUND, 0.0001);

        x = 99.999999;
        y = 100;
        tileSize = 100;
        zoom = 0;
        lonlat = MapImageBasics.getGeoCoordinates_global(x, y, zoom, tileSize);
        xy = MapImageBasics.getPixelCoordinates_global(lonlat[0], lonlat[1], zoom, tileSize);
        assertEquals(x, xy[0], 0.0001);
        assertEquals(y, xy[1], 0.0001);
        assertEquals(lonlat[0], 179.9999999, 0.0001);
        assertEquals(lonlat[1], -MapImageBasics.LATITUDE_BOUND, 0.0001);

        x = 500;
        y = 500;
        tileSize = 1000;
        zoom = 0;
        lonlat = MapImageBasics.getGeoCoordinates_global(x, y, zoom, tileSize);
        xy = MapImageBasics.getPixelCoordinates_global(lonlat[0], lonlat[1], zoom, tileSize);
        assertEquals(x, xy[0], 0.0001);
        assertEquals(y, xy[1], 0.0001);
        assertEquals(lonlat[0], 0, 0.0001);
        assertEquals(lonlat[1], 0, 0.0001);
    }

    @Test
    public void getPixelCoordinates_global() {
        double lon = 0;
        double lat = 0;
        int zoom = 0;
        int tileSize = 256;
        double[] xy = MapImageBasics.getPixelCoordinates_global(lon, lat, zoom, tileSize);
        double[] ll = MapImageBasics.getGeoCoordinates_global(xy[0], xy[1], zoom, tileSize);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 128, 0.0001);
        assertEquals(xy[1], 128, 0.0001);

        lon = 0;
        lat = 0;
        zoom = 0;
        tileSize = 512;
        xy = MapImageBasics.getPixelCoordinates_global(lon, lat, zoom, tileSize);
        ll = MapImageBasics.getGeoCoordinates_global(xy[0], xy[1], zoom, tileSize);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 256, 0.0001);
        assertEquals(xy[1], 256, 0.0001);

        lon = -180;
        lat = MapImageBasics.LATITUDE_BOUND;
        zoom = 0;
        tileSize = 512;
        xy = MapImageBasics.getPixelCoordinates_global(lon, lat, zoom, tileSize);
        ll = MapImageBasics.getGeoCoordinates_global(xy[0], xy[1], zoom, tileSize);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 0, 0.0001);
        assertEquals(xy[1], 0, 0.0001);

        lon = 180 - 0.00000001;
        lat = -MapImageBasics.LATITUDE_BOUND;
        zoom = 0;
        tileSize = 512;
        xy = MapImageBasics.getPixelCoordinates_global(lon, lat, zoom, tileSize);
        ll = MapImageBasics.getGeoCoordinates_global(xy[0], xy[1], zoom, tileSize);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 512, 0.0001);
        assertEquals(xy[1], 512, 0.0001);

        lon = -180;
        lat = -MapImageBasics.LATITUDE_BOUND;
        zoom = 29;
        tileSize = 1234;
        xy = MapImageBasics.getPixelCoordinates_global(lon, lat, zoom, tileSize);
        ll = MapImageBasics.getGeoCoordinates_global(xy[0], xy[1], zoom, tileSize);
        assertEquals(ll[0], lon, 0.0001);
        assertEquals(ll[1], lat, 0.0001);
        assertEquals(xy[0], 0, 0.0001);
        assertEquals(xy[1], 662498705408d, 0.001);
    }
}
