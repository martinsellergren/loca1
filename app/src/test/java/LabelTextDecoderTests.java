import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;

public class LabelTextDecoderTests {
    TiledImage[] imgs = MapRequest.sweden().imgs;

    @BeforeClass
    public static void setUp() throws IOException {
        LabelTextDecoder.init();
        CategoryDecoder.init();
    }

    @Test
    public void drawAnalysisGrid() throws IOException {
        LabelLayoutIterator iter = new LabelLayoutIterator(imgs[2].getOneImage());
        LabelLayout lay = iter.expandToLabelLayout(new int[]{924, 1516});
        Box[] bs = lay.getBoxes();
        Box b = bs[5];
        BasicImage img = imgs[1].getOneImage();
        Box[] blocks = b.split(LabelTextDecoder.CODE_BOX_ROWS, LabelTextDecoder.CODE_BOX_COLS);

        for (Box block : blocks) {
            img.drawBox(block);
        }

        img.save("test_CodeFontDecoder_drawAnalysisGrid_code.png");
        imgs[0].save("test_CodeFontDecoder_drawAnalysisGrid_full.png");
    }
}
