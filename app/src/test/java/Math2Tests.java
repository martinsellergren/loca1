import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.util.LinkedList;

public class Math2Tests {

    @Test
    public void length() {
        double[] v = new double[]{1, 0};
        assertEquals(Math2.length(v), 1, 0.0001);

        v = new double[]{1, 1};
        assertEquals(Math2.length(v), Math.sqrt(2), 0.0001);

        v = new double[]{0, 0};
        assertEquals(Math2.length(v), 0, 0.0001);

        v = new double[]{-1, 1};
        assertEquals(Math2.length(v), Math.sqrt(2), 0.0001);

        v = new double[]{10, 10};
        assertEquals(Math2.length(v), Math.sqrt(200), 0.0001);

        int[] vv = new int[]{-1, -1};
        assertEquals(Math2.length(vv), Math.sqrt(2), 0.0001);
    }

    @Test
    public void distance() {
        double[] p1 = new double[]{1,1};
        double[] p2 = new double[]{-1,-1};
        assertEquals(Math2.distance(p1,p2), Math.sqrt(8), 0.0001);
        p2 = new double[]{1,1};
        assertEquals(Math2.distance(p1,p2), 0, 0.0001);
        p2 = new double[]{1,0};
        assertEquals(Math2.distance(p1,p2), 1, 0.0001);
        p2 = new double[]{2,1};
        assertEquals(Math2.distance(p1,p2), 1, 0.0001);
    }

    @Test
    public void angle_horizontal() {
        double[] v = new double[]{1, 0};
        assertEquals(0, Math2.angle(v), 0.0001);
        v = new double[]{5, 5};
        assertEquals(-45, Math2.angle(v), 0.0001);
        v = new double[]{0, 0};
        assertEquals(Math2.angle(v), 0, 0.0001);
        v = new double[]{-1, 1};
        assertEquals(Math2.angle(v), -135, 0.0001);
        v = new double[]{-1, 0};
        assertEquals(Math2.angle(v), -180, 0.0001);
        v = new double[]{0, -100000};
        assertEquals(Math2.angle(v), 90, 0.0001);
    }

    @Test
    public void angle_between() {
        double[] v1 = new double[]{1, 0};
        double[] v2 = new double[]{0, 1};
        assertEquals(270, Math2.angle(v1, v2), 0.0001);
        assertEquals(90, Math2.angle(v2, v1), 0.0001);

        v1 = new double[]{1, 0};
        v2 = new double[]{-1, 0};
        assertEquals(180, Math2.angle(v1, v2), 0.0001);
        assertEquals(180, Math2.angle(v2, v1), 0.0001);

        v1 = new double[]{1, 0};
        v2 = new double[]{0, -1};
        assertEquals(90, Math2.angle(v1, v2), 0.0001);
        assertEquals(270, Math2.angle(v2, v1), 0.0001);

        v1 = new double[]{1, 0};
        v2 = new double[]{1, 0};
        assertEquals(0, Math2.angle(v1, v2), 0.0001);
        assertEquals(0, Math2.angle(v2, v1), 0.0001);
    }

    @Test
    public void toUnitDegrees() {
        double deg = -540;
        double expected = -180;
        deg = Math2.toUnitDegrees(deg);
        assertEquals(deg, expected, 0.0001);

        deg = 0;
        expected = 0;
        deg = Math2.toUnitDegrees(deg);
        assertEquals(deg, expected, 0.0001);

        deg = 181;
        expected = -179;
        deg = Math2.toUnitDegrees(deg);
        assertEquals(deg, expected, 0.0001);

        deg = 180;
        expected = -180;
        deg = Math2.toUnitDegrees(deg);
        assertEquals(deg, expected, 0.0001);

        deg = -180;
        expected = -180;
        deg = Math2.toUnitDegrees(deg);
        assertEquals(deg, expected, 0.0001);
    }

    @Test
    public void normalize() {
        double[] v = new double[]{1,0};
        double[] n = Math2.normalize(v);
        assertEquals(n[0], 1, 0.0001);
        assertEquals(n[1], 0, 0.0001);

        v = new double[]{0,0};
        n = Math2.normalize(v);
        assertEquals(n[0], 0, 0.0001);
        assertEquals(n[1], 0, 0.0001);

        v = new double[]{3232,-1543};
        n = Math2.normalize(v);
        assertEquals(Math2.length(n), Math.sqrt(1), 0.0001);
    }

    @Test
    public void rotate() {
        double[] v = new double[]{1, 0};
        double[] r = Math2.rotate(v, 90);
        assertEquals(Math2.dot(v, r), 0, 0.0001);
        assertEquals(0, r[0], 0.0001);
        assertEquals(-1, r[1], 0.0001);

        v = new double[]{1, 1};
        r = Math2.rotate(v, 180);
        assertEquals(-1, r[0], 0.0001);
        assertEquals(-1, r[1], 0.0001);

        v = new double[]{1, 1};
        r = Math2.rotate(v, -180);
        assertEquals(-1, r[0], 0.0001);
        assertEquals(-1, r[1], 0.0001);

        v = new double[]{1, 1};
        r = Math2.rotate(v, 360);
        assertEquals(1, r[0], 0.0001);
        assertEquals(1, r[1], 0.0001);

        v = new double[]{-5435, 223};
        r = Math2.rotate(v, -90);
        assertEquals(Math2.dot(v, r), 0, 0.0001);

        v = new double[]{1, 1};
        r = Math2.rotate(v, -90);
        assertEquals(-1, r[0], 0.0001);
        assertEquals(1, r[1], 0.0001);

        v = new double[]{1, 1};
        r = Math2.rotate(v, 90);
        assertEquals(1, r[0], 0.0001);
        assertEquals(-1, r[1], 0.0001);
    }

    @Test
    public void angleDiff() {
        assertEquals(20, Math2.angleDiff(170, 150), 0.0001);
        assertEquals(20, Math2.angleDiff(10, -10), 0.0001);
        assertEquals(20, Math2.angleDiff(-70, -90), 0.0001);
        assertEquals(20, Math2.angleDiff(170, -170), 0.0001);
        assertEquals(0, Math2.angleDiff(180, -180), 0.0001);
    }

    @Test
    public void same_contains() {
        double delta = 0.1;
        assertTrue(Math2.same(new double[]{0, 0}, new double[]{0, 0}, delta));
        assertFalse(Math2.same(new double[]{0, 0}, new double[]{0, 1}, delta));
        assertTrue(Math2.same(new double[]{0, 0}, new double[]{0, 0.1}, delta));

        delta = 1;
        assertTrue(Math2.same(new int[]{0, 0}, new int[]{0, 0}));
        assertFalse(Math2.same(new int[]{0, 0}, new int[]{1, 1}));
        assertTrue(Math2.same(new int[]{0, 0}, new int[]{0, 0}));

        delta = 0.1;
        double[][] ps = new double[][]{new double[]{0, 0},
                                       new double[]{1, 1},
                                       new double[]{2, 1}};
        assertTrue(Math2.contains(new double[]{1, 1}, ps, delta));
        assertFalse(Math2.contains(new double[]{1, 2}, ps, delta));

        double[][] qs = new double[][]{new double[]{1.01, 1},
                                       new double[]{2, 1},
                                       new double[]{0, 0}};
        assertTrue(Math2.same(ps, qs, delta));
        assertTrue(Math2.same(qs, ps, delta));

        assertTrue(Math2.contains(new int[]{0, 0}, new int[][]{new int[]{2, 1}, new int[]{0, 0}, new int[]{1, 0}}));
        assertFalse(Math2.contains(new int[]{0, 0}, new int[][]{new int[]{2, 1}, new int[]{0, 1}, new int[]{1, 0}}));
    }

    @Test
    public void getFurthest() {
        int[] p = new int[]{0, 0};
        int[] p0 = new int[]{1, 1};
        int[] p1 = new int[]{0, 2};
        int[] p2 = new int[]{1, 0};
        assertEquals(1, Math2.getFurthest(p, new int[][]{p0,p1,p2}));
    }

    @Test
    public void getComplement() {
        int[] p0 = new int[]{0, 0};
        int[] p1 = new int[]{1, 0};
        int[] p2 = new int[]{0, 1};
        int[] p3 = new int[]{1, 1};
        assertArrayEquals(new int[]{0,3}, Math2.getComplement(new int[][]{p1,p2}, new int[][]{p0,p1,p2,p3}));
    }

    @Test
    public void sum_mean() {
        int[] p0 = new int[]{0, 0};
        int[] p1 = new int[]{1, 0};
        int[] p2 = new int[]{0, 1};
        int[] p3 = new int[]{1, 2};
        int[][] ps = new int[][]{p0, p1, p2, p3};
        LinkedList<int[]> l = new LinkedList<int[]>();
        l.add(p0); l.add(p1); l.add(p2); l.add(p3);

        assertEquals(2, Math2.sum(l)[0]);
        assertEquals(3, Math2.sum(l)[1]);

        double[] mean = new double[]{2/4d, 3/4d};
        assertEquals(mean[0], Math2.mean(ps)[0], 0.0001);
        assertEquals(mean[1], Math2.mean(ps)[1], 0.0001);
        assertEquals(mean[0], Math2.mean(l)[0], 0.0001);
        assertEquals(mean[1], Math2.mean(l)[1], 0.0001);
    }
}
