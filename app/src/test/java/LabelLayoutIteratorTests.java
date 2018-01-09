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
        BasicImage img = new BasicImage(100, 100);
        Graphics2D g = img.createGraphics();
        g.setPaint(Color.BLACK);
        g.fillRect(10, 10, 80, 80);
        //img.save("test_LabelLayoutIterator_before.png");
        LabelLayoutIterator iter = new LabelLayoutIterator(img);
        //iter.toImg().save("test_LabelLayoutIterator_after.png");
    }
}
