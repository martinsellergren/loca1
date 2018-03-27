import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;
import java.util.LinkedList;

public class LabelTextDecoderTests {
    //MapRequest.ViewAndImgs vis = MapRequest.sweden();
    static BasicImage fimg;
    static BasicImage cimg;
    static BasicImage bimg;

    // @BeforeClass
    // public static void setUp() throws IOException {
    //     LabelTextDecoder.init();

    //     fimg = BasicImage.load("tile-2-1-full.png");
    //     cimg = BasicImage.load("tile-2-1-code.png");
    //     bimg = BasicImage.load("tile-2-1-box.png");
    // }

    // @Test
    // public void drawSingleAnalysisGrid() throws IOException {
    //     LabelLayoutIterator iter = new LabelLayoutIterator(bimg);
    //     LabelLayout lay = iter.expandToLabelLayout(new int[]{999, 701});
    //     Box[] bs = lay.getBoxes();
    //     Box b = bs[9];
    //     BasicImage cimg_ = cimg.copy();
    //     Box[] blocks = b.split(LabelTextDecoder.CODE_BOX_ROWS, LabelTextDecoder.CODE_BOX_COLS);

    //     for (Box block : blocks) {
    //         cimg_.drawBox(block);
    //     }

    //     cimg_.save("test_CodeFontDecoder_drawAnalysisGrid_code.png");
    //     fimg.save("test_CodeFontDecoder_drawAnalysisGrid_full.png");
    // }

    // @Test
    // public void drawAllAnalysisGrids() throws IOException {
    //     LabelLayoutIterator iter = new LabelLayoutIterator(bimg);
    //     LabelLayout lay;
    //     BasicImage cimg_ = cimg.copy();

    //     while ((lay=iter.next()) != null) {
    //         Box[] bs = lay.getBoxes();

    //         for (Box b : bs) {
    //             Box[] blocks = b.split(LabelTextDecoder.CODE_BOX_ROWS, LabelTextDecoder.CODE_BOX_COLS);

    //             for (Box block : blocks) {
    //                 cimg_.drawBox(block);
    //             }

    //             try {
    //                 System.out.println(LabelTextDecoder.decode(lay, cimg));
    //             }catch (Exception e) {
    //                 System.out.println(e);
    //             }
    //         }
    //     }

    //     cimg_.save("test_CodeFontDecoder_drawAnalysisGrid_code.png");
    //     fimg.save("test_CodeFontDecoder_drawAnalysisGrid_full.png");
    // }


    // @Test
    // public void printMappings() {
    //     for (int cp : LabelTextDecoder.mappings) {
    //         System.out.println(new String(Character.toChars(cp)).charAt(0));
    //     }
    //     System.out.println("Count: " + LabelTextDecoder.mappings.length);
    // }
}
