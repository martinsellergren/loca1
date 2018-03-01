import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;

public class PlacesTests {
    TiledImage limg;
    TiledImage bimg;
    MapImageView view;
    Language lang;
    Places places;

    @Before
    public void setUp() {
        limg = TiledImageTests.loader("../sweden/label");
        bimg = TiledImageTests.loader("../sweden/box");
        double w = 9.887695;
        double n = 69.446949;
        double e = 23.862305;
        double s = 55.336956;
        int z = 5;
        view = new MapImageView(w, n, e, s, z, true);
        lang = Language.ENG;
        places = new Places(limg, bimg, view, lang);
    }

    @Test
    public void construction() {
        BasicImage dump = limg.getOneImage();

        for (Place p : places.getPlaces()) {
            dump.drawPlace(p);
        }

        dump.save("test_PlacesTests_construction.png");
    }
}
