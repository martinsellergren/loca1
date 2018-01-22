import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.util.LinkedList;
import java.util.Arrays;
import java.awt.Graphics2D;
import java.awt.Color;

public class LabelLayoutIteratorTests {

    @Test
    public void constructor() {
        BasicImage img = BasicImage.load("../test_box.png");
        LabelLayoutIterator iter = new LabelLayoutIterator(img);
        iter.toImg().save("test_LabelLayoutIterator_constructor.png");
    }

    // @Test
    // public void isBoxPoint() {
    //     BasicImage img = BasicImage.load("../test_box_small.png");
    //     int[] bp = new int[]{2136, 1156};
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img);
    // }

    // @Test
    // public void expandToBoxPoints_findCorners() {
    //     BasicImage img = BasicImage.load("../test_box.png");
    //     int[] bp = new int[]{858, 261};
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img);
    //     img = iter.toImg();
    //     LinkedList<int[]> bps = iter.expandToBoxPoints(bp);
    //     //LabelLayoutIterator.toImg(bps).save("test_LabelLayoutIterator_expandToBoxPoints.png");

    //     int[][] cs = iter.findCorners(bps);
    //     img.drawPoints(cs);
    //     img.save("test_LabelLayoutIterator_findCorners.png");
    // }

    @Test
    public void displayAllCorners() {
        BasicImage img = BasicImage.load("../test_box.png");
        LabelLayoutIterator iter = new LabelLayoutIterator(img);

        for (int y = 0; y < img.getHeight()-1; y++) {
            for (int x = 0; x < img.getWidth()-1; x++) {
                try {
                    if (iter.isBoxPoint(x, y)) {
                        LinkedList<int[]> bps = iter.expandToBoxPoints(new int[]{x,y});

                        int[][] cs = iter.findCorners(bps);
                        if (cs != null) {

                            // cs = iter.orderByDirection(cs, bps);
                            // System.out.println(Arrays.deepToString(cs));
                            // System.out.println("");

                            img.drawPoints(cs);
                        }

                        iter.expandAndRemove(new int[]{x,y});
                    }
                }
                catch (Exception e) {
                    System.out.println(x + ", " + y);
                    throw e;
                }
            }
        }
        img.save("test_LabelLayoutIterator_displayAllCorners.png");
    }

    // @Test
    // public void expandToBox() {
    //     BasicImage img = BasicImage.load("../test_box.png");
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img);

    //     int[] bp = new int[]{227, 2};
    //     Box box = iter.expandToBox(bp);
    //     //img.drawBox(box);

    //     LinkedList<int[]> ps = iter.expandToBoxPoints(bp);
    //     int[][] cs = iter.orderByDirection(iter.findCorners(ps), ps);
    //     img.drawPoints(cs);

    //     img.save("test_LabelLayoutIterator_expandToBox_1.png");

    //     // BasicImage img2 = iter.toImg();
    //     // img2.drawBox(b);
    //     // img2.save("test_LabelLayoutIterator_expandToBox_2.png");
    // }
}