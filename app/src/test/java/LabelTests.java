import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;
import java.util.LinkedList;

public class LabelTests {
    MapRequest.ViewAndImgs vis = MapRequest.lidingo();

    @BeforeClass
    public static void setUp() throws IOException {
        LabelTextDecoder.init();
        CategoryDecoder.init();
    }

    @Test
    public void detectAllLabels() throws IOException {
        BasicImage full = vis.imgs[0].getOneImage();
        LinkedList<LabelLayout> lays = LabelLayoutIterator.getLayouts(vis.imgs[2], vis.view);

        for (LabelLayout lay : lays) {
            try {
                Label lab = new Label(lay, vis.imgs[1], vis.imgs[2]);
                full.drawLabel(lab);
            }
            catch (UnknownCharacterException e) {
                System.out.println(e);
            }
            catch (UnknownCategoryException e) {
                System.out.println(e);
            }
            catch (Label.JunkException e) {
                System.out.println(e);
            }
        }

        full.save("test_Label_detectAllLabels.png");
    }

    // @Test
    // public void detectLabel() throws UnknownCategoryException, IOException {
    //     LabelLayoutIterator iter = new LabelLayoutIterator(vis.imgs[2].getOneImage());
    //     LabelLayout lay = iter.expandToLabelLayout(new int[]{924, 1516});
    //     try {
    //         Label lab = new Label(lay, vis.imgs[1], vis.imgs[2]);
    //         BasicImage full = vis.imgs[0].getOneImage();
    //         full.drawLabel(lab);
    //         full.save("test_Label_detectAllLabels.png");
    //     }
    //     catch (Label.JunkException e) {
    //         e.printStackTrace();
    //     }
    // }
}
