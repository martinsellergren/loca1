import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;

public class CategoryTests {

    // @Test
    // public void getAvgColor() throws IOException {
    //     MapImageView v = MapImageView.sweden();
    //     TiledImage[] imgs = new MapRequest(v, "test_Category_getAvgColor", Language.EN).fetch3();

    //     LinkedList<LabelLayout> lays = LabelLayoutIterator.getLayouts(imgs[2], v);

    //     for (LabelLayout lay : lays) {
    //         Color c = lay.getAverageColor(imgs[2]);
    //         System.out.println(c);
    //     }
    // }

    // @Test
    // public void decipherLabelTypes() throws IOException {
    //     ColorCategoryConversion ccc = new ColorCategoryConversion();
    //     OCR ocr = new OCR(Language.EN);

    //     MapImageView v = MapImageView.lidingo();
    //     TiledImage[] imgs = new MapRequest(v, "test_Category_decipherLabelTypes", Language.EN).fetch3();
    //     BasicImage full = imgs[0].getOneImage();

    //     LinkedList<LabelLayout> lays = LabelLayoutIterator.getLayouts(imgs[2], v);

    //     for (LabelLayout lay : lays) {
    //         Color avg = lay.getAverageColor(imgs[2]);
    //         String text = ocr.detectString(imgs[1].extractLabel(lay));

    //         System.out.println(text + " : " + avg);

    //         String labelType = ccc.getLabelType(avg);
    //         full.drawLabelLayout(lay);
    //         full.drawLabelString(labelType, lay, Color.RED);
    //     }

    //     full.save("test_Category_decipherLabelTypes.png");
    //     ocr.end();
    // }
}
