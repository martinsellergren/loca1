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
    }

    @Test
    public void construction() {
        try {
            Places ps = new Places(limg, bimg, view, lang);
        }
        catch (IOException e) {
            assertTrue(false);
        }
    }



}
