package map;

public class MapDimensions {

    /**
     * Returns the width and height (in degrees) of a map-area
     * defined by a mid-point, a zoom and pixel-properties of
     * the area. Pixel-properties, i.e pixel-width/height and
     * density (tileSize). Mapbox-standards..
     *
     * @param mid Mid-point, where [0]=x, mid[1]=y.
     * @param zoom Zoom-level (0-16).
     * @param pixelDims, [0]=width, [1]=height.
     * @param retina Pixel-density. false=default=256, true=retina=512.
     * @return Map-dims in degrees, where [0]=width, [1]=height.
     */
    public static double[] getDegreeDimensions(double[] mid, int zoom, int[] pixelDims, boolean retina) {
        int tileSize = 256;
        if (retina) tileSize = 512;

        double[] bounds = getBounds(mid, zoom, pixelDims, tileSize);
        double width = Math.abs(bounds[0] - bounds[2]);
        double height = Math.abs(bounds[1] - bounds[3]);

        return new double[]{width, height};
    }

    private static double[] getBounds(double[] mid, int zoom, int[] pixelDims, int tileSize) {
        double[][] env = getEnvironment(tileSize);
        double[] px = px(mid, zoom, env);
        double[] tl = ll(new double[] {
                px[0] - pixelDims[0] / 2.0,
                px[1] - pixelDims[1] / 2.0},
            zoom, env);
        double[] br = ll(new double[] {
                px[0] + pixelDims[0] / 2.0,
                px[1] + pixelDims[1] / 2.0},
            zoom, env);
        return new double[] {tl[0], br[1], br[0], tl[1]};
    }

    /**
     * From sphericalmercator constructor.
     */
    private static double[][] getEnvironment(int tileSize) {
        double size = tileSize;
        double[] bc = new double[30];
        double[] cc = new double[30];
        double[] zc = new double[30];
        double[] ac = new double[30];

        for (int i = 0; i < 30; i++) {
            bc[i] = size / 360;
            cc[i] = size / (2 * Math.PI);
            zc[i] = size / 2;
            ac[i] = size;
            size *= 2;
        }

        double[][] env = new double[4][30];
        env[0] = bc;
        env[1] = cc;
        env[2] = zc;
        env[3] = ac;
        return env;
    }

    private static double[] px(double[] ll, int zoom, double[][] env) {
        double d = env[2][zoom];
        double f = Math.min(Math.max(Math.sin(Math.PI/180 * ll[1]), -0.9999), 0.9999);
        double x = Math.round(d + ll[0] * env[0][zoom]);
        double y = Math.round(d + 0.5 * Math.log((1 + f) / (1 - f)) * (-env[1][zoom]));

        if (x > env[3][zoom]) x = env[3][zoom];
        if (y > env[3][zoom]) y = env[3][zoom];
        return new double[] {x, y};
    }

    private static double[] ll(double[] px, int zoom, double[][] env) {
        double g = (px[1] - env[2][zoom]) / (-env[1][zoom]);
        double lon = (px[0] - env[2][zoom]) / env[0][zoom];
        double lat = 180/Math.PI * (2 * Math.atan(Math.exp(g)) - 0.5 * Math.PI);
        return new double[] {lon, lat};
    }
}
