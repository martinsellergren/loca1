import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;

public class CategoryDecoderTests {
    // MapRequest.ViewAndImgs vis = MapRequest.lidingo();

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
    // public void findCategoryOfAllLabels() throws UnknownCategoryException, IOException {
    //     BasicImage full = vis.imgs[0].getOneImage();
    //     LinkedList<LabelLayout> lays = LabelLayoutIterator.getLayouts(vis.imgs[2], vis.view);

    //     for (LabelLayout lay : lays) {
    //         Color c = lay.getAverageColor(vis.imgs[2]);
    //         String labelType = CategoryDecoder.getLabelType(c);
    //         full.drawLabelString(labelType, lay, Color.RED);
    //     }

    //     full.save("test_CategoryDecoder_findCategoryOfAllLabels.png");
    // }

    // @Test
    // public void categoryFromColor() throws UnknownCategoryException, IOException {
    //     Color c = new Color(0, 0, 0);
    //     String labelType = CategoryDecoder.getLabelType(c);
    //     System.out.println(labelType);
    //     //assertTrue(labelType.equals("country_label : country"));
    // }
}
