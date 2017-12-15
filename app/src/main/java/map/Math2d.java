package map;

/**
 * Some math for 2d-vectors.
 */
public class Math2d {
    /**
     * @return Distance between two points.
     */
    public static double distance(double[] p1, double[] p2) {
        return 0;
    }

    public static double distance(int[] p1, int[] p2) {
        return distance(new double[]{p1[0], p1[1]},
                        new double[]{p2[0], p2[1]});
    }

    /**
     * @return Angle between two vectors, in degrees.
     */
    public static double angle(double[] v1, double[] v2) {
        return 0;
    }

    public static double angle(int[] v1, int[] v2) {
        return angle(new double[]{v1[0], v1[1]},
                        new double[]{v2[0], v2[1]});
    }

    /**
     * @return Angle with the horizontal axis, in degrees.
     */
    public static double angle(double[] v) {
        return 0;
    }

    /**
     * @return Angle with the horizontal axis, in degrees.
     */
    public static double angle(int[] v) {
        return angle(new double[]{v[0], v[1]});
    }
}
