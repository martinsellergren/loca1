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
    public/***/ static final int DEFAULT_ALPHA_THRESHOLD = 130;

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
     * @return Iterator's next label layout. Null if no more.
     */
    public LabelLayout next() {
        int[] p = findBoxPoint(this.startX, this.startY);
        if (p == null) return null;

        LinkedList<Box> row = expandToRow(p);
        LabelLayout lay = new LabelLayout(row);
        boolean up = true;
        addRows(up, row, lay);
        up = false;
        addRows(up, row, lay);

        removeLabel(lay);
        this.startX = p[0];
        this.startY = p[1];

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
            int[] boxP = b.getTopLeft();
            LinkedList<int[]> ps = expandToBoxPoints(boxP);

            for (int[] p : ps) map[ p[0] ][ p[1] ] = false;
        }
    }

    /**
     * Finds all boxed-points connected to the start-point and
     * created a box from these points.
     *
     * @param start Start-point.
     * @return The box containing start-point.
     *
     * @pre start is a box-point in the map.
     */
    public/***/ Box expandToBox(int[] start) {
        LinkedList<int[]> ps = expandToBoxPoints(start);
        int[][] cs = getCorners(ps);
        cs = orderByDirection(cs, ps);
        return new Box(cs[0], cs[1], cs[2], cs[3]);
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
            start = startBox.getLeftMid();
            end = Math2.step(start, startBox.getDirVector(), -searchLength);
        }
        else {
            start = startBox.getRightMid();
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
     * @param start Start-point.
     * @return All points connected to the start-point.
     *
     * @pre start is a box-point in the map.
     */
    public/***/ LinkedList<int[]> expandToBoxPoints(int[] start) {
        if (!isBoxPoint(start)) throw new RuntimeException();

        LinkedList<int[]> open = new LinkedList<int[]>();
        LinkedList<int[]> closed = new LinkedList<int[]>();
        open.add(start);

        while (open.size() > 0) {
            int[] current = open.removeFirst();
            LinkedList<int[]> ns = getBoxPointNeighbors(current);
            LinkedList<int[]> uns = getUniquePoints(ns, open, closed);
            //System.out.println(ns.size() + ", " + uns.size());
            open.addAll(uns);
            closed.add(current);

            // System.out.println(Arrays.toString(current));
            // System.out.println(open.toString());
            // System.out.println(closed.toString() + "\n");
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
     * @return List of points in ps not in the other lists.
     */
    public/***/ LinkedList<int[]> getUniquePoints(LinkedList<int[]> ps, LinkedList<int[]> notIn1, LinkedList<int[]> notIn2) {
        LinkedList<int[]> rs = new LinkedList<int[]>();

        for (int[] p : ps) {
            if (!containsPoint(p, notIn1) && !containsPoint(p, notIn2)) {
                rs.add(p);
            }
        }
        return rs;
    }

    /**
     * @return true if ps contains p (by value)
     */
    public boolean containsPoint(int[] p, LinkedList<int[]> ps) {
        for (int[] pp : ps) {
            if (p[0] == pp[0] && p[1] == pp[1]) return true;
        }
        return false;
    }

    /**
     * From an array of points (making up something like a box),
     * finds the corner-points.
     *
     * @return corners[0] - top left
     *         corners[1] - top right
     *         corners[2] - bottom right
     *         corners[3] - bottom left
     */
    public/***/ int[][] getCorners(LinkedList<int[]> ps) {
        int[][] es = findExtremes(ps);
        if (es == null) {
            LinkedList<int[]> rps = rotatePoints(ps, 45);
            es = findExtremes(rps);
        }
        if (es == null) throw new RuntimeException();
        return es;
    }

    /**
     * @return [0]Leftmost, [1]upmost, [2]rightmost and [3]downmost
     * points in given array, or NULL if unclear which any of those
     * are.
     */
    public/***/ int[][] findExtremes(LinkedList<int[]> ps) {
        return null;
    }

    /**
     * @param deg Rotation if degrees, counterclockwise.
     * @return Same points but rotated around (0,0).
     */
    public/***/ LinkedList<int[]> rotatePoints(LinkedList<int[]> ps, double deg) {
        return null;
    }

    /**
     * Order given cornerpoints by direction determined from all
     * box-points (the opening in the C).
     *
     * @param cs The four corner-points of the box in random order.
     * @param bps All box-points (including cs).
     * @return [topL, topR, bottomR, bottomL]
     */
    private int[][] orderByDirection(int[][] cs, LinkedList<int[]> bps) {
        return null;
    }

    /**
     * @param cs Random-ordered corner-points.
     * @return cs ordered consecutively (start somewhere and walk
     * whole way round..walk forwards or backwards).
     */
    private int[][] orderConsecutively(int[][] cs) {
        return null;
    }

    /**
     * Start in middle between p1,p2, walk NESW, how long is minimum
     * til box-point?
     */
    private int getBetweenSpace(int[] p1, int[] p2) {
        return 0;
    }


    /**
     * @return True if [x,y] is a box-point.
     */
    public/***/ boolean isBoxPoint(int x, int y) {
        if (y < 0 || y > map.length) return false;
        if (x < 0 || x > map[0].length) return false;

        return map[y][x];
    }
    public/***/ boolean isBoxPoint(int[] p) {
        return map[ p[1] ][ p[0] ];
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
