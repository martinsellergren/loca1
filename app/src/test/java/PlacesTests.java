import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;
import java.util.LinkedList;

public class PlacesTests {
    TiledImage limg;
    TiledImage bimg;
    MapImageView view;
    Language lang = Language.EN;
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
    //     lang = Language.EN;
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

    // @Test
    // public void splitIntoLabelLayoutAnalysisSize() throws IOException {
    //     //MapRequest.ViewAndImgs vis = MapRequest.lidingo();
    //     MapImageView view = MapImageView.lidingo().getExtendedView();
    //     TiledImage[] imgs = new MapRequest (view, ".", Language.EN).fetch3();
    //     TiledImage fimg = imgs[0];
    //     TiledImage bimg = imgs[2];

    //     int extTerm = view.getExtensionTerm();
    //     int[] imgBs = new int[]{0, 0, bimg.getWidth()-1, bimg.getHeight()-1};
    //     LinkedList<int[]> bss = Math2.split(imgBs, Places.LABEL_LAYOUT_ANALYSIS_SIZE);

    //     for (int[] bs : bss) {
    //         bs = Math2.extendBounds(bs, extTerm);
    //         BasicImage subFimg = fimg.getSubImage(bs);
    //         BasicImage subBimg = bimg.getSubImage(bs);
    //         subFimg.save("test_splitIntoLabelLayoutAnalysisSize_" + bs[0] + "," + bs[1] + "_full.png");
    //         subBimg.save("test_splitIntoLabelLayoutAnalysisSize_" + bs[0] + "," + bs[1] + "_box.png");
    //     }
    // }
}
