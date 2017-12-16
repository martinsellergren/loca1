package map;

import java.util.LinkedList;

/**
 * A map is represented as a defined area and an array of map-images
 * ({@link MapImage}) covering this area.
 *
 * @pre Every zoom-level's map-image covers the map's area.
 */
public class Map {
    public/***/ double westLon;
    public/***/ double northLat;
    public/***/ double eastLon;
    public/***/ double southLat;
    public/***/ LinkedList<MapImage> zoomLevels;

    /**
     * Constructs an empty map (no zoom-levels).
     */
    public Map(double w, double n, double e, double s) {
        westLon = w;
        northLat = n;
        eastLon = e;
        southLat = s;
        zoomLevels = new LinkedList<MapImage>();
    }

    /**
     * Complements the map with an additional zoom level.
     * If this zoom-level already exists, does nothing.
     *
     * @param mi Map-image covering map's area.
     */
    public void addZoomLevel(MapImage mi) {
    }

    /**
     * @param z Zoom-level.
     * @return True if z present.
     */
    public boolean hasZoomLevel(int z) {
        return false;
    }

    /**
     * @return [westLon, northLat, eastLon, southLat]
     */
    public double[] getBounds() {
        return null;
    }
}
