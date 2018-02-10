import org.junit.Test;
import static org.junit.Assert.*;
import map.*;

public class OCRTests {

    @Test
    public void detectString() {
        BasicImage label = BasicImage.load("../label.png");
        String text = OCR.detectString(label).trim();
        System.out.println(text);
        assertEquals("Balearic sea", text);
    }
}
