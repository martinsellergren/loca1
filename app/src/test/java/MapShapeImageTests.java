import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.io.IOException;

public class MapShapeImageTests {
    static Language lang = Language.LOCAL;
    static MapImageView view = MapImageView.luthagen();
    static Shape sh = new Shape(view.getGeoBounds());

    @BeforeClass
    public static void init() {
        double[][] ps = new double[][]{
            new double[]{17.601297,59.864718},
            new double[]{17.62181, 59.866959},
            new double[]{17.639062, 59.861012},
            new double[]{17.632282, 59.849547},
            new double[]{17.590654, 59.847478}};
        sh = new Shape(Math2.toList(ps));
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
