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
 * the symbol [ with the opening towards right -to provide direction.
 * The space-character is also represented as a box.
 * The box is sized and positioned so that corresponding letter
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
 *   between adjacent characters is 15deg.
 *  -Labels with multiple rows has no rotation and a straight base line.
 *
 * This class represents a box-image using a 2D boolean array (a "map").
 * If map[x][y] is True, this is a box-point.
 *
 * @pre No letter-boxes touch!
 * @pre Horizontal space between boxes of same row is always less
 * than the height of the boxes of that label.
 * @pre Hight of a box is always shorter than 2*height of any box in
 * same label (i.e same font-size) (and higher than 0.5*any box
 * in label).
 * @pre Rotation change between adjacent letters in a label is <=15deg.
 * @pre Vertical space between boxes of neighboring rows is always
 * less than the heigh of any box in the label. (...!)
 */
public class LabelLayoutIterator {
    /**
     * Pixels alpha-value-threshold where over means box-point. */
    public/***/ static final int DEFAULT_ALPHA_THRESHOLD = 1;

    /**
     * Max search length from left/right edge of a box to
     * a neighbor-box (same label) is boxHeight*this. */
    public/***/ static final double BOX_SEARCH_LENGTH_FACTOR = 0.5;

    /**
     * Max search length from top/botten edge of a box to a
     * neighbor-row-box (same label) is boxHeight*this. */
    public/***/ static final double ROW_SEARCH_LENGTH_FACTOR = 0.3;

    /**
     * sb/hb >= this, where sb is shortest, tb tallest box, in
     * any label. */
    public/***/ static final double MAX_BOX_HEIGHT_DIFFERENCE_FACTOR = 0.7;

    /**
     * The maximum change in angle between two adjacent boxes. */
    public/***/ static final double MAX_ANGLE_CHANGE = 25;

    /**
     * w0 = (dL + w1 + dR) where w0 is width of longest line, w1
     * width of shortest, and dL, dR are distances next to shortest
     * "under" longest. abs(_L - dR) / w1 = this. */
    public/***/ static final double CENTERED_LAXNESS_FACTOR = 0.15;

    /**
     * d / boxHeight <= this, where d is ortogonal distance from a
     * box's baseline to neighbor box's closest bottom-point. */
    public/***/ static final double SAME_BASELINE_LAXNESS_FACTOR = 0.15;

    /**
     * Stricter when multiple row (all boxes on a straight baseline) */
    public/***/ static final double SAME_BASELINE_LAXNESS_FACTOR_MULTIROW = 0.1;


    /** Start searching for next box-point at this pos in map. */
    public/***/ int startX = 0;
    public/***/ int startY = 0;

    /** Representation of box-image: map[row][column]. */
    public/***/ boolean[][] map;
    public/***/ boolean[][] untouchedMap;

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
        this.untouchedMap = new boolean[img.getHeight()][img.getWidth()];

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = img.getColor(x, y);
                int alpha = c.getAlpha();
                if (alpha >= alphaThreshold) {
                    this.map[y][x] = true;
                    this.untouchedMap[y][x] = true;
                }
                else {
                    this.map[y][x] = false;
                    this.untouchedMap[y][x] = false;
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

        LabelLayout lay = expandToLabelLayout(p);
        if (lay == null) {
            expandAndRemove(p);
            return next();
        }

        removeLabel(lay);
        if (!isEdgeLabel(lay)) return lay;
        else return next();
    }

    /**
     * Scans through the map, looking for a box-point.
     *
     * @return A box-point in the map, or NULL if no more
     * box-points, as [x,y].
     */
    public/***/ int[] findBoxPoint(int startX, int startY) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (isBoxPoint(x, y))
                    return new int[]{x, y};
            }
        }
        return null;
    }

    /**
     * Expands box-point to label-layout.
     *
     * 1.Box-point expands to box.
     * 2.Looks left/right for more boxes -> row.
     * 3.Looks up/down for more rows -> layout.
     *
     * @param bp Box-point.
     * @return The layout of the label that contains bp. NULL if bp
     * won't expand to box.
     */
    public/***/ LabelLayout expandToLabelLayout(int[] bp) {
        if (!isBoxPoint(bp))
            throw new RuntimeException("Not box-point!");

        Box b = expandToBox(bp);
        if (b == null) return null;

        LinkedList<Box> row = expandToRow(b);
        LabelLayout lay = new LabelLayout(row);

        if (lay.hasObviousRotation()) return lay;

        boolean up = true;
        addRows(up, row, lay);
        up = false;
        addRows(up, row, lay);

        return lay;
    }

    /**
     * Expand a box-point to a box and the box to a horizontal
     * row of boxes.
     *
     * Looks left/right to expanded box (at certain distance..).
     * Stops looking when finding:
     *  - Nothing.
     *  - A box at wrong size/rotation.
     *  - The box-image-edge.
     *  - An edge-box (expanded to null).
     *
     * @return A row in the map (i.e horizontaly adjacent boxes).
     * As a minumum [startBox].
     * @pre startBox not null..
     */
    public/***/ LinkedList<Box> expandToRow(Box startBox) {
        if (startBox == null) throw new RuntimeException();

        LinkedList<Box> row = new LinkedList<Box>();
        row.add(startBox);

        boolean left = true;
        addBoxes(left, startBox, row);
        left = false;
        addBoxes(left, startBox, row);

        return row;
    }

    /**
     * Adds, to a LabelLayout, all rows above or below some
     * start-row in same label in the map. Done when nothing
     * neighbor-row-like there.
     *
     * @param up If true, adds rows above startRow, otherwise below.
     * @param startRow Start-row.
     * @param lay Accumulator for the new rows.
     * @return The provided label-layout, prepended/appended
     * with all rows above/below in same label as start-row.
     *
     * @pre startRow has no rotation and a straight base-line.
     */
    public/***/ LabelLayout addRows(boolean up, LinkedList<Box> startRow, LabelLayout lay) {
        LinkedList<Box> neigh = findNeighborRow(up, startRow);
        if (neigh == null) return lay;

        if (up) lay.addRowFirst(neigh);
        else lay.addRowLast(neigh);

        return addRows(up, neigh, lay);
    }

    /**
     * Removes a label from the map, i.e deactivates all its
     * points. Goes through the label-layout and deactivates every
     * point of every letter-box.
     *
     * @param lay LabelLayout for the label to be removed.
     */
    public/***/ void removeLabel(LabelLayout lay) {
        for (Box b : lay.getBoxes()) {
            int[] bp = getInsideBoxPoint(b);
            expandAndRemove(bp);
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
     * @param b A box extracted from this box-image.
     * @return A box-point inside this box.
     *
     * @pre b a box that contains box-points.
     */
    public/***/ int[] getInsideBoxPoint(Box b) {
        int[] start = Math2.toInt(b.getTopLeft());
        int[] end = Math2.toInt(b.getBottomRight());
        PixelWalk pw = new PixelWalk(start, end);

        int[] p = null;
        while ((p = pw.next()) != null) {
            if (isBoxPoint(p)) return p;
        }

        throw new RuntimeException();
    }

    /**
     * Adds, to a list, all boxes to the left or right of some
     * start-box, in same row of some label in the map. Done when
     * nothing neighbor-box-like there.
     *
     * @param left If true, adds boxes to the left of startBox,
     * otherwise to the right.
     * @param start Start-box.
     * @param bs Accumulator for the new boxes.
     * @return The provided box-array (bs) prepended/appended
     * with all boxes to left/right of that box (in same label).
     */
    public/***/ LinkedList<Box> addBoxes(boolean left, Box start, LinkedList<Box> bs) {
        Box neigh = findNeighborBox(left, start);
        if (neigh == null) return bs;

        if (left) bs.addFirst(neigh);
        else bs.addLast(neigh);

        return addBoxes(left, neigh, bs);
    }

    /**
     * Finds a neighbor-row of a start row.
     * Looks either up or down from the start row. A neighbor-row
     * is an adjacent row in same label as the start-row.
     *
     * Conditions for beeing a neighbor-row:
     *  -No rotation (like start-row).
     *  -In close proximity (up/down) to start-row.
     *  -Generally same line-height as start-row.
     *  -Centered in relation to start-row.
     *
     * @param up Search up, otherwise down.
     * @param sr Start-row.
     * @return A neighbor-row either up or down of startRow,
     * or NULL if such a row doesn't exist.
     *
     * @pre sr has no obvious rotation and a straight baseline.
     */
    public/***/ LinkedList<Box> findNeighborRow(boolean up, LinkedList<Box> sr) {
        if (new LabelLayout(sr).hasObviousRotation())
            throw new RuntimeException();

        int[] np = findPotentialNeighborRowPoint(up, sr);
        if (np == null) return null;

        Box nb = expandToBox(np);
        if (nb == null) return null;

        LinkedList<Box> nr = expandToRow(nb);
        LabelLayout sl = new LabelLayout(sr);
        LabelLayout nl = new LabelLayout(nr);

        if (nl.hasObviousRotation()) return null;
        if (sl.getShortestBoxHeight() / nl.getTallestBoxHeight() < MAX_BOX_HEIGHT_DIFFERENCE_FACTOR) return null;
        if (nl.getShortestBoxHeight() / sl.getTallestBoxHeight() < MAX_BOX_HEIGHT_DIFFERENCE_FACTOR) return null;
        if (!rowsCentered(sr, nr)) return null;
        if (!sameBaseline(sr)) return null;

        return nr;
    }

    /**
     * @param up If true searches up, otherwise down.
     * @param sr Start-row.
     * @return Point in potential neighbor-row, or NULL if none
     * found within probable distance.
     */
    public/***/ int[] findPotentialNeighborRowPoint(boolean up, LinkedList<Box> sr) {
        Box midB = sr.get(sr.size() / 2);

        double[] start = up ? midB.getTopMid() : midB.getBottomMid();
        double verticalD = new LabelLayout(sr).getAverageBoxHeight() * ROW_SEARCH_LENGTH_FACTOR;
        double[] verticalV = new double[]{0,1};
        int dir = up ? -1 : 1;
        start = Math2.step(start, verticalV, dir*verticalD);

        double horizontalD = new LabelLayout(sr).getAverageBoxWidth();
        double[] horizontalV = new double[]{1,0};
        double[] end0 = Math2.step(start, horizontalV, horizontalD);
        double[] end1 = Math2.step(start, horizontalV, -horizontalD);

        int[] bp0 = findBoxPointOnPath(start, end0);
        int[] bp1 = findBoxPointOnPath(start, end1);

        return bp0 != null ? bp0 : bp1;
    }

    /**
     * @return First box-point you come across when walking from start
     * till end, or NULL if none/edge.
     */
    public/***/ int[] findBoxPointOnPath(int[] start, int[] end) {
        PixelWalk pw = new PixelWalk(start, end);

        int[] p;
        while ((p=pw.next()) != null) {
            if (isBoxPoint(p)) return p;
        }

        return null;
    }
    public/***/ int[] findBoxPointOnPath(double[] start, double[] end) {
        return findBoxPointOnPath(Math2.toInt(start), Math2.toInt(end));
    }

    /**
     * @return True if rows are centered above/below eachother.
     * @pre Non-rotated rows.
     */
    public/***/ boolean rowsCentered(LinkedList<Box> r0, LinkedList<Box> r1) {
        double[] bs0 = new LabelLayout(r0).getBounds();
        double[] bs1 = new LabelLayout(r1).getBounds();
        double w0 = bs0[2] - bs0[0];
        double w1 = bs1[2] - bs1[0];
        double deltaL = bs0[0] - bs1[0];
        double deltaR = bs1[2] - bs0[2];

        return Math.abs(deltaL - deltaR) / Math.min(w0, w1) <= CENTERED_LAXNESS_FACTOR;
    }

    /**
     * Finds a neighbor-box to a start-box.
     * Looks to either left or right side of the start box.
     * A neighbor-box is an adjacent box in same label.
     *
     * Conditions for beeing a neighboring box:
     *  -In close proximity to left/right of start-box.
     *  -At about same height as start-box.
     *  -At about the same rotation as start-box.
     *  -At about same baseline as start-box.
     *
     * @param left Search left, otherwise right.
     * @param sb Start-box.
     * @return A left/right neighbor-box, or NULL if such a
     * neighbor-box doesn't exist.
     */
    public Box findNeighborBox(boolean left, Box sb) {
        int[] np = findPotentialNeighborBoxPoint(left, sb);
        if (np == null) return null;

        Box nb = expandToBox(np);
        if (nb == null) return null;

        if (nb.getHeight() / sb.getHeight() < MAX_BOX_HEIGHT_DIFFERENCE_FACTOR)
            return null;
        if (sb.getHeight() / nb.getHeight() < MAX_BOX_HEIGHT_DIFFERENCE_FACTOR)
            return null;
        if (Math2.angleDiff(sb.getRotation(), nb.getRotation()) > MAX_ANGLE_CHANGE)
            return null;
        if (!sameBaseline(sb, nb))
            return null;

        return nb;
    }

    /**
     * Finds a point in a neighboring box, or NULL if no neighbor
     * there. Looks either left or right.
     *
     * @param left Search left, otherwise right.
     * @param sb Start-box.
     * @return Neighbor-box-point, or NULL if none there.
     */
    public/***/ int[] findPotentialNeighborBoxPoint(boolean left, Box sb) {
        double INIT_JUMP_LENGTH_OUT_OF_STARTBOX = 3;
        double d = sb.getHeight() * BOX_SEARCH_LENGTH_FACTOR;

        double[] start0 = sb.getLeftMid();
        double[] start1 = sb.getTopLeft();
        double[] start2 = sb.getBottomLeft();
        int dir = -1;

        if (!left) {
            start0 = sb.getRightMid();
            start1 = sb.getTopRight();
            start2 = sb.getBottomRight();
            dir = 1;
        }

        double[] end0 = Math2.step(start0, sb.getDirVector(), dir*d);
        double[] end1 = Math2.step(start1, sb.getDirVector(), dir*d);
        double[] end2 = Math2.step(start2, sb.getDirVector(), dir*d);
        start0 = Math2.step(start0, sb.getDirVector(), dir*INIT_JUMP_LENGTH_OUT_OF_STARTBOX);
        start1 = Math2.step(start1, sb.getDirVector(), dir*INIT_JUMP_LENGTH_OUT_OF_STARTBOX);
        start2 = Math2.step(start2, sb.getDirVector(), dir*INIT_JUMP_LENGTH_OUT_OF_STARTBOX);

        int[] bp0 = findBoxPointOnPath(start0, end0);
        int[] bp1 = findBoxPointOnPath(start1, end1);
        int[] bp2 = findBoxPointOnPath(start2, end2);

        return bp0 != null ? bp0 : (bp1 != null ? bp1 : bp2);
    }

    /**
     * @param lax d / boxHeight <= this, where d is ortogonal
     * distance from a box's baseline to neighbor box's closest
     * bottom-point.
     * @return True if boxes is positioned on same (similar) base-line.
     */
    public/***/ boolean sameBaseline(Box b0, Box b1, double lax) {
        double[] p0 = b0.getBottomMid();
        double[] v = b0.getDirVector();

        double[] p1 =
            Math2.distance(p0, b1.getBottomLeft()) <
            Math2.distance(p0, b1.getBottomRight()) ?
            b1.getBottomLeft() : b1.getBottomRight();

        return Math.abs(Math2.distance(p1, p0, v)) / b0.getHeight() < lax;
    }

    /**
     * Between any boxes (baseline might be curved so high lax).
     */
    public/***/ boolean sameBaseline(Box b0, Box b1) {
        return sameBaseline(b0, b1, this.SAME_BASELINE_LAXNESS_FACTOR);
    }

    /**
     * Boxes in a row (straight base line so low lax).
     * @return True if all boxes in row are on same baseline.
     */
    public/***/ boolean sameBaseline(LinkedList<Box> row) {
        if (row.size() < 2) return true;

        for (int i = 0; i < row.size()-1; i++) {
            if (!sameBaseline(row.get(i), row.get(i+1))) return false;
        }

        return true;
    }

    /**
     * Finds all boxed-points connected to the start-point and
     * created a box from these points.
     *
     * @param start Start-point.
     * @return A box encapsulating start-point and all connceted
     * box-points, or NULL if start-point expands to too few points,
     * or if resulting box is outside or on the box-image edge.
     *
     * @pre start is a box-point in the map.
     */
    public/***/ Box expandToBox(int[] start) {
        LinkedList<int[]> ps = expandToBoxPoints(start);
        if (ps.size() < Box.MIN_NO_POINTS_FOR_BOX_FITTING
            || containsEdgePoint(ps)) {
            return null;
        }

        Box b = new Box(ps);
        if (isInside(b)) return b;
        else return null;
    }

    /**
     * @param start Start-point.
     * @return All box-points connected to start (including start).
     * Empty list if start not a box-point.
     */
    public/***/ LinkedList<int[]> expandToBoxPoints(int[] start) {
        if (!isBoxPoint(start)) return new LinkedList<int[]>();

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
        if (!isBoxPoint(p)) throw new RuntimeException("Not bp!");

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
     * @return True if any point in ps is an edge-point.
     */
    public/***/ boolean containsEdgePoint(LinkedList<int[]> ps) {
        for (int[] p : ps) {
            if (isEdgePoint(p)) return true;
        }
        return false;
    }

    /**
     * @return True if p is an edge-point, i.e a point at the
     * edge of the box-image.
     */
    public/***/ boolean isEdgePoint(int[] p) {
        return
            p[0] == 0 ||
            p[0] == map[0].length-1 ||
            p[1] == 0 ||
            p[1] == map.length-1;
    }

    /**
     * @return True if box is inside box-image.
     */
    public/***/ boolean isInside(Box b) {
        int[][] cs = Math2.toInt(b.getCorners());
        return
            isInside(cs[0]) && isInside(cs[1]) &&
            isInside(cs[2]) && isInside(cs[3]);
    }

    /**
     * @return True if point p is inside box-image.
     */
    public/***/ boolean isInside(int[] p) {
        return
            p[0] >= 0 && p[0] < map[0].length &&
            p[1] >= 0 && p[1] < map.length;
    }
    public/***/ boolean isInside(double[] p) {
        return isInside(Math2.toInt(p));
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

    /**
     * @return True if map is empty (all points false).
     */
    public/***/ boolean mapIsEmpty() {
        for (boolean[] row : map) {
            for (boolean p : row) {
                if (p == true) return false;
            }
        }
        return true;
    }

    /**
     * @return True if layout might continue outside of box-image.
     */
    public boolean isEdgeLabel(LabelLayout l) {
        boolean[][] temp = this.map;
        this.map = this.untouchedMap;
        boolean res = isEdgeLabel_(l);
        this.map = temp;
        return res;
    }
    public/***/ boolean isEdgeLabel_(LabelLayout l) {
        LinkedList<Box> row0 = l.getRow(0);
        if (isEdgeRow(row0)) return true;
        if (l.hasObviousRotation()) return false;

        LinkedList<Box> row1 = row0;
        boolean up = true;
        while (true) {
            int[] p = findPotentialNeighborRowPoint(up, row1);
            if (p == null) break;
            Box b = expandToBox(p);
            if (b == null) return true;
            LinkedList<Box> row2 = expandToRow(b);
            if (isEdgeRow(row2)) return true;

            row1 = row2;
        }

        row1 = row0;
        up = false;
        while (true) {
            int[] p = findPotentialNeighborRowPoint(up, row1);
            if (p == null) return false;
            Box b = expandToBox(p);
            if (b == null) return true;
            LinkedList<Box> row2 = expandToRow(b);
            if (isEdgeRow(row2)) return true;

            row1 = row2;
        }
    }

    /**
     * @return True if row might continue outside view, i.e
     * if it has clean cut-out boxes, or has a cut neighbor-box.
     *
     * @pre Row has been expanded properly so any complete
     * "potential-neighbor-boxes" are part of the row.
     *
     */
    private boolean isEdgeRow(LinkedList<Box> row) {
        if (isEdgeBox(row.getFirst())
            || isEdgeBox(row.getLast())
            || isEdgeBox(row.get(row.size() / 2)))
            return true;

        Box first = row.getFirst();
        Box last = row.getLast();

        int[] p = findPotentialNeighborBoxPoint(true, first);
        if (p != null && expandToBox(p) == null) return true;
        p = findPotentialNeighborBoxPoint(true, last);
        if (p != null && expandToBox(p) == null) return true;
        p = findPotentialNeighborBoxPoint(false, first);
        if (p != null && expandToBox(p) == null) return true;
        p = findPotentialNeighborBoxPoint(false, last);
        if (p != null && expandToBox(p) == null) return true;

        return false;
    }

    /**
     * @return True if box might have a neighbor outside of box-image
     * (box-neighbor or row-neighbor).
     */
    public/***/ boolean isEdgeBox(Box b) {
        double hd = b.getHeight() * BOX_SEARCH_LENGTH_FACTOR;// * 2;
        double vd = b.getHeight() * ROW_SEARCH_LENGTH_FACTOR;// + b.getHeight();

        double[] lr = b.getDirVector();
        double[] ud = b.getOrtoDirVector();

        return
            !isInside(Math2.step(b.getTopLeft(), lr, -hd)) ||
            !isInside(Math2.step(b.getTopLeft(), ud, -vd)) ||
            !isInside(Math2.step(b.getTopRight(), lr, hd)) ||
            !isInside(Math2.step(b.getTopRight(), ud, -vd)) ||
            !isInside(Math2.step(b.getBottomRight(), lr, hd)) ||
            !isInside(Math2.step(b.getBottomRight(), ud, vd)) ||
            !isInside(Math2.step(b.getBottomLeft(), lr, -hd)) ||
            !isInside(Math2.step(b.getBottomLeft(), ud, vd));
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
