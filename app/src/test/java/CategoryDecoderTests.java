import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.awt.Color;
import java.io.IOException;

public class CategoryDecoderTests {
    // MapRequest.ViewAndImgs vis = MapRequest.sweden();

    // @BeforeClass
    // public static void setUp() throws IOException {
    //     LabelTextDecoder.init();
    //     CategoryDecoder.init();
    // }

    // @Test
    // public void findCategoryOfLabel() throws UnknownCategoryException, IOException {
    //     LabelLayoutIterator iter = new LabelLayoutIterator(vis.imgs[2].getOneImage());
    //     LabelLayout lay = iter.expandToLabelLayout(new int[]{617, 1072});
    //     Color c = lay.getAverageColor(vis.imgs[2]);
    //     String labelType = CategoryDecoder.getLabelType(c);
    //     System.out.println(labelType);
    // }

    // @Test
    // public void categoryFromColor() throws IOException {
    //     Color c = new Color(0, 0, 0);
    //     String labelType = CategoryDecoder.getLabelType(c);
    //     System.out.println(labelType);
    //     //assertTrue(labelType.equals("country_label : country"));
    // }
}
