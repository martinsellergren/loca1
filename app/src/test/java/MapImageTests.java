import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;

public class MapImageTests {

    boolean FETCH;
    boolean FETCH_CATEGORIES; //ignored if FETCH
    Language lang = Language.EN;

    @Before
    public void setup() {
        FETCH = true;
        FETCH_CATEGORIES = true;
    }

    @Test
    public void construction() throws IOException {
        FETCH = true;
        FETCH_CATEGORIES = true;

        // constructAndDump(MapImageView.world(), "world");
        // constructAndDump(MapImageView.europe(), "europe");
        // constructAndDump(MapImageView.sweden(), "sweden");
        // constructAndDump(MapImageView.uppsala(), "uppsala");
        // constructAndDump(MapImageView.luthagen(), "luthagen");
        // constructAndDump(MapImageView.lidingo(), "lidingo");
        // constructAndDump(MapImageView.rudboda(), "rudboda");
        // constructAndDump(MapImageView.mefjard(), "mefjard");
        // constructAndDump(MapImageView.lonEdge(), "lonEdge");
    }

    // @Test
    // public void fuzz() throws IOException {
    //     while (true) {
    //         MapImageView v = MapImageView.randomize();
    //         System.out.println(v);
    //         constructAndDump(v);
    //         System.out.println("\n");
    //     }
    // }

    public/***/ void constructAndDump(MapImageView v, String name) throws IOException {
        MapImage mimg;

        if (FETCH) {
            mimg = new MapImage(v, lang);
        }
        else {
            MapRequest.ViewAndImgs vis = new MapRequest.ViewAndImgs(name, v);
            if (FETCH_CATEGORIES) {
                mimg = new MapImage(vis.imgs, vis.view, lang);
            }
            else {
                mimg = new MapImage(vis.imgs, vis.view, lang, null);
            }
        }

        BasicImage dump = mimg.getImg();

        dump.save("test_" + name + "_before.png");
        for (Place p : mimg.getPlaces()) {
            dump.drawPlace(p);
        }
        dump.save("test_" + name + "_after.png");
    }
    public/***/ void constructAndDump(MapImageView v) throws IOException {
        constructAndDump(v, "test_" + v.toString());
    }

}
