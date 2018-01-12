package map;

import java.util.LinkedList;

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
        if (length(v) == 0) return 0;

        double[] w = new double[]{v[0], v[1]};
        w[1] = -w[1];
        double a = Math.acos(w[0] / length(w));

        if (Math.asin(w[1] / length(w)) >= 0)
            return Math2.toUnitDegrees(Math.toDegrees(a));
        else
            return Math2.toUnitDegrees(Math.toDegrees(-a));
    }
    public static double angle(int[] v) {
        return angle(toDouble(v));
    }

    /**
     * @return Angle between two vectors, in degrees.
     */
    public static double angle(double[] v1, double[] v2) {
        return 0;
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
    public static boolean same(int[] p1, int[] p2, double delta) {
        return distance(p1, p2) <= delta;
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



    //****************************************CONVERSIONS

    /**
     * @return Point converted from int[] to double[].
     */
    public static double[] toDouble(int[] v) {
        return new double[]{v[0], v[1]};
    }

    /**
     * @return Point converted from double[] to int[] (by rounding).
     */
    public static int[] toInt(double[] v) {
        return new int[] { Math.round((float)v[0]),
                           Math.round((float)v[1])};
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

    // /*
    //  * Turn list of int-points into array.
    //  */
    // public static int[][] toArray_int(LinkedList<int[]> l) {
    //     int[][] vs = new int[l.size()][2];
    //     int i = 0;
    //     for (int[] v : l) vs[i++] = v;
    //     return vs;
    // }

    // /*
    //  * Turn array of int-points into list.
    //  */
    // public static LinkedList<int[]> toList_int(int[][] a) {
    //     LinkedList<int[]> l = new LinkedList<int[]>();
    //     for (int[] v : a) l.add(v);
    //     return l;
    // }
}
