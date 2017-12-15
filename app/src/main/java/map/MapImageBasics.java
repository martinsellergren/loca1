package map;

/**
 * Basic data about a map image. Immutable. Lon(gitude) and lat(itude)
 * are the center-point. (Web-)Mercator-projection.
 * (0,0) is equator around Africa. Lon grows west, lat grows north.
 *
 * @inv -180 < lon < 180
 * @inv -85 < lat < 85 (2*arctan(e^pi) - pi/2)
 * @inv width/height > 0
 * @inv 0 <= zoom <= 22
 * @inv lat-bounds inside +-85...
 */
public class MapImageBasics {
    /** Map-center longitude. */
    public final double lon;

    /** Map-center latitude. */
    public final double lat;

    /** Map width in pixels. */
    public final int width;

    /** Map height in pixels. */
    public final int height;

    /** Zoom of image following mapbox-standard where
     * 0 is all way out and 22 is all way in. */
    public final int zoom;

    /** Map-image is highQuality. Tile-size low=256, hight=512. */
    public final boolean highQuality;

    /** Show map-provider's attribution on map. */
    public final boolean attribution;

    /**
     * Default constructor.
     *
     * @param lon Center longitude.
     * @param lat Center latitude.
     * @param w Pixel-width. w > 0
     * @param h Pixel-height. h > 0
     * @param z Zoom. 0 <= z <= 22
     * @param highQ Use high quality image.
     * @param attrib Show attribution on map.
     */
    public MapImageBasics(double lon, double lat, int w, int h, int z, boolean highQ, boolean attrib) {
        this.lon = lon;
        this.lat = lat;
        this.width = w;
        this.height = h;
        this.zoom = z;
        this.highQuality = highQ;
        this.attribution = attrib;
    }

    /**
     * Constructor for default quality and no attribution.
     * Quality-default defined in MapFetcher.
     */
    public MapImageBasics(double lon, double lat, int w, int h, int z) {
        this(lon, lat, w, h, z, MapFetcher.USE_HIGH_QUALITY_IMAGE, false);
    }

    /**
     * Constructs from map-bounds instead of centerpoint and pixel dims.
     *
     * @param west Western longitude.
     * @param north Northen latitude.
     * @param east Eastern longitude.
     * @param south Southern latitude.
     */
    public MapImageBasics(double west, double north, double east, double south, int z, boolean highQ, boolean attrib) {
        this(0,0,0,0,0,false,false);
    }

    /**
     * Map-bounds constructor with default quality, no attribution.
     */
    public MapImageBasics(double west, double north, double east, double south, int z) {
        this(west, north, east, south, z, MapFetcher.USE_HIGH_QUALITY_IMAGE, false);
    }

    /**
     * @return [lonMin, latMin, lonMax, latMax]
     */
    public double[] getBounds() {
        return null;
    }

    /**
     * Increases width and height so that cut labels will fit inside.
     * The expansion-factor used is derived from experements.
     */
    public void expandToIncludeCutLabels() {
    }

    /**
     * Increases width and height.
     *
     * @param factor Expansion factor.
     */
    public void expand(double factor) {
    }

    /**
     * Splits "map" into smaller blocks, each with a maximum side-
     * length of a certain length. If "enough map is left", the
     * maximum side length becomes the side length of the block.
     * That means, only blocks in last row or column can be smaller
     * than the maximum side length.
     *
     * @param l Maximum side length.
     * @return A 2d-layout of "maps".
     */
    public MapImageBasics[][] split(int l) {
        // int rows = this.pixelHeight / l;
        // int cols = this.pixelWidth / l;
        // if (this.pixelHeight % l != 0) rows += 1;
        // if (this.pixelWidth % l != 0) cols += 1;

        // MapBasics[][] layout = new MapBasics[rows][cols];

        // for (int r = 0; r < rows; r++) {
        //     for (int c = 0; c < cols; c++) {
        //         double x0 = this.x - this.pixelWidth/2.0 + l*c;
        //         double y0 = this.y - this.pixelHeight/2.0 + l*r;
        //         double x1 = x0 + l;
        //         double y1 = y0 + l;
        //         int pxw = l;
        //         int pxh = l;

        //         if ((c+1) * l > this.pixelWidth) {
        //             x1 = this.x + this.pixelWidth/2.0;
        //             pxw = this.pixelWidth - c*l;
        //         }
        //         if ((r+1) * l > this.height) {
        //             y1 = this.y + this.height/2.0;
        //             pxh = this.pixelHeight - r*l;
        //         }

        //         double xMid = (x0 + x1) / 2;
        //         double yMid = (y0 + y1) / 2;
        //         layout[r][c] = new MapBasics(xMid, yMid, pxw, pxh, this.zoom, this.highQuality, this.attribution);
        //     }
        // }

        // return layout;
        return null;
    }

    /**
     * Turn pixel-coordinates into geo-coordinates. Pixel cordinates
     * are specific to associated map-image (local).
     * (0,0) is top left corner of this image.
     *
     * @param x Local x-pixel.
     * @param y Local y-pixel.
     * @return [lon, lat]]
     */
    public double[] getGeoCoordinates(int x, int y) {
        return null;
    }

    /**
     * Turn global pixel-coordinates into geo-coordinates. Global
     * pixel-coordinates is defined by a grid covering the whole
     * earth (lat +-85, lon +-180). The granularity of the grid is
     * defined by zoom-level and tileSize (image-quality).
     * (0,0) is top left corner i.e lat 85, lon -180.
     *
     * @param x Global x-pixel.
     * @param y Global y-pixel.
     * @return [lon, lat]
     */
    private double[] getGeoCoordinates_global(int x, int y) {
        return null;
    }

    /**
     * Turn geo-coordinates into pixel-coordinates (local to this
     * image). Around opposite of getGeoCoordinates except this method
     * returns rounded values.
     *
     * @param lon Longitude.
     * @param lat Latitude.
     * @return Local [x, y]
     */
    public int[] getPixelCoordinates(double lon, double lat) {
        return null;
    }

    /**
     * Same as getPixelCoordinates except returns real values.
     * Exact opposite of getGeoCoordinates.
     *
     * @param lon Longitude.
     * @param lat Latitude.
     * @return Local [x, y]
     */
    private double[] getPixelCoordinates_real(double lon, double lat) {
        return null;
    }

    /**
     * Turn geo-coordinates into global pixel-coordinates.
     * Around opposite of getGeoCoordinates_global except this method
     * returns rounded values.
     *
     * @param lon Longitude.
     * @param lat Latitude.
     * @return Global [x, y]
     */
    private int[] getPixelCoordinates_global(double lon, double lat) {
        return null;
    }

    /**
     * Same as getPixelCoordinates_global except returns real values.
     * Exact opposite of getGeoCoordinates_global.
     *
     * @param lon Longitude.
     * @param lat Latitude.
     * @return Global [x, y]
     */
    private double[] getPixelCoordinates_global_real(double lon, double lat) {
        return null;
    }

    @Override
    public String toString() {
        return
            "lon: " + this.lon + ", " +
            "lat: " + this.lat + ", " +
            "width: " + this.width + ", " +
            "height: " + this.height + ", " +
            "zoom: " + this.zoom + "\n";
    }
}
