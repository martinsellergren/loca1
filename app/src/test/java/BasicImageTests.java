import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

public class BasicImageTests {

    @Test
    public void extractElement() {
        BasicImage img = new BasicImage(100, 100);
        img.color(Color.WHITE);
        Box box = new Box(new int[]{10, 40}, new int[]{50, 10}, 60);
        img.drawBox(box);
        img.save("1orig.png");
        img.extractElement(box);
        img.save("1extracted.png");
    }

    @Test
    public void concatenateImages_2x2Layout_squares() {
        int w = 10;
        int h = 10;
        BasicImage img1 = new BasicImage(w, h);
        img1.color(Color.RED);
        BasicImage img2 = new BasicImage(w, h);
        img2.color(Color.GREEN);
        BasicImage img3 = new BasicImage(w, h);
        img3.color(Color.BLUE);
        BasicImage img4 = new BasicImage(w, h);
        img4.color(Color.YELLOW);

        BasicImage[][] imgs = new BasicImage[][]{{img1, img2}, {img3, img4}};
        BasicImage img = BasicImage.concatenateImages(imgs);
        assertEquals(img.getWidth(), 20);
        assertEquals(img.getHeight(), 20);

        //img.save("test_concatenateImages_2x2Layout_squares.png");
    }
}