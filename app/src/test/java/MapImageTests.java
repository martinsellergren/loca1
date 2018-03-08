import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;

public class MapImageTests {

    boolean FETCH = false;
    boolean FETCH_CATEGORIES = false; //ignored if FETCH
    Language lang = Language.ENG;

    @Test
    public void construction() throws IOException {
        // constructAndDump(MapImageView.world(), "world");
        // constructAndDump(MapImageView.europe(), "europe");
        // constructAndDump(MapImageView.sweden(), "sweden");
        // constructAndDump(MapImageView.uppsala(), "uppsala");
        constructAndDump(MapImageView.luthagen(), "luthagen");
        // constructAndDump(MapImageView.lidingo(), "lidingo");
        // constructAndDump(MapImageView.rudboda(), "rudboda");
        // constructAndDump(MapImageView.mefjard(), "mefjard");
        // constructAndDump(MapImageView.lonEdge(), "lonEdge");
    }

    private void constructAndDump(MapImageView v, String name) throws IOException {
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

}
