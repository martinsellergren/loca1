package map;

import java.util.Arrays;

/**
 * A box located in an integer-grid, with some rotation.
 * Low x is left, low y is up, in [x,y].
 *
 * @inv width,height > 0
 */
public class Box {
    public/***/ int[] topL;
    public/***/ int[] topR;
    public/***/ double height;


    /**
     * Constructs a box with known height.
     */
    public Box(int[] topL, int[] topR, double height) {
        if (Math2.distance(topL, topR) <= 0)
            throw new IllegalArgumentException("Zero box width");
        if (height <= 0)
            throw new IllegalArgumentException("Zero box height");

        this.topL = topL;
        this.topR = topR;
        this.height = height;
    }

    /**
     * Constructs a Box from four corner points [x,y].
     * Top points are final. Rest is calculated to create an actual
     * rectangular box.
     */
    public Box(int[] topL, int[] topR, int[] bottomR, int[] bottomL) {
        this(topL, topR, (Math2.distance(topL, bottomL) + Math2.distance(topR, bottomR)) / 2);
    }

    public Box copy() {
        return new Box(topL, topR, height);
    }

    /**
     * @return Rotation of box with horizontal axis, in degrees.
     */
    public double getRotation() {
        return Math2.angle(this.getDirVector());
    }

    /**
     * @return Normalized directional vector (from left to right).
     */
    public double[] getDirVector() {
        int[] r = Math2.minus(topR, topL);
        return Math2.normalize(r);
    }

    /**
     * @return Normalized ortogonal dir vector (from up to down).
     */
    public double[] getOrtoDirVector() {
        return Math2.rotate(this.getDirVector(), -90);
    }

    /**
     * @return Box width.
     */
    public double getWidth() {
        return Math2.distance(topL, topR);
    }

    /**
     * @return Box height.
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Add offset box position.
     */
    public void addOffset(int addX, int addY) {
        this.topL[0] += addX;
        this.topL[1] += addY;
        this.topR[0] += addX;
        this.topR[1] += addY;
    }

    /**
     * Returns the top-left point of this rotated box. For this method
     * and the similar below, note that e.g "top-left" is relative to
     * the box, not screen. Rounds to whole pixels by standard method.
     */
    public int[] getTopLeft() {
        return this.topL;
    }

    public int[] getTopRight() {
        return this.topR;
    }

    public int[] getBottomLeft() {
        double[] tl = Math2.toDouble(topL);
        double[] bl = Math2.step(tl, getOrtoDirVector(), this.height);
        return Math2.toInt(bl);
    }

    public int[] getBottomRight() {
        double[] tr = Math2.toDouble(topR);
        double[] br = Math2.step(tr, getOrtoDirVector(), this.height);
        return Math2.toInt(br);
    }

    public int[] getTopMid() {
        return new int[]{ Math.round((topL[0]+topR[0]) / 2f),
                          Math.round((topL[1]+topR[1]) / 2f) };
    }

    public int[] getBottomMid() {
        double[] tm = new double[]{ (topL[0]+topR[0]) / 2d,
                                    (topL[1]+topR[1]) / 2d };
        double[] bm = Math2.step(tm, getOrtoDirVector(), this.height);
        return Math2.toInt(bm);
    }

    public int[] getLeftMid() {
        double[] tl = Math2.toDouble(topL);
        double[] ml = Math2.step(tl, getOrtoDirVector(), this.height/2);
        return Math2.toInt(ml);
    }

    public int[] getRightMid() {
        double[] tr = Math2.toDouble(topR);
        double[] mr = Math2.step(tr, getOrtoDirVector(), this.height/2);
        return Math2.toInt(mr);
    }

    public int[] getMid() {
        double[] tm = new double[]{ (topL[0]+topR[0]) / 2,
                                    (topL[1]+topR[1]) / 2 };
        double[] m = Math2.step(tm, getOrtoDirVector(), this.height/2);
        return Math2.toInt(m);
    }

    /**
     * @return [xmin, ymin, xmax, ymax]
     */
    public int[] getBounds() {
        int[] tl = getTopLeft();
        int[] tr = getTopRight();
        int[] br = getBottomRight();
        int[] bl = getBottomLeft();

        return new int[] {
            Math.min(Math.min(tl[0], tr[0]), Math.min(br[0], bl[0])),
            Math.min(Math.min(tl[1], tr[1]), Math.min(br[1], bl[1])),
            Math.max(Math.max(tl[0], tr[0]), Math.max(br[0], bl[0])),
            Math.max(Math.max(tl[1], tr[1]), Math.max(br[1], bl[1])) };
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof Box == false) return false;
        Box b = (Box) o;

        return
            Arrays.equals(this.getTopLeft(), b.getTopLeft()) &&
            Arrays.equals(this.getTopRight(), b.getTopRight()) &&
            this.getHeight() == b.getHeight();
    }

    @Override
    public String toString() {
        return
            "topL: " + Arrays.toString(topL) + ", " +
            "topR: " + Arrays.toString(topR) + ", " +
            "height: " + height;
    }


    // /**
    //  * @return True if [x,y] is inside box.
    //  */
    // public boolean isInside(int x, int y) {
    //     return true;
    // }

    // /**
    //  * Iterator for points inside this box. Returned in no
    //  * particular order.
    //  */
    // public class BoxPointIterator {
    //     private int x;
    //     private int y;
    //     private int xMax;
    //     private int yMax;

    //     public BoxPointIterator() {
    //         int[] bs = getBounds();
    //         x = bs[0];
    //         y = bs[1];
    //         xMax = bs[2];
    //         yMax = bs[3];
    //     }

    //     public int[] next() {
    //         for ( ; y <= yMax; y++) {
    //             for ( ; x <= xMax; x++) {
    //                 if (isInside(x, y)) {
    //                     return new int[]{x++, y};
    //                 }
    //             }
    //             x = 0;
    //         }

    //         return null;
    //     }
    // }
}
