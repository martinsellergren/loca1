package map;

/**
 * Basic data about a map (with pixel-specification, like a printed
 * map). Immutable. X-pos(longitude) and Y-pos(latitude) are the
 * center-point. Mercator-style. (0,0) is equator around Africa.
 * X grows west, Y grows north.
 *
 * @inv -180 <= x <= 180
 * @inv y + height/2 <= 90
 * @inv y - height/2 >= -90
 * @inv all dims > 0
 * @inv 0 <= zoom <= 22
 */
public class MapBasics {
    /** Map-center x-value. */
    public final double x;

    /** Map-center y-value. */
    public final double y;

    /** Map width in degrees. */
    public final double width;

    /** Map height in degrees. */
    public final double height;

    /** Zoom of image following mapbox-standard where
     * 0 is all way out and 22 is all way in. */
    public final int zoom;

    /** Map-image pixels on width. */
    public final int pixelWidth;

    /** Map-image pixels on height. */
    public final int pixelHeight;

    /** Map-image is highQuality (retina).
     * Tile-size 256=default, 512=retina. */
    public final boolean highQuality;

    /** Show map-provider's attribution on map. */
    public final boolean attribution;

    /**
     * Constructs a MapBasics object.
     * @param x X-position (longitude).
     * @param y Y-position (latitude).
     * @param pxW Pixel-width. pxW > 0
     * @param pxH Pixel-height. pxH > 0
     * @param z Zoom. 0 <= z <= 22
     * @param highQ Use high quality image.
     * @param attrib Show attribution on map.
     */
    public MapBasics(double x, double y, int pxW, int pxH, int z, boolean highQ, boolean attrib) {
        this.x = x;
        this.y = y;
        this.pixelWidth = pxW;
        this.pixelHeight = pxH;
        this.zoom = z;
        this.highQuality = highQ;
        this.attribution = attrib;

        double[] mid = {x, y};
        int[] pxDims = {pxW, pxH};
        double[] dims = MapDimensions.getDegreeDimensions(mid, zoom, pxDims, highQ);
        this.width = dims[0];
        this.height = dims[1];
    }

    /**
     * Constructor for default(low) quality and no attribution.
     */
    public MapBasics(double x, double y, int pxW, int pxH, int z) {
        this(x, y, pxW, pxH, z, false, false);
    }

    /**
     * Gives the bounds of this map.
     *
     * @return {xMin, yMin, xMax, yMax}
     */
    // public Point2D.Double[] getCornerPoints() {
    //     double topLX = x - width/2;
    //     double topLY = y - height/2;
    //     double topRX = topLX + width;
    //     double topRY = topLY;
    //     double bottomRX = topRX;
    //     double bottomRY = topRY + height;
    //     double bottomLX = topLX;
    //     double bottomLY = bottomRY;

    //     return new Point2D.Double[] {
    //         new Point2D.Double(topLX, topLY),
    //         new Point2D.Double(topRX, topRY),
    //         new Point2D.Double(bottomRX, bottomRY),
    //         new Point2D.Double(bottomLX, bottomLY)
    //     };
    // }

    /**
     * Splits "map" into smaller blocks, each with a maximum side-
     * length of a certain length. If "enough map is left", the
     * maximum side length becomes the side length of the block.
     *
     * @param l Maximum side length.
     * @return A 2d-layout of "maps".
     */
    public MapBasics[][] split(int l) {
        int rows = this.pixelHeight / l;
        int cols = this.pixelWidth / l;
        if (this.pixelHeight % l != 0) rows += 1;
        if (this.pixelWidth % l != 0) cols += 1;

        MapBasics[][] layout = new MapBasics[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x0 = this.x - this.pixelWidth/2.0 + l*c;
                double y0 = this.y - this.pixelHeight/2.0 + l*r;
                double x1 = x0 + l;
                double y1 = y0 + l;
                int pxw = l;
                int pxh = l;

                if ((c+1) * l > this.pixelWidth) {
                    x1 = this.x + this.pixelWidth/2.0;
                    pxw = this.pixelWidth - c*l;
                }
                if ((r+1) * l > this.height) {
                    y1 = this.y + this.height/2.0;
                    pxh = this.pixelHeight - r*l;
                }

                double xMid = (x0 + x1) / 2;
                double yMid = (y0 + y1) / 2;
                layout[r][c] = new MapBasics(xMid, yMid, pxw, pxh, this.zoom, this.highQuality, this.attribution);
            }
        }

        return layout;
    }

    @Override
    public String toString() {
        return "x: " + this.x + ", " +
            "y: " + this.y + ", " +
            "w: " + this.width + ", " +
            "h: " + this.height + ", " +
            "z: " + this.zoom + "\n";
    }
}
