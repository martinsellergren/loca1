import org.junit.Test;
import static org.junit.Assert.*;
import map.*;

public class BoxTests {

    @Test
    public void getRotation() {
        int[] topL = new int[]{0,0};
        int[] topR = new int[]{1,1};
        double h = 1;
        Box b = new Box(topL, topR, h);
        assertEquals(45, b.getRotation(), 0.0001);

        topL = new int[]{0,0};
        topR = new int[]{-1,1};
        h = 1;
        b = new Box(topL, topR, h);
        assertEquals(135, b.getRotation(), 0.0001);
    }

}
