import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.util.LinkedList;
import java.util.Arrays;

public class LabelLayoutTests {
    LabelLayout lay;

    @Before
    public void setUp() {
        Box b1 = new Box(new int[]{0,0}, new int[]{1,1}, 1);
        Box b2 = b1.copy();
        Box b3 = b1.copy();
        LinkedList<Box> r1 = new LinkedList<Box>();
        r1.add(b1);
        r1.add(b2);
        r1.add(b3);

        b1 = new Box(new int[]{0,0}, new int[]{-1,-1}, 1);
        b2 = b1.copy();
        b3 = b1.copy();
        LinkedList<Box> r0 = new LinkedList<Box>();
        r0.add(b1);
        r0.add(b2);
        r0.add(b3);

        b1 = new Box(new int[]{0,0}, new int[]{10,10}, 1);
        b2 = b1.copy();
        b3 = b1.copy();
        LinkedList<Box> r2 = new LinkedList<Box>();
        r2.add(b1);
        r2.add(b2);
        //r2.add(b3);

        lay = new LabelLayout(r1);
        lay.addRowFirst(r0);
        lay.addRowLast(r2);
    }

    @Test
    public void getNoBoxes_getBoxes_getBox() {
        assertEquals(8, lay.getNoBoxes());

        Box b_r0 = new Box(new int[]{0,0}, new int[]{-1,-1}, 1);
        Box b_r1 = new Box(new int[]{0,0}, new int[]{1,1}, 1);
        Box b_r2 = new Box(new int[]{0,0}, new int[]{10,10}, 1);
        Box[] bs = lay.getBoxes();

        //getBoxes
        assertEquals(b_r0, bs[0]);
        assertEquals(b_r0, bs[1]);
        assertEquals(b_r0, bs[2]);
        assertEquals(b_r1, bs[3]);
        assertEquals(b_r1, bs[4]);
        assertEquals(b_r1, bs[5]);
        assertEquals(b_r2, bs[6]);
        assertEquals(b_r2, bs[7]);

        //getBox
        assertEquals(b_r0, lay.getBox(0,0));
        assertEquals(b_r0, lay.getBox(0,1));
        assertEquals(b_r0, lay.getBox(0,2));
        assertEquals(b_r1, lay.getBox(1,0));
        assertEquals(b_r1, lay.getBox(1,1));
        assertEquals(b_r1, lay.getBox(1,2));
        assertEquals(b_r2, lay.getBox(2,0));
        assertEquals(b_r2, lay.getBox(2,1));
    }

    // @Test
    // public void addRows_getNoRows() {
    //     Box b1 = new Box(new int[]{0,0}, new int[]{1,1}, 1);
    //     Box b2 = new Box(new int[]{0,0}, new int[]{1,1}, 1);
    //     Box b3 = new Box(new int[]{0,0}, new int[]{1,1}, 1);
    //     LinkedList<Box> r1 = new LinkedList<Box>();
    //     r1.add(b1);
    //     r1.add(b2);
    //     r1.add(b3);

    //     b1 = new Box(new int[]{0,0}, new int[]{1,1}, 1);
    //     b2 = new Box(new int[]{0,0}, new int[]{1,1}, 1);
    //     b3 = new Box(new int[]{0,0}, new int[]{1,1}, 1);
    //     LinkedList<Box> r0 = new LinkedList<Box>();
    //     r0.add(b1.copy());
    //     r0.add(b2.copy());
    //     r0.add(b3.copy());

    //     LinkedList<Box> r2 = new LinkedList<Box>();
    //     r1.add(b1.copy());
    //     r1.add(b2.copy());
    //     r1.add(b3.copy());

    //     LabelLayout lay = new LabelLayout(r1);
    //     lay.addRowFirst(r0);
    //     lay.addRowLast(r2);


    // }

}
