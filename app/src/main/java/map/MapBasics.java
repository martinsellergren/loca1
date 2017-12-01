package map;

import java.awt.geom.Point2D;
//import java.lang.Math;

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
        double topLX = x - (double)width/2;
        double topLY = y - (double)height/2;
        double topRX = topLX + width;
        double topRY = topLY;
        double bottomRX = topRX;
        double bottomRY = topRY + height;
        double bottomLX = topLX;
        double bottomLY = bottomRY;

        return new Point2D.Double[] {
            new Point2D.Double(topLX, topLY),
            new Point2D.Double(topRX, topRY),
            new Point2D.Double(bottomRX, bottomRY),
            new Point2D.Double(bottomLX, bottomLY)
        };
    }

    /**
     * Splits "map" into smaller blocks, each with a maximum side-
     * length of a certain length.
     *
     * @param l Maximum side length.
     * @return A 2d-layout of "maps".
     */
    public MapBasics[][] split(int l) {
        int rows = this.height / l;
        int cols = this.width / l;
        if (this.height % l != 0) rows += 1;
        if (this.width % l != 0) cols += 1;

        MapBasics[][] layout = new MapBasics[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x0 = this.x - (double)this.width/2 + l*c;
                double y0 = this.y - (double)this.height/2 + l*r;
                double x1 = x0 + l;
                double y1 = y0 + l;
                int w = l;
                int h = l;

                if ((c+1) * l > this.width) {
                    x1 = this.x + (double)this.width/2;
                    w = this.width - c*l;
                }
                if ((r+1) * l > this.height) {
                    y1 = this.y + (double)this.height/2;
                    h = this.height - r*l;
                }

                double xMid = (x0 + x1) / 2;
                double yMid = (y0 + y1) / 2;
                layout[r][c] = new MapBasics(xMid, yMid, w, h, this.zoom);
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
