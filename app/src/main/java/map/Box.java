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
     * @return Box width.
     */
    public double getWidth() {
        return 0;
    }

    /**
     * @return Box height.
     */
    public double getHeight() {
        return 0;
    }

    public Point getTopLeft() {
        return null;
    }
    public Point getTopRight() {
        return null;
    }
    public Point getBottomLeft() {
        return null;
    }
    public Point getBottomRight() {
        return null;
    }
    public Point getTopMid() {
        return null;
    }
    public Point getBottomMid() {
        return null;
    }
    public Point getLeftMid() {
        return null;
    }
    public Point getRightMid() {
        return null;
    }
    public Point getHighest() {
        return null;
    }
    public Point getLowest() {
        return null;
    }
    public Point getLeftest() {
        return null;
    }
    public Point getRightest() {
        return null;
    }
}
