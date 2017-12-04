import org.junit.Test;
import static org.junit.Assert.*;
import map.MapBasics;
import java.awt.geom.Point2D;
import java.util.Arrays;

public class MapBasicsTests {

    @Test
    public void getCornerPoints() {
        MapBasics mb = new MapBasics(0, 0, 10, 10, 0);
        Point2D.Double[] cps = mb.getCornerPoints();
        assertTrue(cps[0].equals(new Point2D.Double(-5, -5)));
        assertTrue(cps[1].equals(new Point2D.Double(5, -5)));
        assertTrue(cps[2].equals(new Point2D.Double(5, 5)));
        assertTrue(cps[3].equals(new Point2D.Double(-5, 5)));
    }

    @Test
    public void split_bigMap_multipleBlocks() {
        MapBasics mb = new MapBasics(0, 0, 100, 90, 0);
        MapBasics[][] layout = mb.split(10);
        assertTrue(layout.length == 9);
        assertTrue(layout[0].length == 10);
        assertTrue(layout[3][3].x == -15);
        assertTrue(layout[3][3].y == -10);
        assertTrue(layout[7][3].x == -15);
        assertTrue(layout[7][3].y == 30);

        mb = new MapBasics(0, 0, 101, 91, 0);
        layout = mb.split(10);
        assertTrue(layout.length == 10);
        assertTrue(layout[0].length == 11);

        assertTrue(layout[9][10].width == 1);
        assertTrue(layout[9][10].height == 1);
        assertTrue(layout[9][10].x == 50);
        assertTrue(layout[9][10].y == 45);
    }

    @Test
    public void split_smallMap_1x1() {
        MapBasics mb = new MapBasics(12345, 54321, 10, 10, 0);
        MapBasics[][] layout = mb.split(10);
        assertEquals(layout.length, 1);
        assertEquals(layout[0].length, 1);
        assertTrue(Math.abs(layout[0][0].x - 12345) < 0.000001);
        assertTrue(Math.abs(layout[0][0].y - 54321) < 0.000001);
        assertEquals(layout[0][0].width, 10);
        assertEquals(layout[0][0].height, 10);

        mb = new MapBasics(12345, 54321, 1, 1, 0);
        layout = mb.split(10);
        assertEquals(layout.length, 1);
        assertEquals(layout[0].length, 1);
        assertTrue(Math.abs(layout[0][0].x - 12345) < 0.000001);
        assertTrue(Math.abs(layout[0][0].y - 54321) < 0.000001);
        assertEquals(layout[0][0].width, 1);
        assertEquals(layout[0][0].height, 1);
    }

    @Test
    public void split_shortMap_multipleBlocksInX() {
        MapBasics mb = new MapBasics(0, 0, 100, 10, 0);
        MapBasics[][] layout = mb.split(10);
        assertTrue(layout.length == 1);
        assertTrue(layout[0].length == 10);

        mb = new MapBasics(0, 0, 101, 9, 0);
        layout = mb.split(10);
        assertTrue(layout.length == 1);
        assertTrue(layout[0].length == 11);
    }

    @Test
    public void split_thinMap_multipleBlocksInY() {
        MapBasics mb = new MapBasics(0, 0, 10, 100, 0);
        MapBasics[][] layout = mb.split(10);
        assertTrue(layout.length == 10);
        assertTrue(layout[0].length == 1);

        mb = new MapBasics(0, 0, 9, 101, 0);
        layout = mb.split(10);
        assertTrue(layout.length == 11);
        assertTrue(layout[0].length == 1);
    }
}
