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

    /**
     * @return A non-rotated bounding-box to this box represented
     * as four corner points (top/bottom/left/right is relative
     * to the screen):
     *  bb[0] - top-left
     *  bb[1] - top-right
     *  bb[2] - bottom-right
     *  bb[3] - bottom-left
     *
     * Note: bb[0] equal to getTopLeft() if box has no rotation.
     */
    public Point[] getBoundingBox() {
        return null;
    }

    /**
     * @param b Box to compare to this box.
     * @return True if this box and b is centered horizontally.
     *
     * @pre This box and b both has no rotation.
     */
    public boolean isCentered(Box b) {
        return false;
    }

    /**
     * Returns the top-left point of this rotated box. For this method
     * and the similar below, note that e.g "top-left" is relative to
     * the box, not screen.
     */
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
    public Point getMid() {
        return null;
    }
}
