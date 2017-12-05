import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import map.*;

public class MapDimensionsTest {

    @Test
    public void getDegreeDimensions() {
        double[] dims = MapDimensions.getDegreeDimensions(new double[] {-75.03, 35.25}, 14, new int[] {600, 400}, false);

        System.out.println(Arrays.toString(dims));
    }
}
