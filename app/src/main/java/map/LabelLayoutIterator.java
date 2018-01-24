package map;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Arrays;
import java.awt.Color;


/**
 * Iterator that finds labels (clusters of boxes) in a box-image
 * and returns each label's layout. Scans through a box-image,
 * finds a box-point, expands it into a label-layout.
 * Returns good and bad (e.g clipped, half outside of view) labels.
 *
 * A box-image is an image where only labels are showing and instead
 * of letters in the labels, there are boxes. Each box is formed like
 * the letter C (with straight lines) with the opening towards right
 * -to provide direction. The space-character is also represented as
 * a box. The box is sized and positioned so that corresponding letter
 * precisely fits inside. If the character is very short/thin the
 * box is given a min-width/height (which doesn't affect the
 * letter-spacing/box-spacing relationship).
 *
 * Note: the underlying font must comply with box-image constraints -
 * the font should be mono-spaced.
 *
 * The boxes (letters) form groups:
 *  -Rows of horizontally adjacent boxes.
 *  -Labels (multiple rows) separated by surrounding space.
 *
 * More underlying properties:
 *  -Labels are located anywhere in the image, at any rotation.
 *  -The baseline of a row might not be a straight line - letters
 *   are rotated accordingly folloing the row. Maximum angle change
 *   between adjacent characters is around 40deg.
 *  -Labels with multiple rows has no rotation and a straight base line.
 *
 * This class represents a box-image using a 2D boolean array (a "map").
 * If map[x][y] is True, this is a box-point.
 *
 * @pre No (or few..) letter-boxes touch.
 * @pre Horizontal space between boxes of same row is always less
 * than the width of any box in the label.
 * @pre Vertical space between boxes of neighboring rows is always
 * less than the heigh of any box in the label (...).
 * @pre Highest box is always shorter than 2*shortest box in same
 * label (i.e same font-size).
 */
public class LabelLayoutIterator {
    /** Pixels alpha-value-threshold where over means box-point. */
    public/***/ static final int DEFAULT_ALPHA_THRESHOLD = 100;

    /** Value used when finding corners. Min distance between corners.*/
    public/**/ static final double MIN_CORNER_PADDING = 2.5;

    /** Start searching for next box-point at this pos in map. */
    public/***/ int startX = 0;
    public/***/ int startY = 0;

    /** Representation of box-image: map[row][column]. */
    public/***/ boolean[][] map;

    /**
     * Constructs the iterator from an rgba-image (box-image).
     * Close to transparent pixels are marked as non-box-point.
     * Lowest alpha value still counted as box-point is the
     * alpha-threshold.
     *
     * @param img A box-image (an rgba-image).
     * @param alphaThreshold Pixels alpha-value-threshold where
     * over means box-point, under means non-box-point.
     *
     * @pre 0 <= alphaThreshold <= 255
     */
    public LabelLayoutIterator(BasicImage img, int alphaThreshold) {
        this.map = new boolean[img.getHeight()][img.getWidth()];

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = img.getColor(x, y);
                int alpha = c.getAlpha();
                if (alpha >= alphaThreshold) {
                    this.map[y][x] = true;
                }
                else {
                    this.map[y][x] = false;
                }
            }
        }
    }

    /**
     * Constructor for using default value for alphaThreshold.
     */
    public LabelLayoutIterator(BasicImage img) {
        this(img, DEFAULT_ALPHA_THRESHOLD);
    }

    /**
     * Finds and returns next layout. Starts searching at startX
     * startY, and sets this to found box-point. Removes
     * found label from map so it's not found again.
     *
     * @return Iterator's next label layout. NULL if no more.
     */
    public LabelLayout next() {
        int[] p = findBoxPoint(this.startX, this.startY);
        if (p == null) return null;

        this.startX = p[0];
        this.startY = p[1];

        Box box = expandToBox(p);
        if (box == null) {
            expandAndRemove(p);
            return next();
        }

        LinkedList<Box> row = expandToRow(p);
        LabelLayout lay = new LabelLayout(row);
        boolean up = true;
        addRows(up, row, lay);
        up = false;
        addRows(up, row, lay);

        removeLabel(lay);
        return lay;
    }

    /**
     * Scans through the map, looking for a box-point.
     *
     * @return A box-point in the map, or NULL if no more
     * box-points, as [x,y].
     */
    public/***/ int[] findBoxPoint(int startX, int startY) {
        for (int y = startY; y < map.length; y++) {
            for (int x = startX; x < map[y].length; x++) {
                if (isBoxPoint(x, y))
                    return new int[]{x, y};
            }
        }
        return null;
    }

    /**
     * Expand one box-point to a box and the box to a horizontal
     * row of boxes.
     *
     * @param start Start-point.
     * @return A row in the map (i.e horizontaly
     * adjacent boxes).
     *
     * @pre start is a box-point in the map.
     */
    public/***/ LinkedList<Box> expandToRow(int[] start) {
        return null;
    }

    /**
     * Adds, to a label-layout, all rows above or below some
     * start-row in same label in the map.
     *
     * @param up If true, adds rows above startRow, otherwise below.
     * @param startRow Start-row.
     * @param layout Accumulator for the new rows.
     * @return The provided label-layout, prepended/appended
     * with all rows above/below in same label as start-row.
     *
     * @pre startRow a row of some label in the map, that has
     * no rotation and a straight base-line.
     */
    public/***/ LabelLayout addRows(boolean up, LinkedList<Box> startRow, LabelLayout layout) {
        return null;
    }

    /**
     * Removes a label from the map, i.e deactivates all its
     * points. Goes through the label-layout and deactivates every
     * point of every letter-box in the layout. Extends the size of
     * the letter-box-area slightly to ensure that no point is
     * missed.
     *
     * @param layout Layout for the label to be removed.
     */
    public/***/ void removeLabel(LabelLayout layout) {
        for (Box b : layout.getBoxes()) {
            int[] boxP = Math2.toInt(b.getMid());
            expandAndRemove(boxP);
        }
    }

    /**
     * Expands point to all connecting box-points, and removes them.
     */
    public/***/ void expandAndRemove(int[] p) {
        LinkedList<int[]> ps = expandToBoxPoints(p);
        for (int[] q : ps) map[ q[1] ][ q[0] ] = false;
    }

    /**
     * Adds, to an array, all boxes to the left or right of some
     * start-box, in same row of some label in the map.
     *
     * @param left If true, adds boxes to the left of startBox,
     * otherwise to the right.
     * @param startBox Start-box.
     * @param bs Accumulator for the new boxes.
     * @return The provided box-array (bs) prepended/appended
     * with all boxes to left/right of that box (in same label).
     *
     * @pre startBox a letter-box in the map.
     */
    public/***/ Box[] addBoxes(boolean left, Box startBox, LinkedList<Box> bs) {
        return null;
    }

    /**
     * Finds a neighboring row of a start row.
     * Looks either up or down from the start row. A neighboring
     * row is an adjacent row in same label as the start-row.
     *
     * Conditions for beeing a neighboring row:
     *  -No rotation (like start-row).
     *  -In close proximity (up/down) to start-row (~box-height).
     *  -Generally same line-height as start-row.
     *  -Centered in relation to start-row.
     *
     * @param up Search up, otherwise down.
     * @param startRow Start row.
     * @return A neighbor-row either up or down of startRow,
     * or NULL if such a neighbor-row doesn't exist.
     *
     * @pre startRow a row in the map (i.e horizontaly
     * adjacent boxes) with no apparent rotation and a straight
     * baseline.
     */
    public/***/ int[] findNeighborRow(boolean up, LinkedList<Box> startRow) {
        return null;
    }

    /**
     * Finds a neighboring box of a start-box.
     * Looks to either left or right side of the start box.
     * A neighboring box is an adjacent box in same label as
     * the start-box.
     *
     * Conditions for beeing a neighboring box:
     *  -In close proximity to left/right of start-box (~box-width).
     *  -At about the same rotation as start-box (~40deg).
     *  -At about same size as start-box (~height*2)
     *
     * @param left Search left, otherwise right.
     * @param sb Start box.
     * @return A left/right neighbor-box, or NULL
     * if such a neighbor-box doesn't exist.
     *
     * @pre startBox a box in the map.
     */
    public Box findNeighborBox(boolean left, Box sb) {
        int[] bp = findNeighborBoxPoint(left, sb);
        Box b = expandToBox(bp);
        if (b == null) return null;

        if (Math2.angleDiff(b.getRotation(), sb.getRotation()) <= 40
            && sb.getHeight() < 2 * b.getHeight()
            && sb.getHeight() > 0.5 * b.getHeight()) {
            return b;
        }
        return null;
    }

    public/***/ int[] findNeighborBoxPoint(boolean left, Box startBox) {
        int[] start, end;
        double searchLength = startBox.getWidth() * 1.3;

        if (left) {
            start = Math2.toInt(startBox.getLeftMid());
            end = Math2.step(start, startBox.getDirVector(), -searchLength);
        }
        else {
            start = Math2.toInt(startBox.getRightMid());
            end = Math2.step(start, startBox.getDirVector(), searchLength);
        }

        PixelWalk pw = new PixelWalk(start, end);
        int[] p;
        while ((p = pw.next()) != null) {
            if (isBoxPoint(p)) return p;
        }

        return null;
    }

    /**
     * Finds all boxed-points connected to the start-point and
     * created a box from these points.
     *
     * @param start Start-point.
     * @return The box containing start-point, or NULL if start-point
     * isn't part of a rectangular shape of box-points.
     *
     * @pre start is a box-point in the map.
     */
    public/***/ Box expandToBox(int[] start) {
        LinkedList<int[]> ps = expandToBoxPoints(start);
        int[][] cs = findCorners(ps);
        if (cs == null) return null;

        return new Box(cs[0], cs[1], cs[2], cs[3]);
    }

    /**
     * @param start Start-point.
     * @return All points connected to the start-point.
     *
     * @pre start is a box-point in the map.
     */
    public/***/ LinkedList<int[]> expandToBoxPoints(int[] start) {
        if (!isBoxPoint(start)) throw new RuntimeException("Not bp");

        LinkedList<int[]> open = new LinkedList<int[]>();
        LinkedList<int[]> closed = new LinkedList<int[]>();
        open.add(start);

        while (open.size() > 0) {
            int[] current = open.removeFirst();
            LinkedList<int[]> ns = getBoxPointNeighbors(current);
            LinkedList<int[]> uns = Math2.getUniquePoints(ns, open, closed);
            open.addAll(uns);
            closed.add(current);
        }
        return closed;
    }

    /**
     * @return The box-point neighbors (left/up/right/down).
     * @pre p is box-point.
     */
    public/***/ LinkedList<int[]> getBoxPointNeighbors(int[] p) {
        if (!isBoxPoint(p)) throw new RuntimeException();

        int[] left = new int[]{ p[0]-1, p[1] };
        int[] up = new int[]{ p[0], p[1]-1 };
        int[] right = new int[]{ p[0]+1, p[1] };
        int[] down = new int[]{ p[0], p[1]+1 };

        LinkedList<int[]> ns = new LinkedList<int[]>();
        if (isBoxPoint(left)) ns.add(left);
        if (isBoxPoint(up)) ns.add(up);
        if (isBoxPoint(right)) ns.add(right);
        if (isBoxPoint(down)) ns.add(down);
        return ns;
    }

    /**
     * From an array of points (making up something like a box),
     * finds the corner-points.
     *
     * @param bps Complete set of box-points.
     * @return [topL, topR, bottomR, bottomL], or NULL is shape
     * isn't rectangle-like.
     */
    public/***/ int[][] findCorners(LinkedList<int[]> bps) {
        LinkedList<int[]> sps = getSidePoints(bps);
        if (sps.size() < 4) return null;

        int[] diag0 = Math2.getFurthest(sps);
        int[] c0 = sps.get( diag0[0] );
        int[] c1 = sps.get( diag0[1] );
        Math2.removeIndexes(diag0, sps);

        Math2.removeClose(c0, sps, MIN_CORNER_PADDING);
        Math2.removeClose(c1, sps, MIN_CORNER_PADDING);
        if (sps.size() < 2) return null;

        int[] diag1 = Math2.getFurthest(sps);
        int[] c2 = sps.get( diag1[0] );
        int[] c3 = sps.get( diag1[1] );

        int[][] cs = new int[][]{c0, c1, c2, c3};

        if (resemblesRectangleCorners(cs))
            return orderByDirection(cs, bps);
        else
            return null;
    }

    /**
     * @param Complete box-points.
     * @return Points in bps with at least one side "free".
     */
    private LinkedList<int[]> getSidePoints(LinkedList<int[]> bps) {
        LinkedList<int[]> sps = new LinkedList<int[]>();

        for (int[] p : bps) {
            if (getBoxPointNeighbors(p).size() > 0)
                sps.add(p);
        }
        return sps;
    }

    // public/***/ int[][] findCorners(LinkedList<int[]> ps) {
    //     int R = 27;

    //     LinkedList<double[]> cands = Math2.toDouble(getCornerCandidates(ps));
    //     if (cands.size() < 4) return null;
    //     else if (cands.size() == 4)
    //         return Math2.toInt(new double[][]{cands.get(0),
    //                                           cands.get(1),
    //                                           cands.get(2),
    //                                           cands.get(3)});

    //     int[][] es0 = Math2.toInt(getExtremes(cands));
    //     int[][] es1 = Math2.toInt(Math2.rotate(getExtremes(Math2.rotate(cands, R*1)), -R*1));
    //     int[][] es2 = Math2.toInt(Math2.rotate(getExtremes(Math2.rotate(cands, R*2)), -R*2));

    //     double cl0 = Math2.getCrossLength(es0);
    //     double cl1 = Math2.getCrossLength(es1);
    //     double cl2 = Math2.getCrossLength(es2);

    //     int[][] cs;
    //     if (cl0 > cl1 && cl0 > cl2)
    //         cs = es0;
    //     else if (cl1 > cl0 && cl1 > cl2)
    //         cs = es1;
    //     else
    //         cs = es2;

    //     if (resemblesRectangleCorners(cs)) return es0;
    //     else return null;
    // }

    // /**
    //  * @param bps Box-points.
    //  * @return All box-points in bps that has exactly two sides "free".
    //  */
    // private LinkedList<int[]> getCornerCandidates(LinkedList<int[]> bps) {
    //     LinkedList<int[]> cands = new LinkedList<int[]>();

    //     for (int[] p : bps) {
    //         if (getBoxPointNeighbors(p).size() == 2)
    //             cands.add(p);
    //     }

    //     return cands;
    // }

    // public/***/ int[][] findCorners(LinkedList<int[]> ps) {
    //     int STEP = 15;

    //     LinkedList<double[]> ps_d = Math2.toDouble(ps);
    //     LinkedList<int[]> cs0 = new LinkedList<int[]>();
    //     LinkedList<int[]> cs1 = new LinkedList<int[]>();
    //     LinkedList<int[]> cs2 = new LinkedList<int[]>();
    //     LinkedList<int[]> cs3 = new LinkedList<int[]>();
    //     LinkedList[] css = new LinkedList[]{cs0, cs1, cs2, cs3};
    //     initializeCorners(css);

    //     for (int r = 0; r < 360; r += STEP) {
    //         LinkedList<double[]> ps_ = Math2.rotate(ps_d, r);
    //         int[][] cs = Math2.toInt(Math2.rotate(findExtremes(ps_), -r));
    //         addToNearestCorner(cs[0], css);
    //         addToNearestCorner(cs[1], css);
    //         addToNearestCorner(cs[2], css);
    //         addToNearestCorner(cs[3], css);
    //             // Math2.getClosest(Math2.toDouble(cs[0]), new double[][]{
    //             //         Math2.mean(cs0),
    //             //         Math2.mean(cs1),
    //             //         Math2.mean(cs2),
    //             //         Math2.mean(cs3)});

    //     }

    //     int[][] cs = new int[][]{Math2.getMostOccuring(cs0),
    //                              Math2.getMostOccuring(cs1),
    //                              Math2.getMostOccuring(cs2),
    //                              Math2.getMostOccuring(cs3)};

    //     if (resemblesRectangleCorners(cs)) return cs;
    //     else return null;
    // }
    // public/***/ int[][] findCorners(LinkedList<int[]> psI) {
    //     double SAME_MAX_DISTANCE = 1.5;
    //     int STEP = 1;
    //     int MAX = 1000;

    //     LinkedList<double[]> ps = Math2.toDouble(psI);

    //     for (int r = 0; r <= MAX; r += STEP) {
    //         ps = Math2.rotate(ps, r);

    //         double[][] es = findExtremes(ps);
    //         double[][] es90 = findExtremes(Math2.rotate(ps, 33));
    //         double[][] es180 = findExtremes(Math2.rotate(ps, 44));
    //         double[][] es270 = findExtremes(Math2.rotate(ps, 55));

    //         double[][] es1 = Math2.rotate(es90, -33);
    //         double[][] es2 = Math2.rotate(es180, -44);
    //         double[][] es3 = Math2.rotate(es270, -55);

    //         if (Math2.same(es, es1, SAME_MAX_DISTANCE) &&
    //             Math2.same(es, es2, SAME_MAX_DISTANCE) &&
    //             Math2.same(es, es3, SAME_MAX_DISTANCE)) {

    //             int[][] cs = Math2.toInt(Math2.rotate(es, -r));
    //             //cs = forcePointsInside(cs);

    //             if (resemblesRectangleCorners(cs))
    //                 return cs;
    //             else
    //                 return null;
    //         }
    //     }
    //     throw new RuntimeException("Failed to make sence of box.");
    // }

    /**!
     * @param cs [left,upp,right,down]-corners
     * @return True if corners resemble those of a rectangle.
     */
    public static boolean resemblesRectangleCorners(int[][] cs) {
        return !Math2.hasDuplicates(cs);
    }

    /**
     * Order given cornerpoints by direction determined from all
     * box-points (the opening in the C).
     *
     * @param cs The four corner-points of the box in random order.
     * @param bps All box-points (including cs).
     * @return [topL, topR, bottomR, bottomL] or NULL if failed to
     * order (weird appearance).
     */
    public/***/ int[][] orderByDirection(int[][] cs, LinkedList<int[]> bps) {
        cs = orderConsecutively(cs);
        int[][][] legs = new int[][][]{new int[][]{cs[0], cs[1]},
                                       new int[][]{cs[1], cs[2]},
                                       new int[][]{cs[2], cs[3]},
                                       new int[][]{cs[3], cs[0]}};
        int maxS = Integer.MIN_VALUE;
        int maxS_index = -1;
        int i = -1;

        for (int[][] leg : legs) {
            i++;
            int[] p1 = leg[0];
            int[] p2 = leg[1];
            int s = findBetweenSpace(p1, p2, bps);

            if (s == -1) return null;

            if (s > maxS) {
                maxS = s;
                maxS_index = i;
            }
        }

        int[][] rightLeg = legs[maxS_index];
        int[] topR = rightLeg[0];
        int[] bottomR = rightLeg[1];

        int[][] leftLeg = legs[(maxS_index + 2) % 4];
        int[] topL = leftLeg[1];
        int[] bottomL = leftLeg[0];

        return new int[][]{topL, topR, bottomR, bottomL};
    }

    /**
     * @param cs Random-ordered corner-points.
     * @return cs ordered consecutively (start somewhere and walk
     * whole way round in CLOCKWISE direction).
     */
    public/***/ int[][] orderConsecutively(int[][] cs) {
        int[] diag = Math2.getFurthest(cs);
        int headI = diag[0];
        int oppositeI = diag[1];

        int neighborIs[] = Math2.getComplement(diag, new int[]{0,1,2,3});

        // System.out.println(Arrays.deepToString(cs));
        // System.out.println(cs.length);
        // System.out.println(neighborIs.length);
        // System.out.println("");

        int[] p0 = cs[ neighborIs[0] ];
        int[] p1 = cs[ headI ];
        int[] p2 = cs[ neighborIs[1] ];
        int[] p3 = cs[ oppositeI ];

        if (Math2.rightTurn(p0, p1, p2))
            return new int[][]{p0, p1, p2, p3};
        else
            return new int[][]{p3, p2, p1, p0};
    }

    /**
     * Start in middle between p1,p2, walk towards box-points' center,
     * how long til box-point?
     *
     * @return Box-point distance, or -1 if no between-space..
     */
    public/***/ int findBetweenSpace(int[] p1, int[] p2, LinkedList<int[]> bps) {
        int[] start = Math2.toInt(Math2.mean(new int[][]{p1, p2}));
        int[] mid = Math2.toInt(Math2.mean(bps));
        double[] dir = Math2.toDouble(Math2.minus(mid, start));
        double length = Math2.distance(start, mid) * 2;
        int[] end = Math2.step(start, dir, length);

        PixelWalk pw = new PixelWalk(start, end);
        int[] p;

        for (int d = 0; (p = pw.next()) != null; d++) {
            if (isBoxPoint(p)) return d;
        }

        return -1;
        //throw new RuntimeException();
    }


    /**
     * @return True if [x,y] is a box-point.
     */
    public/***/ boolean isBoxPoint(int x, int y) {
        if (y < 0 || y >= map.length) return false;
        if (x < 0 || x >= map[0].length) return false;

        return map[y][x];
    }
    public/***/ boolean isBoxPoint(int[] p) {
        return isBoxPoint(p[0], p[1]);
    }


    //*********************************FOR TESTING

    public BasicImage toImg() {
        BasicImage img = new BasicImage(map[0].length, map.length);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x])
                    img.setColor(x, y, Color.BLACK);
            }
        }
        return img;
    }

    /**
     * Color points in ps black.
     */
    public static BasicImage toImg(LinkedList<int[]> ps) {
        int xmin = Integer.MAX_VALUE;
        int ymin = Integer.MAX_VALUE;
        int xmax = 0;
        int ymax = 0;
        for (int[] p : ps) {
            if (p[0] < xmin) xmin = p[0];
            if (p[1] < ymin) ymin = p[1];
            if (p[0] > xmax) xmax = p[0];
            if (p[1] > ymax) ymax = p[1];
        }

        BasicImage img = new BasicImage(xmax+1-xmin, ymax+1-ymin);
        for (int[] p : ps)
            img.setColor(p[0]-xmin, p[1]-ymin, Color.BLUE);

        return img;
    }
}
