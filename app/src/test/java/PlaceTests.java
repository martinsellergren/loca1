import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import map.*;
import java.io.IOException;
import java.util.LinkedList;

public class PlaceTests {

    Language lang = Language.EN;
    String path;
    OCR ocr;
    LabelLayoutIterator iter;
    TiledImage labelImg;
    MapImageView view;

    // @Before
    // public void setUp() throws IOException {
    //     this.path = "lonEdge";
    //     double west = 177;
    //     double south = 54;
    //     double east = -168;
    //     double north = 66;
    //     int z = 3;
    //     boolean x2 = true;
    //     this.view = new MapImageView(west, south, east, north, z, x2);
    //     //TiledImage fullImg = TiledImage.load(path + "/full");
    //     this.labelImg = TiledImage.load(path + "/label");
    //     TiledImage boxImg = TiledImage.load(path + "/box");
    //     this.iter = new LabelLayoutIterator(boxImg.getOneImage());
    //     this.ocr = new OCR();
    // }

    // @After
    // public void tearDown() {
    //     this.ocr.end();
    // }

    // @Test
    // public void construction() {
    //     // sweden
    //     // int[] pBalticSea = new int[]{876, 2594};
    //     // assertEquals(Category.SEA, findCategory(pBalticSea));

    //     // int[] pKalmar = new int[]{582, 2579};
    //     // assertEquals(Category.CITY, findCategory(pKalmar));

    //     // int[] pSthlm = new int[]{745, 2168};
    //     // assertEquals(Category.CITY, findCategory(pSthlm));

    //     //lonEdge
    //     int[] pBalticSea = new int[]{164, 393};
    //     assertEquals(Category.SEA, findCategory(pBalticSea));
    // }

    private Category findCategory(int[] p) {
        try {
            LabelLayout lay = iter.expandToLabelLayout(p);
            String text = ocr.detectString(labelImg.extractLabel(lay));
            Label lab = new Label(text, lay);

            Place place = new Place(lab, view, lang);
            return place.getCategory();
        }
        catch (PlaceQuery.UnknownPlaceException e) {
            System.out.print("Unknown place:\n" + e.getMessage());
            assertTrue(false);
        }
        catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        System.exit(1);
        return null;
    }

}
