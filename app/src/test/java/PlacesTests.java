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

    // @Before
    // public void setUp() {
    //     TiledImageTests.loader("../sweden/full").getOneImage().save("test_PlacesTests_construction_full.png");
    //     limg = TiledImageTests.loader("../sweden/label");
    //     limg.getOneImage().save("test_PlacesTests_construction_label.png");
    //     bimg = TiledImageTests.loader("../sweden/box");
    //     bimg.getOneImage().save("test_PlacesTests_construction_box.png");

    //     double w = 9.887695;
    //     double s = 55.336956;
    //     double e = 23.862305;
    //     double n = 69.446949;
    //     int z = 5;
    //     view = new MapImageView(w, s, e, n, z, true);
    //     lang = Language.ENG;
    //     try {
    //         places = new Places.Builder(limg, bimg, view, lang).build();
    //     }
    //     catch (IOException ex) {
    //         ex.printStackTrace();
    //     }
    // }

    // @Test
    // public void construction() {
    //     BasicImage dump = limg.getOneImage();

    //     for (Place p : places.getPlaces()) {
    //         dump.drawPlace(p);
    //     }

    //     dump.save("test_PlacesTests_construction_after.png");
    // }
}
