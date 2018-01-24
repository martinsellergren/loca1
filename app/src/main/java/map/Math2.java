package map;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

/**
 * Some additional math. 2D is assumed when applicable and angles
 * in degrees. X grows right, (NOTE:) y grows down.
 */
public class Math2 {

    /**
     * @return Length of vector.
     */
    public static double length(double[] v) {
        return Math.sqrt(Math.pow(v[0], 2) + Math.pow(v[1], 2));
    }
    public static double length(int[] v) {
        return length(toDouble(v));
    }

    /**
     * @return Distance between two points.
     */
    public static double distance(double[] p1, double[] p2) {
        return length(new double[]{p2[0]-p1[0], p2[1]-p1[1]});
    }
    public static double distance(int[] p1, int[] p2) {
        return distance(toDouble(p1), toDouble(p2));
    }

    /**
     * @return Angle with the horizontal axis, in degrees.
     */
    public static double angle(double[] v) {
        v = new double[]{v[0], v[1]};
        if (length(v) == 0) return 0;

        v[1] = -v[1];
        double a = Math.acos(v[0] / length(v));

        if (Math.asin(v[1] / length(v)) >= 0)
            return Math2.toUnitDegrees(Math.toDegrees(a));
        else
            return Math2.toUnitDegrees(Math.toDegrees(-a));
    }
    public static double angle(int[] v) {
        return angle(toDouble(v));
    }

    /**
     * @return Angle between two vectors, in degrees. Walk from
     * v1 to v2 counterclockwise - how many degrees? Always >= 0.
     */
    public static double angle(double[] v1, double[] v2) {
        return toPositiveDegrees(angle(v2) - angle(v1));
    }
    public static double angle(int[] v1, int[] v2) {
        return angle(toDouble(v1), toDouble(v2));
    }

    /**
     * Return absolute difference between two angles, in degrees.
     */
    public static double angleDiff(double a1, double a2) {
        a1 = toUnitDegrees(a1);
        a2 = toUnitDegrees(a2);
        double r1 = Math.abs(a1 - a2);
        double d1 = Math.min(Math.abs(a1 - 180), Math.abs(a1 + 180));
        double d2 = Math.min(Math.abs(a2 - 180), Math.abs(a2 + 180));
        double r2 = d1 + d2;
        return Math.min(r1, r2);
    }


    /**
     * Ands/removes 360 to an angle to make it inside [-180, 180).
     */
    public static double toUnitDegrees(double deg) {
        while (deg < -180) deg += 360;
        while (deg >= 180) deg -= 360;
        return deg;
    }

    /**
     * Add 360 until positive.
     */
    public static double toPositiveDegrees(double deg) {
        while (deg < 0) deg += 360;
        return deg;
    }

    /**
     * 2D addition.
     */
    public static double[] plus(double[] v1, double[] v2) {
        return new double[]{v1[0] + v2[0], v1[1] + v2[1]};
    }
    public static int[] plus(int[] v1, int[] v2) {
        return new int[]{v1[0] + v2[0], v1[1] + v2[1]};
    }

    /**
     * 2D subtraction.
     */
    public static double[] minus(double[] v1, double[] v2) {
        return new double[]{v1[0] - v2[0], v1[1] - v2[1]};
    }
    public static int[] minus(int[] v1, int[] v2) {
        return new int[]{v1[0] - v2[0], v1[1] - v2[1]};
    }

    /**
     * @return Normalized vector.
     */
    public static double[] normalize(double[] v) {
        double l = length(v);
        if (l == 0) return new double[]{0, 0};
        return new double[]{ v[0]/l, v[1]/l };
    }
    public static double[] normalize(int[] v) {
        return normalize(toDouble(v));
    }

    /**
     * @return Rotated vector (counter-clockwise).
     */
    public static double[] rotate(double[] v, double deg) {
        double rad = Math.toRadians(deg);
        double A[][] = new double[][] {
            new double[] { Math.cos(rad), Math.sin(rad) },
            new double[] { -Math.sin(rad), Math.cos(rad) }};

        return new double[] {A[0][0]*v[0] + A[0][1]*v[1],
                             A[1][0]*v[0] + A[1][1]*v[1] };
    }
    public static int[] rotate(int[] v, double deg) {
        return Math2.toInt(Math2.rotate(Math2.toDouble(v), deg));
    }

    /**
     * Rotate every point in list/array.
     */
    public static LinkedList<double[]> rotate(LinkedList<double[]> ps, double deg) {
        LinkedList<double[]> rps = new LinkedList<double[]>();
        for (double[] p : ps) rps.add(Math2.rotate(p, deg));
        return rps;
    }
    public static double[][] rotate(double[][] ps, double deg) {
        double[][] rps = new double[ps.length][2];
        int i = 0;
        for (double[] p : ps) rps[i++] = Math2.rotate(p, deg);
        return rps;
    }

    /**
     * @return Dot-product.
     */
    public static double dot(double[] v, double[] w) {
        return v[0]*w[0] + v[1]*w[1];
    }

    /**
     * @return Vector multiplied by scalar.
     */
    public static double[] scale(double[] v, double f) {
        return new double[]{v[0]*f, v[1]*f};
    }

    /**
     * @param start Start-pos.
     * @param dir Directional vector.
     * @param l Length of step along dir.
     * @param Pos [x,y] after the step.
     */
    public static double[] step(double[] start, double[] dir, double l) {
        dir = normalize(dir);
        return plus(start, scale(dir, l));
    }
    public static int[] step(int[] start, double[] dir, double l) {
        return toInt(step(toDouble(start), dir, l));
    }

    /**
     * @return True if p1 and p2 equal (within delta-distance).
     */
    public static boolean same(double[] p1, double[] p2, double delta) {
        return distance(p1, p2) <= delta;
    }
    public static boolean same(double[] p1, double[] p2) {
        return same(p1, p2, 0.000001);
    }

    /**
     * @return True if x and y (scalars) are equal
     * (within delta-distance).
     */
    public static boolean same(double x, double y, double delta) {
        return Math.abs(x - y) <= delta;
    }
    public static boolean same(double x, double y) {
        return same(x, y, 0.000001);
    }

    /**
     * @return True if p1 and p2 are same (by value).
     */
    public static boolean same(int[] p1, int[] p2) {
        return p1[0] == p2[0] && p1[1] == p2[1];
    }

    /**
     * @return True if ps1 and ps2 contains same points, where same
     * points mean they are within the delta-distance.
     */
    public static boolean same(double[][] ps1, double[][] ps2, double delta) {
        if (ps1.length != ps2.length) return false;
        for (double[] p : ps1) {
            if (!contains(p, ps2, delta)) return false;
        }
        return true;
    }

    /**
     * @return True if p is in ps, i.e same x/y-value (within
     * delta distance).
     */
    public static boolean contains(double[] p, double[][] ps, double delta) {
        for (double[] q : ps) {
            if (same(p, q, delta)) return true;
        }
        return false;
    }

    /**
     * @return true if ps contains p (by value).
     */
    public static boolean contains(int[] p, LinkedList<int[]> ps) {
        for (int[] q : ps) {
            if (same(p, q)) return true;
        }
        return false;
    }

    /**
     * @return True if p in ps (by value).
     */
    public static boolean contains(int[] p, int[][] ps) {
        for (int[] q : ps) {
            if (same(p, q)) return true;
        }
        return false;
    }

    /**
     * @return True if scalar in list.
     */
    public static boolean contains(int x, int[] xs) {
        for (int y : xs) {
            if (x == y) return true;
        }
        return false;
    }

    /**
     * @return True if list contains two equal (by value) points.
     */
    public static boolean hasDuplicates(LinkedList<int[]> ps) {
        for (int[] p : ps) {
            for (int[] q : ps) {
                if (p != q) {
                    if (same(p, q)) return true;
                }
            }
        }
        return false;
    }
    public static boolean hasDuplicates(int[][] ps) {
        return hasDuplicates(Math2.toList(ps));
    }

    /**
     * @return The point occuring most times in ps. If ties, return
     * random.
     */
    public static int[] getMostOccuring(LinkedList<int[]> ps) {
        int[] cardins = getCardinalities(ps);
        int maxi = getMaxIndex(cardins);
        return ps.get(maxi);
    }

    /**
     * @return Array, same as ps, except each element is the number
     * of occurences in ps.
     */
    public static int[] getCardinalities(LinkedList<int[]> ps) {
        int[] cs = new int[ps.size()];
        int i = 0;

        for (int[] p : ps) {
            cs[i++] = getNoOccurences(p, ps);
        }
        return cs;
    }

    /**
     * @return Number of times p occures in ps (by value).
     */
    public static int getNoOccurences(int[] p, LinkedList<int[]> ps) {
        int n = 0;
        for (int[] q : ps) {
            if (same(p, q)) n++;
        }
        return n;
    }

    /**
     * @return The index of the max-element in xs. Returns last index
     * if all equal.
     */
    public static int getMaxIndex(int[] xs) {
        int maxX = Integer.MIN_VALUE;
        int maxI = -1;

        for (int i = 0; i < xs.length; i++) {
            if (xs[i] > maxX) {
                maxX = xs[i];
                maxI = i;
            }
        }
        return maxI;
    }


    /**
     * Prints point.
     */
    public static String toString(int[] p) {
        return String.format("[%s,%s]", p[0], p[1]);
    }
    public static String toString(double[] p) {
        return String.format("[%s,%s]", p[0], p[1]);
    }

    /**
     * Print array of points.
     */
    public static String toString(int[][] ps) {
        String s = "";
        for (int[] p : ps) s += toString(p) + "\n";
        return s;
    }
    public static String toString(double[][] ps) {
        String s = "";
        for (double[] p : ps) s += toString(p) + "\n";
        return s;
    }

    /**
     * @return Index in ps of point furthest from p.
     */
    public static int getFurthest(int[] p, int[][] ps) {
        double maxd = Double.MIN_VALUE;
        int maxi = -1;

        for (int i = 0; i < ps.length; i++) {
            if (distance(p, ps[i]) > maxd) {
                maxd = distance(p, ps[i]);
                maxi = i;
            }
        }
        return maxi;
    }
    public static int getFurthest(int[] p, LinkedList<int[]> ps) {
        return getFurthest(p, toArray(ps));
    }

    /**
     * @return [p1_index, p2_index] where p1, p2 is the two points
     * in ps with greatest distance between them.
     */
    public static int[] getFurthest(int[][] ps) {
        if (ps.length < 2) throw new IllegalArgumentException();

        double maxd = -1;
        int i0 = -1;
        int i1 = -1;

        for (int i = 0; i < ps.length; i++) {
            int j = getFurthest(ps[i], ps);

            if (distance(ps[i], ps[j]) > maxd) {
                maxd = distance(ps[i], ps[j]);
                i0 = i;
                i1 = j;
            }
        }
        return new int[]{i0, i1};
    }
    public static int[] getFurthest(LinkedList<int[]> ps) {
        return getFurthest(toArray(ps));
    }

    /**
     * @return Index in ps of point closest to p.
     */
    public static int getClosest(double[] p, double[][] ps) {
        double mind = Double.MAX_VALUE;
        int mini = -1;

        for (int i = 0; i < ps.length; i++) {
            if (distance(p, ps[i]) < mind) {
                mind = distance(p, ps[i]);
                mini = i;
            }
        }
        return mini;
    }


    /**
     * @return All points in space not in set (indexes).
     * Compares by value.
     */
    public static int[] getComplement(int[][] set, int[][] space) {
        int[] comp = new int[space.length];
        int compI = 0;

        for (int i = 0; i < space.length; i++) {
            int[] p = space[i];
            if (!contains(p, set)) comp[ compI++ ] = i;
        }

        return Arrays.copyOf(comp, compI);
    }

    public static int[] getComplement(int[] set, int[] space) {
        int[] comp = new int[space.length];
        int compI = 0;

        for (int i = 0; i < space.length; i++) {
            int x = space[i];
            if (!contains(x, set)) comp[ compI++ ] = i;
        }

        return Arrays.copyOf(comp, compI);
    }

    /**
     * @return Mean-value of points.
     */
    public static double[] mean(LinkedList<int[]> ps) {
        int[] sum = sum(ps);
        double sx = sum[0];
        double sy = sum[1];
        return new double[]{ sx/ps.size(), sy/ps.size() };
    }
    public static double[] mean(int[][] ps) {
        return mean(toList(ps));
    }
    public static double[] mean(double[][] ps) {
        double[] sum = sum(ps);
        double sx = sum[0];
        double sy = sum[1];
        return new double[]{ sx/ps.length, sy/ps.length };
    }

    /**
     * @return All points added together.
     */
    public static int[] sum(LinkedList<int[]> ps) {
        int sumx = 0;
        int sumy = 0;
        for (int[] p : ps) {
            sumx += p[0];
            sumy += p[1];
        }
        return new int[]{sumx, sumy};
    }
    public static double[] sum(double[][] ps) {
        double sumx = 0;
        double sumy = 0;
        for (double[] p : ps) {
            sumx += p[0];
            sumy += p[1];
        }
        return new double[]{sumx, sumy};
    }

    /**
     * @param cs Corner-points, random order.
     * @return Length of diagonal1+diagonal2.
     */
    public static double getCrossLength(int[][] cs) {
        int[] diag1 = getFurthest(cs);
        int[] diag2 = getComplement(diag1, new int[]{0,1,2,3});

        return
            distance(cs[diag1[0]], cs[diag1[1]]) +
            distance(cs[diag2[0]], cs[diag2[1]]);
    }

    /**
     * Removes indexes from list.
     */
    public static void removeIndexes(List<Integer> rms, List l) {
        Collections.sort(rms, Collections.reverseOrder());
        for (int i : rms) l.remove(i);
    }
    public static void removeIndexes(int[] rms, LinkedList l) {
        removeIndexes(toList(rms), l);
    }

    /**
     * Removes points in ps close to p (within distance D).
     */
    public static void removeClose(int[] p, LinkedList<int[]> ps, double D) {
        LinkedList<Integer> rms = new LinkedList<Integer>();

        for (int i = 0; i < ps.size(); i++) {
            int[] q = ps.get(i);
            if (distance(p, q) <= D) rms.add(i);
        }

        removeIndexes(rms, ps);
    }

    /**
     * @return List of points in ps not in the other lists.
     */
    public/***/ static LinkedList<int[]> getUniquePoints(LinkedList<int[]> ps, LinkedList<int[]> notIn1, LinkedList<int[]> notIn2) {
        LinkedList<int[]> rs = new LinkedList<int[]>();

        for (int[] p : ps) {
            if (!Math2.contains(p, notIn1) && !Math2.contains(p, notIn2)) {
                rs.add(p);
            }
        }
        return rs;
    }

    /**
     * @return [0]Leftmost, [1]upmost, [2]rightmost and [3]downmost
     * points in given list.
     */
    public static double[][] getExtremes(LinkedList<double[]> ps) {
        double[] l = new double[]{Double.MAX_VALUE, 0};
        double[] u = new double[]{0, Double.MAX_VALUE};
        double[] r = new double[]{Double.MIN_VALUE, 0};
        double[] d = new double[]{0, Double.MIN_VALUE};

        for (double[] p : ps) {
            if (p[0] < l[0]) l = p;
            if (p[1] < u[1]) u = p;
            if (p[0] > r[0]) r = p;
            if (p[1] > d[1]) d = p;
        }
        return new double[][]{l,u,r,d};
    }

    /**
     * @return True if you turn right at p1 when walking p0-p1-p2.
     */
    public static boolean rightTurn(int[] p0, int[] p1, int[] p2) {
        int[] v0 = Math2.minus(p1, p0);
        int[] v1 = Math2.minus(p2, p1);
        return Math2.angle(v0, v1) > 180;
    }

    /**
     * @return A normalized directional vector at specified angle
     * with horizontal plane.
     */
    public static double[] getDirVector(double deg) {
        double rad = Math.toRadians(deg);
        return new double[]{Math.cos(rad), -Math.sin(rad)};
    }

    /**
     * Solves equation Ax=b.
     *
     * @param A 2*2 matrix.
     * @param b Length-2-vector.
     * @return Vector x (length 2), or null if no solution.
     */
    public static double[] solve(double[][] A, double[] b) {
        double[][] Ainv = inverse(A);
        if (Ainv == null) return null;
        return multiply(Ainv, b);
    }

    /**
     * @return Inverse of A, or null if no inverse.
     */
    public static double[][] inverse(double[][] A) {
        double det = 1 / (A[0][0]*A[1][1] - A[0][1]*A[1][0]);
        return new double[][]{
            scale(new double[]{ A[1][1], -A[0][1] }, det),
            scale(new double[]{ -A[1][0], A[0][0] }, det) };
    }

    /**
     * @param A 2*2.
     * @param b 2*1.
     * @return A*b (2*1).
     */
    public static double[] multiply(double[][] A, double[] b) {
        return new double[]{ A[0][0]*b[0] + A[0][1]*b[1],
                             A[1][0]*b[0] + A[1][1]*b[1] };
    }

    /**
     * How long walk from point p0 towards p1 (pDist), and q0 towards
     * q1 (qDist) until they intersect.
     *
     * @return [pDist, qDist]
     */
    public static double[] intersectDistance(double[] p0, double[] p1, double[] q0, double[] q1) {
        double[] pv = normalize(minus(p1, p0));
        double[] qv = Math2.scale(normalize(minus(q1, q0)), -1);
        double[][] A = transpose(new double[][]{pv, qv});
        double[] b = minus(q0, p0);
        return solve(A, b);
    }

    public static double[] intersectPoint(double[] p0, double[] p1, double[] q0, double[] q1) {
        double[] d = intersectDistance(p0, p1, q0, q1);
        return step(q0, minus(q1, q0), d[1]);
    }

    /**
     * @param A 2*2 matrix.
     * @return A transposed.
     */
    public static double[][] transpose(double[][] A) {
        return new double[][]{
            new double[]{ A[0][0], A[1][0] },
            new double[]{ A[0][1], A[1][1] }};
    }



    //****************************************CONVERSIONS

    /**
     * @return Array converted from int[] to double[].
     */
    public static double[] toDouble(int[] xs) {
        double[] ys = new double[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ys[i] = (double)xs[i];
        }
        return ys;
    }

    /**
     * @return Array converted from double[] to int[] (by rounding).
     */
    public static int[] toInt(double[] xs) {
        int[] ys = new int[xs.length];
        for (int i = 0; i < xs.length; i++) {
            ys[i] = Math.round((float)xs[i]);
        }
        return ys;
    }

    /*
     * Turn array of int-point into double-point.
     */
    public static double[][] toDouble(int[][] vs) {
        double[][] vsD = new double[vs.length][2];
        int i = 0;
        for (int[] v : vs) vsD[i++] = toDouble(v);
        return vsD;
    }

    /**
     * Turn list of int-points to list of doubles.
     */
    public static LinkedList<double[]> toDouble(LinkedList<int[]> l) {
        LinkedList<double[]> l2 = new LinkedList<double[]>();
        for (int[] p : l) l2.add(toDouble(p));
        return l2;
    }

    /*
     * Turn array of double-points into int-points (by rounding).
     */
    public static int[][] toInt(double[][] vs) {
        int[][] vsI = new int[vs.length][2];
        int i = 0;
        for (double[] v : vs) vsI[i++] = toInt(v);
        return vsI;
    }

    /**
     * Turn list of double-points to list of ints (by rounding).
     */
    public static LinkedList<int[]> toInt(LinkedList<double[]> l) {
        LinkedList<int[]> l2 = new LinkedList<int[]>();
        for (double[] p : l) l2.add(toInt(p));
        return l2;
    }

    /*
     * Turn list of int-points into array.
     */
    public static int[][] toArray(LinkedList<int[]> l) {
        int[][] vs = new int[l.size()][2];
        int i = 0;
        for (int[] v : l) vs[i++] = v;
        return vs;
    }

    /*
     * Turn array of int-points into list.
     */
    public static LinkedList<int[]> toList(int[][] a) {
        LinkedList<int[]> l = new LinkedList<int[]>();
        for (int[] v : a) l.add(v);
        return l;
    }
    public static LinkedList<Integer> toList(int[] xs) {
        LinkedList<Integer> l = new LinkedList<Integer>();
        for (int x : xs) l.add(x);
        return l;
    }
}
