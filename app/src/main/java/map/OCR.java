package map;

import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;

import java.io.File;

/**
 * Class for turning images of letters strings.
 */
public class OCR {
    private TessBaseAPI api = new TessBaseAPI();
    public enum Language { eng, swe; }

    /**
     * Initializes the ocr-engine for specified language detection.
     *
     * @param l Language for this ocr-engine.
     */
    public OCR(Language l) {
        if (api.Init(".", l.name()) != 0) {
            throw new RuntimeException("Could not initialize tesseract.");
        }
        api.SetVariable("tessedit_char_whitelist", " abcdefghijklmnopqrstuvwxyz");
    }

    /**
     * Close down engine and free up memory.
     */
    public void end() {
        api.End();
    }

    /**
     * @return String in image.
     */
    public String detectString(BasicImage img) {
        String fn = "temp.png";
        img.save(fn);
        PIX image = pixRead(fn);
        new File(fn).delete();

        api.SetImage(image);
        api.SetSourceResolution(70);

        // Get OCR result
        BytePointer outText = api.GetUTF8Text();
        String text = outText.getString();

        outText.deallocate();
        pixDestroy(image);

        return text;
    }
}
