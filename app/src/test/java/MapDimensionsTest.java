import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import map.*;

public class MapDimensionsTest {

    @Test
    public void getBounds() {
        boolean retina = false;
        int tileSize = 256;

        double[] mid = new double[] {0, 0};
        int[] pxDims = new int[] {200, 200};
        int zoom = 0;

        // double[] bounds = MapDimensions.getBounds(mid, zoom, pxDims, 256);
        // System.out.println(Arrays.toString(bounds));


        double[] dims = MapDimensions.getDegreeDimensions(mid, zoom, pxDims, retina);
        System.out.println(Arrays.toString(dims));

        mid = new double[] {0, 80};
        dims = MapDimensions.getDegreeDimensions(mid, zoom, pxDims, retina);
        System.out.println(Arrays.toString(dims));

    }
}
