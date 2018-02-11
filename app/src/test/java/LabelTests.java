import org.junit.*;
import static org.junit.Assert.*;
import map.*;

public class LabelTests {

    // @Test
    // public void detectLabel() {
    //     BasicImage boxImg = BasicImage.load("../test_europe_box.png");
    //     BasicImage labelImg = BasicImage.load("../test_europe_label.png");
    //     BasicImage labelImgCpy = labelImg.copy();

    //     LabelLayoutIterator iter = new LabelLayoutIterator(boxImg);
    //     OCR ocr = new OCR(OCR.Language.eng);

    //     LabelLayout lay = iter.expandToLabelLayout(new int[]{1773,298});
    //     Label l = new Label(lay, labelImg, ocr);

    //     labelImgCpy.drawLabelLayout(lay);
    //     labelImgCpy.drawLabelText(l.getText().toUpperCase(), lay);

    //     ocr.end();
    //     labelImg.extractLabel(lay).save("test_Label.png");
    //     labelImgCpy.save("test_Label_detectAllLabels.png");
    // }

    @Test
    public void detectAllLabels() {
        BasicImage boxImg = BasicImage.load("../test_europe_box.png");
        BasicImage labelImg = BasicImage.load("../test_europe_label.png");
        LabelLayoutIterator iter = new LabelLayoutIterator(boxImg);
        OCR ocr = new OCR(OCR.Language.eng);

        LabelLayout lay;
        while ((lay=iter.next()) != null) {
            try {
                Label l = new Label(lay, labelImg, ocr);
                labelImg.drawLabelLayout(lay);
                labelImg.drawLabelText(l.getText(), lay);
            }
            catch (Exception e) {
                System.out.println(lay);
                throw e;
            }
        }

        ocr.end();
        labelImg.save("test_Label_detectAllLabels.png");
    }
}
