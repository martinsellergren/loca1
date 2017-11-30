import org.junit.Test;
import static org.junit.Assert.*;

public class TestTest2 {

    @Test
    public void testTest() {
        //System.out.println("ttteeesssttt");

        assert(1 == 1);
        assertTrue(1 == 1);
        assertFalse(1 == 2);
        assertEquals(1, 1);
    }
}
