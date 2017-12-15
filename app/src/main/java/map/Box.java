package map;

/**
 * A box located in an integer-grid, with some rotation.
 *
 * @inv width,height > 0
 */
public class Box {
    private int[] topL;
    private int[] topR;
    private double height;

    /**
     * Constructs a Box from four corner points [x,y].
     * Top points are final. Rest is calculated to create an actual
     * rectangular box.
     */
    public Box(int[] topL, int[] topR, int[] bottomR, int[] bottomL) {
    }

    /**
     * @return Rotation of box with horizontal axis, in degrees.
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
    public int[] getTopLeft() {
        return null;
    }
    public int[] getTopRight() {
        return null;
    }
    public int[] getBottomLeft() {
        return null;
    }
    public int[] getBottomRight() {
        return null;
    }
    public int[] getTopMid() {
        return null;
    }
    public int[] getBottomMid() {
        return null;
    }
    public int[] getLeftMid() {
        return null;
    }
    public int[] getRightMid() {
        return null;
    }
    public int[] getMid() {
        return null;
    }

    /**
     * @return [xmin, ymin, xmax, ymax]
     */
    public double[] getBounds() {
        return null;
    }
}
