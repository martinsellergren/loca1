import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import map.*;
import java.io.IOException;
import java.util.LinkedList;

public class PlaceTests {

    OCR ocr;
    LabelLayoutIterator iter;
    TiledImage labelImg;
    MapImageView view;

    @Before
    public void setUp() throws IOException {
        double west = 9.887695;
        double south = 55.336956;
        double east = 23.862305;
        double north = 69.446949;
        int z = 5;
        boolean x2 = true;
        this.view = new MapImageView(west, south, east, north, z, x2);
        //TiledImage fullImg = TiledImage.load("../sweden/full");
        this.labelImg = TiledImage.load("../sweden/label");
        TiledImage boxImg = TiledImage.load("../sweden/box");
        this.iter = new LabelLayoutIterator(boxImg.getOneImage());
        this.ocr = new OCR();
    }

    @After
    public void tearDown() {
        this.ocr.end();
    }

    // @Test
    // public void construction() {
    //     int[] pBalticSea = new int[]{876, 2594};
    //     assertEquals(Category.SEA, findCategory(pBalticSea));

    //     int[] pKalmar = new int[]{582, 2579};
    //     assertEquals(Category.CITY, findCategory(pKalmar));

    //     int[] pSthlm = new int[]{745, 2168};
    //     assertEquals(Category.CITY, findCategory(pSthlm));
    // }

    private Category findCategory(int[] p) {
        LabelLayout lay = iter.expandToLabelLayout(p);
        String name = ocr.detectString(labelImg.extractLabel(lay));
        LinkedList<LabelLayout> list = new LinkedList<LabelLayout>();
        list.add(lay);

        try {
            Place place = new Place(name, list, view);
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
