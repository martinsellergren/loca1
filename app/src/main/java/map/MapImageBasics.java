package map;

/**
 * Basic data about a map image. Immutable. Lon(gitude) and lat(itude)
 * are the center-point (in degrees). (Web-)Mercator-projection.
 * (0,0) is equator around Africa. Lon grows west, lat grows north.
 *
 * @inv -180 <= lon < 180
 * @inv -85 < lat < 85 (2*arctan(e^pi) - pi/2)
 * @inv width/height > 0
 * @inv 0 <= zoom <= 22
 * @inv lat-bounds inside +-85...
 */
public class MapImageBasics {
    /** Map-center longitude (degrees). */
    public final double lon;

    /** Map-center latitude (degrees). */
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
    private final int LOW_TILE_SIZE = 256;
    private final int HIGH_TILE_SIZE = 512;

    /** Show map-provider's attribution on map. */
    public final boolean attribution;

    /**
     * Expansion factor for expanding the dimensions in order
     * to include original-size's cut labels.
     * Used by method expandToIncludeCutLabels().
     * Value derived from experiments.
     */
    private final double INCLUDE_CUT_LABELS_EXPANSION_FACTOR = 10;

    /** Max/min value for latitude. */
    public static final double LATITUDE_BOUND = Math.toDegrees(2*Math.atan(Math.exp(Math.PI)) - Math.PI/2);

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

    /** NEEDS TESTING
     * Constructs from map-bounds.
     *
     * @param west Western longitude.
     * @param north Northen latitude.
     * @param east Eastern longitude.
     * @param south Southern latitude.
     */
    public MapImageBasics(double west, double north, double east, double south, int z, boolean highQ, boolean attrib) {
        int q = LOW_TILE_SIZE;
        if (highQ) q = HIGH_TILE_SIZE;

        double[] tl  = getPixelCoordinates_global(west, north, z, q);
        double[] br  = getPixelCoordinates_global(east, south, z, q);
        double width = br[0] - tl[0];
        double height = br[1] - tl[1];
        double midX = tl[0] + width / 2;
        double midY = tl[1] + height / 2;
        double[] midGeo = getGeoCoordinates_global(midX, midY, z, q);

        this.lon = midGeo[0];
        this.lat = midGeo[1];
        this.width = Math.round((float)width);
        this.height = Math.round((float)height);
        this.zoom = z;
        this.highQuality = highQ;
        this.attribution = attrib;
    }

    /**
     * Map-bounds constructor with default quality, no attribution.
     */
    public MapImageBasics(double west, double north, double east, double south, int z) {
        this(west, north, east, south, z, MapFetcher.USE_HIGH_QUALITY_IMAGE, false);
    }

    /** NEEDS TESTING
     * @return [wLon, nLat, eLon, sLat]
     */
    public double[] getBounds() {
        double[] nw = getGeoCoordinates(0, 0);
        double[] se = getGeoCoordinates(this.width, this.height);
        return new double[]{ nw[0], nw[1], se[0], se[1] };
    }

    /**
     * Increases width and height so that cut labels will fit inside.
     * The expansion-factor used is derived from experements.
     *
     * @return New object with updated dims.
     */
    public MapImageBasics expandToIncludeCutLabels() {
        return expand(INCLUDE_CUT_LABELS_EXPANSION_FACTOR);
    }

    /** NEEDS TESTING
     * Increases width and height.
     *
     * @param factor Expansion factor.
     * @return New object with updated dims.
     */
    public MapImageBasics expand(double factor) {
        int newW = Math.round(this.width * (float)factor);
        int newH = Math.round(this.height * (float)factor);

        return new MapImageBasics(this.lon, this.lat, newW, newH, this.zoom, this.highQuality, this.attribution);
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
        int rows = this.height / l;
        int cols = this.width / l;
        if (this.height % l != 0) rows += 1;
        if (this.width % l != 0) cols += 1;

        MapImageBasics[][] layout = new MapImageBasics[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x0 = c * l;
                int y0 = r * l;
                int x1 = Math.min(x0 + l, this.width);
                int y1 = Math.min(y0 + l, this.height);
                double xMid = (x0 + x1) / 2d;
                double yMid = (y0 + y1) / 2d;
                double[] latlon = getGeoCoordinates(xMid, yMid);

                layout[r][c] = new MapImageBasics(latlon[0], latlon[1], x1-x0, y1-y0, this.zoom, this.highQuality, this.attribution);
            }
        }

        return layout;
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
    public double[] getGeoCoordinates(double x, double y) {
        int q = LOW_TILE_SIZE;
        if (this.highQuality) q = HIGH_TILE_SIZE;

        double[] globalMid = getPixelCoordinates_global(this.lon, this.lat, this.zoom, q);
        double globalX = globalMid[0] - this.width/2d + x;
        double globalY = globalMid[1] - this.height/2d + y;

        return getGeoCoordinates_global(globalX, globalY, this.zoom, q);
    }

    /**
     * Turn geo-coordinates into pixel-coordinates (local to this
     * image). Opposite of getGeoCoordinates().
     *
     * @param lon Longitude.
     * @param lat Latitude.
     * @return Local [x, y]
     */
    public double[] getPixelCoordinates(double lon, double lat) {
        int q = LOW_TILE_SIZE;
        if (this.highQuality) q = HIGH_TILE_SIZE;

        double[] globalMid = getPixelCoordinates_global(this.lon, this.lat, this.zoom, q);
        double tlx = globalMid[0] - this.width/2d;
        double tly = globalMid[1] - this.height/2d;

        double[] globalXY = getPixelCoordinates_global(lon, lat, this.zoom, q);
        return new double[]{ globalXY[0]-tlx, globalXY[1]-tly };
    }

    /**
     * Turn global pixel-coordinates into geo-coordinates. Global
     * pixel-coordinates is defined by a grid covering the whole
     * earth (lat +-85, lon +-180). The granularity of the grid is
     * defined by zoom-level and tileSize (image-quality).
     * (0,0) is top left corner i.e lat ~85, lon -180.
     *
     * @param x Global x-pixel.
     * @param y Global y-pixel.
     * @param z Zoom level.
     * @param q TileSize (img-quality).
     * @return [lon, lat]
     */
    public/***/ static double[] getGeoCoordinates_global(double x, double y, int z, int q) {
        double lon = 2*x*Math.PI / (q*Math.pow(2, z)) - Math.PI;
        double lat = 2*Math.atan(Math.exp(Math.PI - 2*Math.PI*y / (q*Math.pow(2, z)))) - Math.PI/2;

        return new double[]{ Math2.toUnitDegrees(Math.toDegrees(lon)),
                             Math2.toUnitDegrees(Math.toDegrees(lat)) };
    }

    /**
     * Turn geo-coordinates into global pixel-coordinates.
     * Opposite of getGeoCoordinates_global().
     *
     * @param lon Longitude.
     * @param lat Latitude.
     * @param z Zoom level.
     * @param q TileSize (img-quality).
     * @return Global [x, y]
     */
    public/***/ static double[] getPixelCoordinates_global(double lon, double lat, int z, int q) {
        lon = Math.toRadians(Math2.toUnitDegrees(lon));
        lat = Math.toRadians(Math2.toUnitDegrees(lat));

        double x = q / (2*Math.PI) * Math.pow(2, z) * (lon + Math.PI);
        double y = q / (2*Math.PI) * Math.pow(2, z) * (Math.PI - Math.log(Math.tan(Math.PI/4 + lat/2)));
        return new double[]{ x, y };
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
