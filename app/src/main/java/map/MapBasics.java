package map;

import java.awt.geom.Point2D;
import java.awt.Dimension;

/**
 * Basic data about a map.
 *
 * @inv width,height > 0
 * @inv 0 <= zoom <= 22
 */
public class MapBasics {
    private Point2D.Double pos;
    private Dimension dim;
    private double zoom;

    /**
     * Constructs a MapBasics object.
     * @param pos Position.
     * @param dim Dimensions. dim.width,dim.height > 0
     * @param z Zoom. 0 <= z <= 16
     */
    public MapBasics(Point2D.Double pos, Dimension dim, double z) {
    }
}
