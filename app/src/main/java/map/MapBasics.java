package map;

import java.awt.geom.Point2D;

/**
 * Basic data about a map. Immutable. X-pos(longitude) and
 * Y-pos(latitude) are center-point of map.
 *
 * @inv width,height > 0
 * @inv 0 <= zoom <= 22
 */
public class MapBasics {
    public final double x;
    public final double y;
    public final int width;
    public final int height;
    public final double zoom;

    /**
     * Constructs a MapBasics object.
     * @param x X-position (longitude).
     * @param y Y-position (latitude).
     * @param w Width. w > 0
     * @param h Height. h > 0
     * @param z Zoom. 0 <= z <= 16
     */
    public MapBasics(double x, double y, int w, int h, double z) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.zoom = z;
    }

    /**
     * Gives the four corner points of this map.
     * @return points[0] - top left corner point
     *         points[1] - top right
     *         points[2] - bottom right
     *         points[3] - bottom left
     */
    public Point2D.Double[] getCornerPoints() {
        return null;
    }
}
