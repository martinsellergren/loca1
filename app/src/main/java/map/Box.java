package map;

import java.awt.Point;

/**
 * A box located in an integer-grid, with some rotation.
 *
 * @inv width,height > 0
 */
public class Box {
    private Point topL;
    private Point topR;
    private double height;

    /**
     * Constructs a Box from four corner points.
     * Top points are final. Rest is calculated to create an actual
     * rectangular box.
     */
    public Box(Point topL, Point topR, Point bottomR, Point bottomL) {
    }

    /**
     * @return Rotation of box in degrees.
     */
    public double getRotation() {
        return 0;
    }

    /**
     * return Midpoint of box.
     */
    public Point getMidpoint() {
        return null;
    }

    /**
     * return box width.
     */
    public double getWidth() {
        return 0;
    }

    /**
     * return box height.
     */
    public double getHeight() {
        return 0;
    }
}
