import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.awt.Color;
import java.io.IOException;

public class CategoryTests {

    @Test
    public void getAvgColor() throws IOException {
        MapImageView v = MapImageView.sweden();
        TiledImage[] imgs = new MapRequest(v, "test_Category_getAvgColor", Language.EN).fetch3();

        LabelLayoutIterator iter = new LabelLayoutIterator(imgs[2].getOneImage());

        LabelLayout lay;
        while ((lay=iter.next()) != null) {
            Color c = Category.getAvgColor(lay, imgs[2]);
            System.out.println(c);
        }
    }
}
