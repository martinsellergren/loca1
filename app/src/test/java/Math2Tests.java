import org.junit.Test;
import static org.junit.Assert.*;
import map.*;

public class Math2Tests {
    @Test
    public void toUnitDegrees() {
        double deg = -540;
        double expected = -180;
        deg = Math2.toUnitDegrees(deg);
        assert(deg == expected);

        deg = 0;
        expected = 0;
        deg = Math2.toUnitDegrees(deg);
        assert(deg == expected);

        deg = 181;
        expected = -179;
        deg = Math2.toUnitDegrees(deg);
        assert(deg == expected);

        deg = 180;
        expected = -180;
        deg = Math2.toUnitDegrees(deg);
        assert(deg == expected);

        deg = -180;
        expected = -180;
        deg = Math2.toUnitDegrees(deg);
        assert(deg == expected);
    }
}
