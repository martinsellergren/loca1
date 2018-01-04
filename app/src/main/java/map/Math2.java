package map;

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

    /**
     * @return Vector converted from int[] to double[].
     */
    public static double[] toDouble(int[] v) {
        return new double[]{v[0], v[1]};
    }

    /**
     * @return Vector converted from double[] to int[].
     */
    public static int[] toInt(double[] v) {
        return new int[] { Math.round((float)v[0]),
                           Math.round((float)v[1])};
    }
}
