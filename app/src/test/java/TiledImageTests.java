import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class TiledImageTests {

    // TiledImage img = loader(Paths.get("../sweden"));

    // @Test
    // public void load() {
    //     img.save("test_TiledImageTests_build.png");
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
}
