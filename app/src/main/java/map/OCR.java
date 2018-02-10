package map;

import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;

import java.io.File;

/**
 * Static class for turning image of letters into chars.
 */
public class OCR {

    /**
     * @return String in image.
     */
    public static String detectString(BasicImage img) {
        BytePointer outText;

        TessBaseAPI api = new TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init(".", "ENG") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }

        // Open input image with leptonica library
        String fn = "temp.png";
        img.save(fn);
        PIX image = pixRead(fn);
        new File(fn).delete();

        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        String text = outText.getString();

        // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image);

        return text;
    }
}
