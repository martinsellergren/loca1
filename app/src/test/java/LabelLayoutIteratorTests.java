import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.util.LinkedList;
import java.util.Arrays;
import java.awt.Graphics2D;
import java.awt.Color;

public class LabelLayoutIteratorTests {

    // @Test
    // public void constructor() {
    //     BasicImage img = BasicImage.load("../test_box.png");
    //     img.save("test_LabelLayoutIterator_before.png");
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img);
    //     iter.toImg().save("test_LabelLayoutIterator_after.png");
    // }

    @Test
    public void findBoxPoint() {
        BasicImage img = new BasicImage(100, 100);
        LabelLayoutIterator iter = new LabelLayoutIterator(img);
        assertEquals(null, iter.findBoxPoint());

        // full = BasicImage.load("../test_box.png");
        // BasicImage part = full.crop(367, 1773,
        // iter = new LabelLayoutIterator(img);
        // assertNotEquals(null, iter.findBoxPoint());
    }


}
