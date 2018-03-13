import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;

public class LabelTests {
    TiledImage boxImg;
    TiledImage labelImg;

    @BeforeClass
    public void setUp() throws IOException {
        this.boxImg = TiledImage.load("../imgs/lidingo/box");
        this.labelImg = TiledImage.load("../imgs/lidingo/label");
    }

    // @Test
    // public void detectLabel() {
    //     LabelLayoutIterator iter = new LabelLayoutIterator(boxImg.getOneImage());
    //     OCR ocr = new OCR(Language.EN);

    //     LabelLayout lay = iter.expandToLabelLayout(new int[]{2564,336});
    //     String txt = ocr.detectString(labelImg.extractLabel(lay));
    //     Label l = new Label(txt, lay);

    //     BasicImage labelImg_ = labelImg.getOneImage();
    //     labelImg_.drawLabelLayout(lay);
    //     //labelImg_.drawLabelText(l.getText().toUpperCase(), lay);

    //     ocr.end();
    //     labelImg.extractLabel(lay).save("test_Label.png");
    //     labelImg.save("test_Label_detectAllLabels.png");
    // }

    // @Test
    // public void detectAllLabels() {
    //     LabelLayoutIterator iter = new LabelLayoutIterator(boxImg);
    //     OCR ocr = new OCR(OCR.Language.eng);

    //     LabelLayout lay;
    //     while ((lay=iter.next()) != null) {
    //         try {
    //             Label l = new Label(lay, labelImg, ocr);
    //             labelImg.drawLabelLayout(lay);
    //             labelImg.drawLabelText(l.getText(), lay);
    //         }
    //         catch (Exception e) {
    //             System.out.println(lay);
    //             throw e;
    //         }
    //     }

    //     ocr.end();
    //     labelImg.save("test_Label_detectAllLabels.png");
    // }
}
