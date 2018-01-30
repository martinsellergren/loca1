import org.junit.Test;
import static org.junit.Assert.*;
import map.*;
import java.util.LinkedList;
import java.util.Arrays;

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

        assertArrayEquals(new int[]{1,3,5}, Math2.getComplement(new int[]{0,2,4}, new int[]{0,1,2,3,4,5}));
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

    @Test
    public void getMostOccuring() {
        LinkedList<int[]> ps = new LinkedList<int[]>();
        ps.add(new int[]{0, 1});
        ps.add(new int[]{1, 2});
        ps.add(new int[]{0, 1});
        ps.add(new int[]{2, 3});

        int[] actual = Math2.getMostOccuring(ps);
        assertEquals(0, actual[0]);
        assertEquals(1, actual[1]);

        ps.add(new int[]{2, 3});
        ps.add(new int[]{2, 3});
        actual = Math2.getMostOccuring(ps);
        assertEquals(2, actual[0]);
        assertEquals(3, actual[1]);
    }

    @Test
    public void getCrossLength() {
        assertEquals(Math.sqrt(2)*2, Math2.getCrossLength(new int[][]{
                    new int[]{0,0},
                    new int[]{1,0},
                    new int[]{0,1},
                    new int[]{1,1}}),
            0.0001);
    }

    @Test
    public void removeIndexes() {
        LinkedList<Integer> l = new LinkedList<Integer>();
        l.add(0); l.add(1); l.add(2); l.add(3); l.add(4); l.add(5);
        Math2.removeIndexes(new int[]{1,3,5}, l);
        assertEquals(0, (int)l.get(0));
        assertEquals(2, (int)l.get(1));
        assertEquals(4, (int)l.get(2));
    }

    @Test
    public void rightTurn() {
        int[] p0 = new int[]{0,0};
        int[] p1 = new int[]{1,0};
        int[] p2 = new int[]{1,1};
        assertTrue(Math2.rightTurn(p0, p1, p2));
    }

    @Test
    public void getDirVector() {
        double deg = 37;
        double[] v = Math2.getDirVector(deg);
        assertEquals(deg, Math2.angle(v), 0.000001);
        assertEquals(1, Math2.length(v), 0.000001);

        deg = 179;
        v = Math2.getDirVector(deg);
        assertEquals(deg, Math2.angle(v), 0.000001);
        assertEquals(1, Math2.length(v), 0.000001);

        deg = -90;
        v = Math2.getDirVector(deg);
        assertEquals(deg, Math2.angle(v), 0.000001);
        assertEquals(1, Math2.length(v), 0.000001);

        deg = 0;
        v = Math2.getDirVector(deg);
        assertEquals(deg, Math2.angle(v), 0.000001);
        assertEquals(1, Math2.length(v), 0.000001);
    }

    @Test
    public void intersectPoint() {
        double[] p0 = new double[]{0, 0};
        double[] v0 = Math2.minus(new double[]{1, 0}, p0);
        double[] p1 = new double[]{1, 1};
        double[] v1 = Math2.minus(new double[]{1, -1}, p1);
        double[] ip = Math2.intersectPoint(p0, v0, p1, v1);
        assertEquals(1, ip[0], 0.000001);
        assertEquals(0, ip[1], 0.000001);

        p0 = new double[]{123.321, 321.123};
        v0 = Math2.minus(new double[]{567.765, 678.876}, p0);
        p1 = new double[]{987.789, 876.678};
        v1 = Math2.minus(new double[]{765.567, 654.456}, p1);
        ip = Math2.intersectPoint(p0, v0, p1, v1);
        assertEquals(1707.04, ip[0], 0.01);
        assertEquals(1595.93, ip[1], 0.01);

        p0 = new double[]{1, 3};
        v0 = Math2.minus(new double[]{-1, 5}, p0);
        p1 = new double[]{11, 31};
        v1 = Math2.minus(new double[]{-11, 51}, p1);
        ip = Math2.intersectPoint(p0, v0, p1, v1);
        assertEquals(-407, ip[0], 0.0000001);
        assertEquals(411, ip[1], 0.0000001);
    }

    @Test
    public void solve() {
        double[][] A = new double[][]{
            new double[]{1,3},
            new double[]{5,7}};
        double[] b = new double[]{1,2};
        double[] x = Math2.solve(A, b);
        assertEquals(-1/8d, x[0], 0.00001);
        assertEquals(3/8d, x[1], 0.00001);

        A = new double[][]{
            new double[]{123.321, -234.432},
            new double[]{-345.543, 456.654}};
        b = new double[]{123,-321};
        x = Math2.solve(A, b);
        assertEquals(0.772913, x[0], 0.00001);
        assertEquals(-0.118088, x[1], 0.00001);
    }
}
