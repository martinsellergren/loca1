package map;

import java.util.Arrays;

/**
 * A box located in an integer-grid, with some rotation.
 * Low x is left, low y is up, in [x,y].
 *
 * @inv width,height > 0
 */
public class Box {
    public/***/ double[] topL;
    public/***/ double[] topR;
    public/***/ double height;


    /**
     * Constructs a box from known data.
     */
    public Box(double[] topL, double[] topR, double height) {
        if (Math2.distance(topL, topR) == 0)
            throw new IllegalArgumentException("Zero box width");
        if (height == 0)
            throw new IllegalArgumentException("Zero box height");

        this.topL = topL;
        this.topR = topR;
        this.height = height;
    }

    /**
     * Constructs a Box from four corner points [x,y].
     * These corner-points will all fit inside the box.
     */
    public Box(double[] topL, double[] topR, double[] bottomR, double[] bottomL) {
        //this(Math2.toDouble(topL), Math2.toDouble(topR), (Math2.distance(topL, bottomL) + Math2.distance(topR, bottomR)) / 2);

        double rotLR1 = (Math2.angle( Math2.minus(topR, topL) ) +
                        Math2.angle( Math2.minus(bottomR, bottomL) )) / 2;
        double rotTB = (Math2.angle( Math2.minus(bottomL, topL) ) +
                        Math2.angle( Math2.minus(bottomR, topR) )) / 2;

        double rotLR2 =
            (Math.abs(Math2.toUnitDegrees(rotTB + 90) - rotLR1) <
             Math.abs(Math2.toUnitDegrees(rotTB - 90) - rotLR1)) ?
            Math2.toUnitDegrees(rotTB + 90) :
            Math2.toUnitDegrees(rotTB - 90);

        double rotLR = (rotLR1 + rotLR2) / 2;
        //System.out.format("%s, %s, %s\n", rotLR1, rotLR2, rotLR);

        double[] mid = Math2.mean(new double[][]{topL, topR, bottomR, bottomL});
        double[] lr = Math2.getDirVector(rotLR);
        double[] ud = Math2.rotate(lr, -90);

        double[] upperBound_p = topL;
        if (Math2.intersectDistance(topL, lr, topR, Math2.minus(mid, topR))[1] > 0)
            upperBound_p = topR;

    }
    public Box(int[] topL, int[] topR, int[] bottomR, int[] bottomL) {
        this(Math2.toDouble(topL), Math2.toDouble(topR), Math2.toDouble(bottomR), Math2.toDouble(bottomL));
    }

    public Box copy() {
        return new Box(new double[]{topL[0], topL[1]},
                       new double[]{topR[0], topR[1]},
                       height);
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
        double[] r = Math2.minus(topR, topL);
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
    public void addOffset(double addX, double addY) {
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
    public double[] getTopLeft() {
        return this.topL;
    }

    public double[] getTopRight() {
        return this.topR;
    }

    public double[] getBottomLeft() {
        return Math2.step(this.topL, getOrtoDirVector(), this.height);
    }

    public double[] getBottomRight() {
        return Math2.step(this.topR, getOrtoDirVector(), this.height);
    }

    public double[] getTopMid() {
        return new double[]{ (topL[0] + topR[0]) / 2,
                             (topL[1] + topR[1]) / 2 };
    }

    public double[] getBottomMid() {
        return Math2.step(getTopMid(), getOrtoDirVector(), this.height);
    }

    public double[] getLeftMid() {
        return Math2.step(this.topL, getOrtoDirVector(), this.height/2);
    }

    public double[] getRightMid() {
        return Math2.step(this.topR, getOrtoDirVector(), this.height/2);
    }

    public double[] getMid() {
        return Math2.step(getTopMid(), getOrtoDirVector(), this.height/2);
    }

    /**
     * @return [xmin, ymin, xmax, ymax]
     */
    public double[] getBounds() {
        double[] tl = getTopLeft();
        double[] tr = getTopRight();
        double[] br = getBottomRight();
        double[] bl = getBottomLeft();

        return new double[] {
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
            Math2.same(this.getTopLeft(), b.getTopLeft()) &&
            Math2.same(this.getTopRight(), b.getTopRight()) &&
            Math2.same(this.getHeight(), b.getHeight());
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
    //     public/***/ int x;
    //     public/***/ int y;
    //     public/***/ int xMax;
    //     public/***/ int yMax;

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
