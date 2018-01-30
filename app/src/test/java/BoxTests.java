import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import map.*;
import java.util.Arrays;

public class BoxTests {


    // @Test
    // public void constructor_cornerPoints() {
    //     int[] tl = new int[]{0, 0};
    //     int[] tr = new int[]{1, 1};
    //     int[] br = new int[]{0, 2};
    //     int[] bl = new int[]{-1, 1};
    //     new Box(tl, tr, br, bl);
    // }

    @Test
    public void getRotation() {
        double[] topL = new double[]{0,0};
        double[] topR = new double[]{1,1};
        double h = 1;
        Box b = new Box(topL, topR, h);
        assertEquals(-45, b.getRotation(), 0.0001);

        topL = new double[]{0,0};
        topR = new double[]{-1,1};
        h = 1;
        b = new Box(topL, topR, h);
        assertEquals(-135, b.getRotation(), 0.0001);
    }

    @Test
    public void getBottomRight() {
        double[] topL = new double[]{0,0};
        double[] topR = new double[]{10,10};
        double h = Math.sqrt(200);
        Box b = new Box(topL, topR, h);
        double[] br = b.getBottomRight();
        assertEquals(0, br[0], 0.000001);
        assertEquals(20, br[1], 0.000001);
    }

    @Test
    public void getMid() {
        double[] topL = new double[]{0,0};
        double[] topR = new double[]{10,0};
        double h = 10;
        Box b = new Box(topL, topR, h);
        double[] m = b.getMid();
        assertEquals(5, m[0], 0.000001);
        assertEquals(5, m[1], 0.000001);
    }

    @Test
    public void getBounds() {
        double[] topL = new double[]{0,0};
        double[] topR = new double[]{10,10};
        double h = Math.sqrt(200);
        Box bx = new Box(topL, topR, h);
        double[] bs = bx.getBounds();
        assertEquals(-10, bs[0], 0.000001);
        assertEquals(0, bs[1], 0.000001);
        assertEquals(10, bs[2], 0.000001);
        assertEquals(20, bs[3], 0.000001);
    }

    @Test
    public void testGeneralBoxLayout_img() {
        BasicImage img = new BasicImage(100, 100);
        Box box = new Box(new double[]{10, 40}, new double[]{50, 10}, 60);
        img.drawBox(box);
        assertTrue(img != null);
        //img.save("test_element.png");
    }

    @Test
    public void addOffset() {
        Box b = new Box(new double[]{0,0}, new double[]{1,0}, 1);
        int[] bounds = Math2.toInt(b.getBounds());
        assertEquals(0, bounds[0]);
        assertEquals(0, bounds[1]);
        assertEquals(1, bounds[2]);
        assertEquals(1, bounds[3]);

        b.addOffset(1, 1);
        bounds = Math2.toInt(b.getBounds());
        assertEquals(1, bounds[0]);
        assertEquals(1, bounds[1]);
        assertEquals(2, bounds[2]);
        assertEquals(2, bounds[3]);

        b.addOffset(-1, -1);
        bounds = Math2.toInt(b.getBounds());
        assertEquals(0, bounds[0]);
        assertEquals(0, bounds[1]);
        assertEquals(1, bounds[2]);
        assertEquals(1, bounds[3]);

        //new
        b = new Box(new double[]{0,0}, new double[]{1,1}, 1);
        bounds = Math2.toInt(b.getBounds());
        assertEquals(-1, bounds[0]);
        assertEquals(0, bounds[1]);
        assertEquals(1, bounds[2]);
        assertEquals(2, bounds[3]);

        b.addOffset(1, 1);
        bounds = Math2.toInt(b.getBounds());
        assertEquals(0, bounds[0]);
        assertEquals(1, bounds[1]);
        assertEquals(2, bounds[2]);
        assertEquals(3, bounds[3]);

        //new
        b = new Box(new double[]{0,0}, new double[]{-1,-1}, 1);
        bounds = Math2.toInt(b.getBounds());
        assertEquals(-1, bounds[0]);
        assertEquals(-2, bounds[1]);
        assertEquals(1, bounds[2]);
        assertEquals(0, bounds[3]);

        b.addOffset(1, 1);
        bounds = Math2.toInt(b.getBounds());
        assertEquals(0, bounds[0]);
        assertEquals(-1, bounds[1]);
        assertEquals(2, bounds[2]);
        assertEquals(1, bounds[3]);
    }
}
