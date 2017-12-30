import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.util.Arrays;

public class BoxTests {

    @Test
    public void getRotation() {
        int[] topL = new int[]{0,0};
        int[] topR = new int[]{1,1};
        double h = 1;
        Box b = new Box(topL, topR, h);
        assertEquals(-45, b.getRotation(), 0.0001);

        topL = new int[]{0,0};
        topR = new int[]{-1,1};
        h = 1;
        b = new Box(topL, topR, h);
        assertEquals(-135, b.getRotation(), 0.0001);
    }

    @Test
    public void getBottomRight() {
        int[] topL = new int[]{0,0};
        int[] topR = new int[]{10,10};
        double h = Math.sqrt(200);
        Box b = new Box(topL, topR, h);
        int[] br = b.getBottomRight();
        assertEquals(0, br[0]);
        assertEquals(20, br[1]);
    }

    @Test
    public void getMid() {
        int[] topL = new int[]{0,0};
        int[] topR = new int[]{10,0};
        double h = 10;
        Box b = new Box(topL, topR, h);
        int[] m = b.getMid();
        assertEquals(5, m[0]);
        assertEquals(5, m[1]);
    }

    @Test
    public void getBounds() {
        int[] topL = new int[]{0,0};
        int[] topR = new int[]{10,10};
        double h = Math.sqrt(200);
        Box bx = new Box(topL, topR, h);
        int[] bs = bx.getBounds();
        assertEquals(-10, bs[0]);
        assertEquals(0, bs[1]);
        assertEquals(10, bs[2]);
        assertEquals(20, bs[3]);
    }

    // @Test
    // public void getDirAndOrtoVector() {
    //     int[] topL = new int[]{0,0};
    //     int[] topR = new int[]{10,10};
    //     double h = Math.sqrt(200);
    //     Box bx = new Box(topL, topR, h);
    //     double[] dirV = bx.getDirVector();
    //     double[] ortoV = bx.getOrtoDirVector();

    //     System.out.println(Arrays.toString(dirV));
    //     System.out.println(Arrays.toString(ortoV));
    // }
}
