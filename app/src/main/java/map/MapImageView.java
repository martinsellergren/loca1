package map;

/**
 * A representation of a subsection of an image containing a map.
 * Enables easy convertion between image-pixel and geo-coordinates.
 * Immutable. Lon(gitude) and lat(itude) defines the position of
 * the center-point of the image. (Web-)Mercator-projection.
 * (0,0) is equator around Africa. Lon grows west, lat grows north.
 *
 * @inv -180 <= lon < 180
 * @inv -85 < lat < 85 (2*arctan(e^pi) - pi/2)
 * @inv width/height > 0
 * @inv 0 <= zoom <= 22
 * @inv lat-bounds inside +-85...
 */
public class MapImageView {
    /** Map-center longitude (degrees). */
    public final double lon;

    /** Map-center latitude (degrees). */
    public final double lat;

    /** Map width in pixels. Actual width depends on pixel density. */
    public final int width;

    /** Map height in pixels. Actual height depends on pixel density. */
    public final int height;

    /** Zoom of image where 0 is all way out and 22 is all way in. */
    public final int zoom;

    /** Tile size (x*x). Zoom level 0 fits whole world on one tile. */
    public final int tileSize;

    /** Max/min value for latitude. */
    public static final double LATITUDE_BOUND = Math.toDegrees(2*Math.atan(Math.exp(Math.PI)) - Math.PI/2);

    /**
     * Default constructor.
     *
     * @param lon Center longitude.
     * @param lat Center latitude.
     * @param w Pixel-width. w > 0.
     * @param h Pixel-height. h > 0.
     * @param z Zoom. 0 <= z <= 22.
     * @param doubleQ A high quality image.
     */
    public MapImageView(double lon, double lat, int w, int h, int z, boolean doubleQ) {
        this.lon = lon;
        this.lat = lat;
        this.width = w;
        this.height = h;
        this.zoom = z;
        this.tileSize = doubleQ ? (MapRequest.DEFAULT_TILE_SIZE*2) : MapRequest.DEFAULT_TILE_SIZE;

        assertLatitude(this.lat, this.height, this.zoom, this.tileSize);
    }

    /**
     * Constructs from map-bounds.
     *
     * @param west Western longitude.
     * @param north Northen latitude.
     * @param east Eastern longitude.
     * @param south Southern latitude.
     */
    public MapImageView(double west, double north, double east, double south, int z, boolean doubleQ) {
        this.tileSize = doubleQ ? (MapRequest.DEFAULT_TILE_SIZE*2) : MapRequest.DEFAULT_TILE_SIZE;

        double[] tl  = getPixelCoordinates_global(west, north, z, this.tileSize);
        double[] br  = getPixelCoordinates_global(east, south, z, this.tileSize);
        double width = br[0]-tl[0] > 0 ? (br[0]-tl[0]) :
            (getGlobalPixelMax(z, this.tileSize)[0]-tl[0]+br[0]);
        double height = br[1] - tl[1];
        double midX = (tl[0] + br[0]) / 2;
        double midY = (tl[1] + br[1]) / 2;
        double[] midGeo = getGeoCoordinates_global(midX, midY, z, this.tileSize);

        this.lon = midGeo[0];
        this.lat = midGeo[1];
        this.width = Math.round((float)width);
        this.height = Math.round((float)height);
        this.zoom = z;

        assertLatitude(this.lat, this.height, this.zoom, this.tileSize);
    }

    /**
     * Throws RuntimeException if latitude bound is ouside valid span.
     *
     * @param lat Latitude.
     * @param h Height.
     * @param z Zoom.
     * @param q Tile size.
     */
    public/***/ static void assertLatitude(double lat, int h, int z, int q) {
        if (!okLatitudeBound(lat, h, z, q)) {
            throw new RuntimeException("Illegal latitude bounds");
        }
    }

    /**
     * @param lat Latitude.
     * @param h Height.
     * @param z Zoom.
     * @param q Tile size.
     * @return False if latitude bound is ouside valid span.
     */
    public/***/ static boolean okLatitudeBound(double lat, int h, int z, int q) {
        double[] globMid = getPixelCoordinates_global(0, lat, z, q);
        int yMin = Math.round( (float)(globMid[1] - h/2d) );
        int yMax = Math.round( (float)(globMid[1] + h/2d) );

        return (yMin >= 0 && yMax <= getGlobalPixelMax(z, q)[1]);
    }

    /**
     * Get max pixel coordinates for a certain zoom, img quality.
     *
     * @param z Zoom level.
     * @param q Tile size.
     * @return Max pixel coordinates [x,y].
     */
    public/***/ static int[] getGlobalPixelMax(int z, int q) {
        double[] xy = getPixelCoordinates_global(179.9999999, -LATITUDE_BOUND, z, q);
        return new int[]{ Math.round((float)xy[0]), Math.round((float)xy[1]) };
    }

    /**
     * @return [wLon, nLat, eLon, sLat]
     */
    public double[] getGeoBounds() {
        double[] nw = getGeoCoordinates(0, 0);
        double[] se = getGeoCoordinates(this.width, this.height);
        return new double[]{ nw[0], nw[1], se[0], se[1] };
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
        double[] globalMid = getPixelCoordinates_global(this.lon, this.lat, this.zoom, this.tileSize);
        double globalX = globalMid[0] - this.width/2d + x;
        double globalY = globalMid[1] - this.height/2d + y;

        return getGeoCoordinates_global(globalX, globalY, this.zoom, this.tileSize);
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
        double[] globalMid = getPixelCoordinates_global(this.lon, this.lat, this.zoom, this.tileSize);
        double tlx = globalMid[0] - this.width/2d;
        double tly = globalMid[1] - this.height/2d;

        double[] globalXY = getPixelCoordinates_global(lon, lat, this.zoom, this.tileSize);
        return new double[]{ globalXY[0]-tlx, globalXY[1]-tly };
    }

    /**
     * Turn global pixel-coordinates into geo-coordinates. Global
     * pixel-coordinates is defined by a grid covering the whole
     * earth (lat +-85, lon +-180). The granularity of the grid is
     * defined by zoom-level and tileSize (image-quality).
     * (0,0) is top left corner i.e lon -180, lat ~85.
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
