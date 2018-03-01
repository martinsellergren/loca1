import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.awt.Color;

public class TiledImageTests {

    // TiledImage img = loader(Paths.get("../sweden/full"));
    // TiledImage img1x = buildOneTile15x10Image();

    // @Test
    // public void load() {
    //     img.save("test_TiledImageTests_build.png");
    //     img1x.save("test_TiledImageTests_build_1x.png");
    // }

    // @Test
    // public void dims() {
    //     assertEquals(img1x.getWidth(), 15);
    //     assertEquals(img1x.getHeight(), 10);
    // }

    // @Test
    // public void getSubImage() {
    //     int w = img.getWidth();
    //     int h = img.getHeight();

    //     BasicImage sub = img.getSubImage(new int[]{0, 0, w-1, h-1});
    //     assertEquals(w, sub.getWidth());
    //     assertEquals(h, sub.getHeight());

    //     sub = img.getSubImage(new int[]{0, 0, w, h});
    //     assertEquals(w, sub.getWidth());
    //     assertEquals(h, sub.getHeight());

    //     sub = img.getSubImage(new int[]{0, 0, 0, 0});
    //     assertEquals(1, sub.getWidth());
    //     assertEquals(1, sub.getHeight());

    //     sub = img.getSubImage(new int[]{10, 10, 10, 10});
    //     assertEquals(1, sub.getWidth());
    //     assertEquals(1, sub.getHeight());

    //     sub = img.getSubImage(new int[]{w-2, h-2, w+2, h+2});
    //     assertEquals(2, sub.getWidth());
    //     assertEquals(2, sub.getHeight());

    //     //1x-img
    //     sub = img1x.getSubImage(new int[]{-1000,-1000,1000,1000});
    //     assertEquals(15, sub.getWidth());
    //     assertEquals(10, sub.getHeight());

    //     sub = img1x.getSubImage(new int[]{5, 4, 10, 9});
    //     assertEquals(6, sub.getWidth());
    //     assertEquals(6, sub.getHeight());
    // }

    public static TiledImage loader(Path dir) {
        try {
            return TiledImage.load(dir);
        }
        catch(IOException e) {
            e.printStackTrace();
            assertTrue(false);
            System.exit(-1);
            return null;
        }
    }
    public static TiledImage loader(String dir) {
        return loader(Paths.get(dir));
    }

    public static TiledImage buildOneTile15x10Image() {
        BasicImage img = new BasicImage(15, 10);
        img.color(Color.BLUE);
        TiledImage.Builder b = new TiledImage.Builder(1, 1, Paths.get("test_TiledImageTests_OneTileImage"));
        b.add(img);

        return b.build();
    }
}
