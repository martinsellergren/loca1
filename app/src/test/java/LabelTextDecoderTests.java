import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;
import java.util.LinkedList;

public class LabelTextDecoderTests {
    // MapRequest.ViewAndImgs vis = MapRequest.sweden();

    // @BeforeClass
    // public static void setUp() throws IOException {
    //     LabelTextDecoder.init();
    //     CategoryDecoder.init();
    // }

    // @Test
    // public void drawSingleAnalysisGrid() throws IOException {
    //     LabelLayoutIterator iter = new LabelLayoutIterator(imgs[2].getOneImage());
    //     LabelLayout lay = iter.expandToLabelLayout(new int[]{924, 1516});
    //     Box[] bs = lay.getBoxes();
    //     Box b = bs[5];
    //     BasicImage img = imgs[1].getOneImage();
    //     Box[] blocks = b.split(LabelTextDecoder.CODE_BOX_ROWS, LabelTextDecoder.CODE_BOX_COLS);

    //     for (Box block : blocks) {
    //         img.drawBox(block);
    //     }

    //     img.save("test_CodeFontDecoder_drawAnalysisGrid_code.png");
    //     imgs[0].save("test_CodeFontDecoder_drawAnalysisGrid_full.png");
    // }

    // @Test
    // public void drawAllAnalysisGrids() throws IOException {
    //     LinkedList<LabelLayout> lays = LabelLayoutIterator.getLayouts(vis.imgs[2], vis.view);
    //     BasicImage cimg = vis.imgs[1].getOneImage();

    //     for (LabelLayout lay : lays) {
    //         Box[] bs = lay.getBoxes();

    //         for (Box b : bs) {
    //             Box[] blocks = b.split(LabelTextDecoder.CODE_BOX_ROWS, LabelTextDecoder.CODE_BOX_COLS);

    //             for (Box block : blocks) {
    //                 cimg.drawBox(block);
    //             }
    //         }
    //     }

    //     cimg.save("test_CodeFontDecoder_drawAnalysisGrid_code.png");
    //     vis.imgs[0].save("test_CodeFontDecoder_drawAnalysisGrid_full.png");
    // }


    // @Test
    // public void printMappings() {
    //     for (int cp : LabelTextDecoder.mappings) {
    //         System.out.println(new String(Character.toChars(cp)).charAt(0));
    //     }
    //     System.out.println("Count: " + LabelTextDecoder.mappings.length);
    // }
}
