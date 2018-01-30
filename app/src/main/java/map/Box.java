package map;

import java.util.Arrays;
import java.util.LinkedList;

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
     * Constructs a box by fitting a rectangle to points.
     * Every point will fit inside the resulting box.
     *
     * @paran ps Points making up something like a the symbol [
     * (at any rotation).
     */
    public Box(LinkedList<int[]> ps) {
        double[][] cs = orderByDirection(fitRectangle(ps), ps);

        this.topL = cs[0];
        this.topR = cs[1];
        this.height = Math2.distance(cs[0], cs[3]);

        if (!Math2.same(Math2.distance(cs[0], cs[3]), Math2.distance(cs[1], cs[2])))
            throw new RuntimeException();
    }

    // /**
    //  * Constructs a Box from four corner points [x,y].
    //  * These corner-points will all fit inside the box.
    //  */
    // public Box(double[] topL, double[] topR, double[] bottomR, double[] bottomL) {
    //     //this(topL, topR, (Math2.distance(topL, bottomL) + Math2.distance(topR, bottomR)) / 2);

    //     double rotLR1 = (Math2.angle( Math2.minus(topR, topL) ) +
    //                     Math2.angle( Math2.minus(bottomR, bottomL) )) / 2;
    //     double rotTB = (Math2.angle( Math2.minus(bottomL, topL) ) +
    //                     Math2.angle( Math2.minus(bottomR, topR) )) / 2;

    //     double rotLR2 =
    //         (Math.abs(Math2.toUnitDegrees(rotTB + 90) - rotLR1) <
    //          Math.abs(Math2.toUnitDegrees(rotTB - 90) - rotLR1)) ?
    //         Math2.toUnitDegrees(rotTB + 90) :
    //         Math2.toUnitDegrees(rotTB - 90);

    //     double rotLR = (rotLR1 + rotLR2) / 2;
    //     //System.out.format("%s, %s, %s\n", rotLR1, rotLR2, rotLR);

    //     double[] mid = Math2.mean(new double[][]{topL, topR, bottomR, bottomL});
    //     double[] lr = Math2.getDirVector(rotLR);
    //     double[] ud = Math2.rotate(lr, -90);

    //     double[] leftP = topL;
    //     double[] topP = topL;
    //     double[] rightP = topR;
    //     double[] botP = bottomL;
    //     if (Math2.intersectDistance(topL, Math2.step(topL, ud, 1), bottomL, mid)[1] > 0)
    //         leftP = bottomL;
    //     if (Math2.intersectDistance(topL, Math2.step(topL, lr, 1), topR, mid)[1] > 0)
    //         topP = topR;
    //     if (Math2.intersectDistance(topR, Math2.step(topR, ud, 1), bottomR, mid)[1] > 0)
    //         rightP = bottomR;
    //     if (Math2.intersectDistance(bottomL, Math2.step(bottomL, lr, 1), bottomR, mid)[1] > 0)
    //         botP = bottomR;

    //     double[] leftP_ = Math2.step(leftP, ud, 1);
    //     double[] topP_ = Math2.step(topP, lr, 1);
    //     double[] rightP_ = Math2.step(rightP, ud, 1);
    //     double[] botP_ = Math2.step(botP, lr, 1);

    //     topL = Math2.intersectPoint(leftP, leftP_, topP, topP_);
    //     topR = Math2.intersectPoint(topP, topP_, rightP, rightP_);
    //     bottomR = Math2.intersectPoint(rightP, rightP_, botP, botP_);
    //     bottomL = Math2.intersectPoint(botP, botP_, leftP, leftP_);
    //     double h = Math2.distance(topL, bottomL);

    //     if (!Math2.same(h, Math2.distance(topR, bottomR)))
    //         throw new RuntimeException();

    //     this.topL = topL;
    //     this.topR = topR;
    //     this.height = h;
    // }
    // public Box(int[] topL, int[] topR, int[] bottomR, int[] bottomL) {
    //     this(Math2.toDouble(topL), Math2.toDouble(topR), Math2.toDouble(bottomR), Math2.toDouble(bottomL));
    // }

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
     * @return [topLeft, topRight, botRight, botLeft]
     */
    public double[][] getCorners() {
        return new double[][]{
                getTopLeft(),
                getTopRight(),
                getBottomRight(),
                getBottomLeft() };
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

    //*******************************************Rectangle fitting

    /**
     * Fits a rotated rectangle to points. Rectangle dimensions are
     * minimized to fit all points. Best rotation is found
     * ("sufficient" precision).
     *
     * @param ps Points that will all fit inside rectangle.
     * @return [c0,c1,c2,c3] of fit rectangle in consecutive order
     * (i.e start somewhere and walk whole way round in CLOCKWISE
     * direction).
     */
    private static double[][] fitRectangle(LinkedList<int[]> ps) {
        ps = getOuterPoints(ps);

        double bestFitness = Double.MIN_VALUE;
        double[][] bestCs = null;

        for (double r = 0; r < 45; r+=1) {
            double[][] cs = fitFromRotation(r, ps);
            double fitness = evaluateFit(cs, ps);

            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestCs = cs;
            }
        }

        return bestCs;
    }

    /**
     * @param ps Points.
     * @return Points in ps with at least one side "free".
     */
    private static LinkedList<int[]> getOuterPoints(LinkedList<int[]> ps) {
        LinkedList<int[]> outer = new LinkedList<int[]>();

        for (int[] p : ps) {
            int[] left = new int[]{ p[0]-1, p[1] };
            int[] up = new int[]{ p[0], p[1]-1 };
            int[] right = new int[]{ p[0]+1, p[1] };
            int[] down = new int[]{ p[0], p[1]+1 };

            if (!Math2.contains(left, ps) ||
                !Math2.contains(up, ps) ||
                !Math2.contains(right, ps) ||
                !Math2.contains(down, ps)) {
                outer.add(p);
            }
        }

        return outer;
    }

    /**
     * @param rot Rotation of fit rectangle.
     * @param ps Points inside fit rectangle.
     * @return [c0,c1,c2,c3] of fit rectangle ordered consecutively.
     */
    private static double[][] fitFromRotation(double rot, LinkedList<int[]> ps) {
        double[] lr = Math2.getDirVector(rot);
        double[] ud = Math2.rotate(lr, -90);
        double[] mid = Math2.mean(ps);

        int[][] lrExtremes = furthestFromLine(mid, ud, ps);
        int[][] udExtremes = furthestFromLine(mid, lr, ps);
        double[] left = Math2.toDouble(lrExtremes[0]);
        double[] right = Math2.toDouble(lrExtremes[1]);
        double[] top = Math2.toDouble(udExtremes[0]);
        double[] bot = Math2.toDouble(udExtremes[1]);

        double[] tl = Math2.intersectPoint(left, ud, top, lr);
        double[] tr = Math2.intersectPoint(top, lr, right, ud);
        double[] br = Math2.intersectPoint(right, ud, bot, lr);
        double[] bl = Math2.intersectPoint(bot, lr, left, ud);

        return new double[][]{tl, tr, br, bl};
    }

    /**
     * Get the two points from a list of points furthest from a line.
     * One point on one side of the line, the other on the other side.
     * If no point is located on one of the sides, returns the closest
     * point (and the futhest) on the other side.
     *
     * @param p Point.
     * @param v Vector. p+t*v='the line', (t<-R)
     * @param ps List of points.
     * @return [p0, p1]
     */
    private static int[][] furthestFromLine(double[] p, double[] v, LinkedList<int[]> ps) {
        double minD = Double.MAX_VALUE;
        double maxD = Double.MIN_VALUE;
        int minI = -1;
        int maxI = -1;

        for (int i = 0; i < ps.size(); i++) {
            double[] q = Math2.toDouble(ps.get(i));
            double d = Math2.distance(q, p, v);

            if (d < minD) {
                minD = d;
                minI = i;
            }
            if (d > maxD) {
                maxD = d;
                minI = i;
            }
        }

        return new int[][]{ ps.get(minI), ps.get(maxI) };
    }

    /**
     * How good fits rectangle to points?
     *
     * @param cs [c0,c1,c2,c3] of rectangle ordered consecutively.
     * @param ps Points used for the rectangle-fit.
     * @return Goodness of fit, a value >=0 where low is good.
     * Implemented as sum of distances from every point to "distance to
     * nearest rectangle-side".
     */
    private static double evaluateFit(double[][] cs, LinkedList<int[]> ps) {
        double sum = 0;
        double[] lr = Math2.minus(cs[1], cs[0]);
        double[] ud = Math2.minus(cs[2], cs[1]);

        for (int[] p : ps) {
            double d0 = Math.abs(Math2.distance(p, cs[0], ud));
            double d1 = Math.abs(Math2.distance(p, cs[0], lr));
            double d2 = Math.abs(Math2.distance(p, cs[2], ud));
            double d3 = Math.abs(Math2.distance(p, cs[2], lr));
            sum += Math.min( Math.min(d0, d1), Math.min(d2, d3) );
        }

        return sum;
    }

    /**
     * Order given cornerpoints by direction determined from the
     * box-points (the opening in the [ ).
     *
     * @param cs Four corner-points of a box, ordered consecutively.
     * @param bps Box-points.
     * @return [topL, topR, bottomR, bottomL]
     */
    private static double[][] orderByDirection(double[][] cs, LinkedList<int[]> ps) {
        double SUFFICIENT_SPACE = 5;

        double maxS = Double.MIN_VALUE;
        int maxI = -1;

        for (int i = 0; i <= 3; i++) {
            double[] c0 = cs[ i ];
            double[] c1 = cs[ (i+1)%4 ];
            double s = findBetweenSpace(c0, c1, ps);

            if (s > maxS) {
                maxS = s;
                maxI = i;
            }
        }

        double[] tr = cs[ maxI ];
        double[] br = cs[ (maxI+1)%4 ];
        double[] bl = cs[ (maxI+2)%4 ];
        double[] tl = cs[ (maxI+3)%4 ];

        return new double[][]{tl, tr, br, bl};
    }

    /**
     * Start in middle between p0,p1, walk towards point-center,
     * how long til point?
     *
     * @param ps Points making up something like the symbol [.
     * @return "Opening-size indicator"
     */
    private static double findBetweenSpace(double[] p0, double[] p1, LinkedList<int[]> ps) {
        int[] start = Math2.toInt(Math2.mean(new double[][]{p0, p1}));
        int[] end = Math2.toInt(Math2.mean(ps));

        PixelWalk pw = new PixelWalk(start, end);
        int[] p;

        while ((p = pw.next()) != null) {
            if (Math2.contains(p, ps)) return Math2.distance(p, start);
        }

        throw new RuntimeException();
    }
}
