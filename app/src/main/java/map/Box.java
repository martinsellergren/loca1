package map;

import java.util.Arrays;
import java.util.LinkedList;
import static loca.Utils.*;

/**
 * A box located in an integer-grid, with some rotation.
 * Low x is left, low y is up, in [x,y].
 *
 * @inv width,height > 0
 */
public class Box {

    /**
     * Throws an exception if tries to construct the box from
     * less than this points. */
    public static final int MIN_NO_POINTS_FOR_BOX_FITTING = 10;

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
     * Constructs a box by fitting a rotated rectangle to points.
     * Every point will fit inside the resulting box.
     *
     * @paran ps Points making up something like a the symbol [
     * (at any rotation).
     */
    public Box(LinkedList<int[]> ps) {
        if (ps.size() < MIN_NO_POINTS_FOR_BOX_FITTING)
            throw new IllegalArgumentException("Too few points");

        double[][] cs = fitRectangle(ps);
        cs = orderByDirection(cs, ps);

        this.topL = cs[0];
        this.topR = cs[1];
        this.height = Math2.distance(cs[0], cs[3]);

        if (!Math2.same(Math2.distance(cs[0], cs[3]), Math2.distance(cs[1], cs[2])))
            throw new RuntimeException();
        if (Math2.distance(topL, topR) == 0)
            throw new IllegalArgumentException("Zero box width");
        if (height == 0)
            throw new IllegalArgumentException("Zero box height");
    }

    /**
     * @return A deep copy.
     */
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

    /**
     * Splits box into a grid of blocks, layed out in one row.
     *
     * @param rows Number of rows in grid.
     * @param cols Number of cols in grid.
     * @return Blocks in grid when this box is split.
     */
    public Box[] split(int rows, int cols) {
        double blockW = getWidth() / cols;
        double blockH = getHeight() / rows;
        Box[] blocks = new Box[rows * cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double[] tl = getTopLeft();
                tl = Math2.step(tl, getDirVector(), blockW * c);
                tl = Math2.step(tl, getOrtoDirVector(), blockH * r);
                double[] tr = Math2.step(tl, getDirVector(), blockW);
                double[] bl = Math2.step(tl, getOrtoDirVector(), blockH);
                blocks[r*cols + c] = new Box(tl, tr, Math2.distance(tl, bl));
            }
        }
        return blocks;
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
    // public/***/ static double[][] fitRectangle(LinkedList<int[]> ps) {
    //     ps = getOuterPoints(ps);

    //     double step = 10;
    //     double r = -step;
    //     double fact = 0.3;
    //     double lastArea = Double.MAX_VALUE;
    //     double[][] cs = null;

    //     for (int i = 0; i < 15; i++) {
    //         r += step;
    //         cs = fitFromRotation(r, ps);
    //         double a = Math2.area(cs);
    //         if (a > lastArea) step *= -fact;
    //         lastArea = a;
    //     }

    //     return cs;
    // }
    public/***/ static double[][] fitRectangle(LinkedList<int[]> ps) {
        ps = getOuterPoints(ps);

        double minArea = Double.MAX_VALUE;
        double[][] bestCs = null;

        for (double r = 0; r < 90; r+=5) {
            double[][] cs = fitFromRotation(r, ps);
            double area = Math2.area(cs);

            if (area < minArea) {
                minArea = area;
                bestCs = cs;
            }
        }
        return bestCs;
    }

    /**
     * @param ps Points.
     * @return Points in ps with at least one side "free".
     */
    public/***/ static LinkedList<int[]> getOuterPoints(LinkedList<int[]> ps) {
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
    public/***/ static double[][] fitFromRotation(double rot, LinkedList<int[]> ps) {
        double[] lr = Math2.getDirVector(rot);
        double[] ud = Math2.rotate(lr, -90);
        double[] mid = Math2.mean(ps);

        int[][] lrExtremes = furthestFromLine(mid, ud, ps);
        int[][] udExtremes = furthestFromLine(mid, lr, ps);
        double[] left = Math2.toDouble(lrExtremes[1]);
        double[] right = Math2.toDouble(lrExtremes[0]);
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
     * Walk along provided line, turn left (any angle) towards pLeft
     * and right (any angle) towards pRight.
     *
     * @param p Point.
     * @param v Vector. p+t*v='the line', (t<-R)
     * @param ps List of points.
     * @return [pLeft, pRight]
     */
    public/***/ static int[][] furthestFromLine(double[] p, double[] v, LinkedList<int[]> ps) {
        double minD = Double.POSITIVE_INFINITY;
        double maxD = Double.NEGATIVE_INFINITY;
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
                maxI = i;
            }
        }

        return new int[][]{ ps.get(minI), ps.get(maxI) };
    }

    /**
     * Order given cornerpoints by direction determined from the
     * box-points (the opening in the [ ).
     *
     * @param cs Four corner-points of a box, ordered consecutively.
     * @param bps Box-points.
     * @return [topL, topR, bottomR, bottomL]
     */
    public/***/ static double[][] orderByDirection(double[][] cs, LinkedList<int[]> ps) {
        double SUFFICIENT_OPENING_SIZE = 1.5;

        int shortSideI = 0;
        if (Math2.distance(cs[0], cs[1]) > Math2.distance(cs[1], cs[2]))
            shortSideI = 1;

        double os0 = openingSize(cs[ shortSideI ], cs[ shortSideI+1 ], ps);
        double os2 = openingSize(cs[ shortSideI+2 ], cs[ (shortSideI+3)%4 ], ps);
        int trI = shortSideI;
        if (os0 < os2) trI = shortSideI+2;

        if (Math.max(os0, os2) < SUFFICIENT_OPENING_SIZE) {
            int longSideI = shortSideI+1;
            double os1 = openingSize(cs[ longSideI ], cs[ longSideI+1 ], ps);
            double os3 = openingSize(cs[ (longSideI+2)%4 ], cs[ (longSideI+3)%4 ], ps);
            trI = longSideI;
            if (os1 < os3) trI = (longSideI+2) % 4;
        }

        double[] tr = cs[ trI ];
        double[] br = cs[ (trI+1)%4 ];
        double[] bl = cs[ (trI+2)%4 ];
        double[] tl = cs[ (trI+3)%4 ];

        return new double[][]{tl, tr, br, bl};
    }

    /**
     * Start in middle between p0,p1, walk towards point-center,
     * how long til point?
     *
     * @param ps Points making up something like the symbol [.
     * @return "Opening size indicator"
     */
    public/***/ static double openingSize(double[] p0, double[] p1, LinkedList<int[]> ps) {
        int[] start = Math2.toInt(Math2.mean(new double[][]{p0, p1}));
        int[] mid = Math2.toInt(Math2.mean(ps));
        double length = Math2.distance(start, mid) * 1.5;
        int[] end = Math2.step(start, Math2.toDouble(Math2.minus(mid, start)), length);

        PixelWalk pw = new PixelWalk(start, end);
        int[] p;

        while ((p = pw.next()) != null) {
            if (Math2.contains(p, ps)) return Math2.distance(p, start);
        }

        LOGGER.severe("Box.openingSize(): Bad box-shape");
        return 0;
    }
}
