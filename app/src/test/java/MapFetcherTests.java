import org.junit.Test;
import static org.junit.Assert.*;
import map.MapBasics;

public class MapFetcherTests {

    @Test
    public void fetchRawImage_widthHeight1280_fetched() {
        MapBasics mb = new MapBasics(0, 0, 1280, 1280, 0);
        //System.out.println(mb);
    }
}
