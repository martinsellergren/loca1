package map;

import java.util.LinkedList;
import java.io.IOException;

/**
 * A map is represented as a defined area on earth and an array
 * of map-images ({@link MapImage}) of different zoom-levels
 * covering this area.
 *
 * @inv Every zoom-level's map-image covers the map's area.
 * @inv Strictly increasing zoom-level in zoom-level-imgs-list.
 */
public class Map {

    /**
     * Use double quality images. Slower to fetch and analyse. */
    public static final boolean X2 = true;

    /**
     * Max zoom-level. */
    public static final int MAX_ZOOM_LEVEL = 22;

    /** [west, south, east, north]-lat/lon-bounds. */
    private final double[] area;

    /** Map-images of zoom-levels, ordered by increasing zoom. */
    private final LinkedList<MapImage> zoomLevels;

    /** Language of map-labels. */
    private final Language language;

    /**
     * Constructs an empty map (no zoom-levels).
     *
     * @param wsen Lat/lon-bounds.
     * @param lang Language of label-text in zoomLevel-images.
     */
    public Map(double[] wsen, Language lang) {
        this.area = wsen.clone();
        this.language = lang;
        this.zoomLevels = new LinkedList<MapImage>();
    }

    /**
     * Adds zoom level by fetching images from internet and analyzing
     * them for labels.
     * If this zoom-level already exists, does nothing.
     * Beware: might easily be an infinite call.
     *
     * @param z Zoom-level [0, 22].
     * @return True if zoom-level added.
     */
    public boolean addZoomLevel(int z) throws IOException {
        if (!hasZoomLevel(z) && z > 0 && z <= MAX_ZOOM_LEVEL) {
            MapImageView v = new MapImageView(area, z, X2);
            zoomLevels.add( new MapImage(v, this.language) );
            return true;
        }
        else return false;
    }

    /**
     * Finds an appropriate zoom-level and adds it.
     * Appropriate zoom-level is a zoom-level that is:
     *  - Bigger than current maximum zoom,
     *  - That has an approximate increase in map-objects defined by
     *    factor-constant,
     *  - Never has fewer map-objects than min-limit defined by
     *    factor-constant (unless last zoom-level).
     *
     * Increasing zoom-levels are fetched and analysed (by steps of
     * constant) until finds a good one, or one worse than
     * previous one.
     *
     * If zoom-level-list currently empty, finds an appropriate
     * start-level for analysis quickly without any fetching/ analysing.
     *
     * @return True if there are any more appropriate zoom-levels.
     */
    public boolean addAppropriateZoomLevel() {
        return false;
    }

    /**
     * @param z Zoom-level.
     * @return True if z present.
     */
    public boolean hasZoomLevel(int z) {
        for (MapImage mimg : zoomLevels)
            if (z == mimg.getView().zoom) return true;
        return false;
    }

    /**
     * @return [WSEN].
     */
    public double[] getArea() {
        return this.area.clone();
    }
}
