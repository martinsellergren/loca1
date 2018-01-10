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
        img.save("test_LabelLayoutIterator_before.png");
        LabelLayoutIterator iter = new LabelLayoutIterator(img);
        iter.toImg().save("test_LabelLayoutIterator_after.png");
    }
}
