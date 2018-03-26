import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;
import java.util.LinkedList;

import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class MapShapeImageTests {
    static Language lang = Language.LOCAL;
    static MapImageView view = MapImageView.lonEdge();
    static Shape sh = new Shape(view.getGeoBounds());

    @BeforeClass
    public static void init() {

        String lonEdge = "[[-223.9453125, 79.3026396], [-242.578125, -38.2726885], [-109.6875, -45.5832898], [-210.9375, 31.6533814], [-84.0234375, 65.2198939], [-223.9453125, 79.3026396]]";

        String sweden = "[[20.0170898, 69.1625579], [13.9306641, 66.7051687], [11.3818359, 62.1038825], [10.7666016, 59.2434148], [12.0410156, 56.8729957], [12.9199219, 55.4040698], [14.0625, 55.3791104], [14.5019531, 56.0474996], [15.8422852, 56.10881], [16.6552734, 56.3409012], [19.0283203, 57.5276217], [19.5776367, 58.1127144], [18.984375, 59.8889369], [18.7646484, 60.3269477], [17.3583984, 60.9090733], [17.2705078, 62.3087937], [19.9731445, 63.6462592], [21.4453125, 64.4538495], [21.1157227, 64.8676078], [22.5878906, 65.8747247], [24.0380859, 65.8477677], [23.6206055, 67.3144515], [22.5, 68.4960402], [20.8081055, 69.1156111], [20.0170898, 69.1625579]]";

        String luthagen = "[[17.6502228, 59.9066726], [17.6069641, 59.8887647], [17.5873947, 59.869296], [17.5915146, 59.8561957], [17.598381, 59.8386055], [17.6131439, 59.8254931], [17.6028442, 59.8080593], [17.6086807, 59.7961439], [17.6258469, 59.7864703], [17.6471329, 59.7856064], [17.6646423, 59.7952803], [17.6670456, 59.8118575], [17.6560593, 59.8342928], [17.6546001, 59.8413222], [17.6662731, 59.8445562], [17.6845551, 59.8448149], [17.7123642, 59.8417103], [17.7309036, 59.837743], [17.7326202, 59.8537819], [17.707901, 59.8705024], [17.6818085, 59.8842862], [17.6651573, 59.8945341], [17.6502228, 59.9066726]]";


        String json = luthagen;

        LinkedList<double[]> ps = new LinkedList<double[]>();
        JsonArray ja = new JsonParser().parse(json).getAsJsonArray();
        for (int i = 0; i < ja.size(); i++) {
            JsonArray pJ = ja.get(i).getAsJsonArray();
            double[] p = new double[]{ pJ.get(0).getAsDouble(),
                                       pJ.get(1).getAsDouble() };
            ps.add(p);
        }

        sh = new Shape(ps);
    }


    //@Test
    public void wrappers() throws IOException {
        MapShapeImage msimg = MapShapeImage.fetchInit(sh, lang);
        msimg.getAssembledImg().save("test_MapImage_wrappers_0.png");

        int N = 3;
        for (int i = 1; i <= N; i++) {
            msimg = MapShapeImage.fetchNext(msimg);
            if (msimg == null) {
                System.out.println("Done!");
                return;
            }
            msimg.getAssembledImg().save("test_MapImage_wrappers_" + i + ".png");
        }
    }
}
