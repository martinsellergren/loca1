package map;

import java.awt.Point;

/**
 * A box-image is an image where only labels are showing and instead
 * of letters in the labels, there are boxes. Each box is formed like
 * the letter C (with straight lines) with the opening towards right
 * -to provide direction. The space-character is also represented as
 * a box. The box is sized and positioned so that corresponding letter
 * precisely fits inside. If the character is very short/thin the
 * box is given a min-width/height (which doesn't effect the
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
 * This is useful for creating labelLayouts for labels in a map-image.
 *
 * @pre No (or few..) letter-boxes touch.
 * @pre Horizontal space between boxes of same row is always less
 * than the width of any box in the label.
 * @pre Vertical space between boxes of neighboring rows is always
 * less than the heigh of any box in the label (...).
 * @pre Highest box is always shorter than 2*shortest box in same
 * label (i.e same font-size).
 */
public class BoxImage {
    private boolean[][] map;

    /**
     * Constructs the box-image representation from an rgba-image.
     * Close to transparent pixels are marked as non-box-point.
     * Lowest alpha value still counted as box-point is the
     * alpha-threshold.
     *
     * @param img An rgb-image (box-image).
     * @param alphaThreshold Pixels alpha-value-threshold where
     * over means box-point, under means non-box-point.
     */
    public BoxImage(MapImage boxImg, double alphaThreshold) {
        // for (int y = 0; y < img.getHeight(); y++) {
        //     for (int x = 0; x < img.getWidth(); x++) {
        //         Color c = new Color(img.getRGB(x, y), true);
        //         int alpha = c.getAlpha();
        //         if (alpha > labelAlphaThreshold) {
        //             img.setRGB(x, y, Color.black.getRGB());
        //         }
        //         else {
        //             img.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
        //         }
        //     }
        // }

    }

    /**
     * Constructor for using default value for alphaThreshold.
     */
    public BoxImage(MapImage boxImg) {
        this(boxImg, 100);
    }

    /**
     * Iterator that finds labels (clusters of boxes) in the box-image
     * and returns each label's layout. Scans through a box-image,
     * finds a box-point, expands it into a label-layout.
     * Returns good and bad (e.g clipped, half outside of view) labels.
     */
    public class LabelLayoutIterator {
        private boolean[][] copy;
        private LabelLayout next = null;

        /**
         * Constructs the iterator by making a copy of the boxImage-map
         * which can be modified without concerns.
         */
        public LabelLayoutIterator() {
            copy = new boolean[map.length][];

            for (int i = 0; i < map.length; i++) {
                copy[i] = new boolean[map[i].length];

                for (int j = 0; j < map[i].length; j++) {
                    copy[i][j] = map[i][j];
                }
            }
        }

        /**
         * @return Iterator's next label layout.
         */
        public LabelLayout next() {
            return next;
        }

        /**
         * True if iterator has more nexts. Also steps one step, i.e
         * sets next. Always call this before calling next().
         *
         * @return True if not done.
         */
        public boolean hasMore() {
            next = null;
            return false;
        }

        /**
         * Scans through the map-copy, looking for a box-point.
         *
         * @return A box-point in the map-copy, or NULL if no more
         * box-points.
         */
        private Point findBoxPoint() {
            return null;
        }

        /**
         * Finds a single point of a neighboring row of a start row.
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
         * @return A box-point of an up/down neighbor-row, or NULL
         * if such a neighbor-row doesn't exist.
         *
         * @pre startRow a row in the map-copy (i.e horizontaly
         * adjacent boxes) with no rotation and a straight baseline.
         */
        private Point findNeighborRowPoint(boolean up, Box[] startRow) {
            return null;
        }

        /**
         * Finds a single point of a neighboring box of a start-box.
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
         * @param startBox Start box.
         * @return A box-point of a left/right neighbor-box, or NULL
         * if such a neighbor-box doesn't exist.
         *
         * @pre startBox a box in the map-copy.
         */
        private Point findNeghborBoxPoint(boolean left, Box startBox) {
            return null;
        }

        /**
         * Expand one box-point to a box and the box to a horizontal
         * row of boxes.
         *
         * @param start Start-point.
         * @return A row in the map-copy (i.e horizontaly
         * adjacent boxes).
         *
         * @pre start is a box-point in the map-copy.
         */
        private Box[] expandToRow(Point start) {
            return null;
        }

        /**
         * Finds all boxed-points connected to the start-point and
         * created a box from these points.
         *
         * @param start Start-point.
         * @return The box containing start-point.
         *
         * @pre start is a box-point in the map-copy.
         */
        private Box expandToBox(Point start) {
            return null;
        }

        /**
         * @param start Start-point.
         * @return All points connected to the start-point.
         *
         * @pre start is a box-point in the map-copy.
         */
        private Point[] expandToBoxPoints(Point start) {
            return null;
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
        private Point[] getCorners(Point[] points) {
            return null;
        }

        /**
         * @return [0]Leftmost, [1]upmost, [2]rightmost and [3]downmost
         * points in given array, or NULL if unclear which any of those
         * are.
         */
        private Point[] findExtremes(Point[] points) {
            return null;
        }

        /**
         * @param deg Rotation if degrees, counterclockwise.
         * @return Same points but rotated around (0,0).
         */
        private Point[] rotatePoints(Point[] points, double deg) {
            return null;
        }

        /**
         * Adds, to a label-layout, all rows above or below some
         * start-row in same label in the map-copy.
         *
         * @param up If true, adds rows above startRow, otherwise below.
         * @param startRow Start-row.
         * @param layout Accumulator for the new rows.
         * @return The provided label-layout, prepended/appended
         * with all rows above/below in same label as start-row.
         *
         * @pre startRow a row of some label in the map-copy, that has
         * no rotation and a straight base-line.
         */
        private LabelLayout addRows(boolean up, Box[] startRow, LabelLayout layout) {
            return null;
        }

        /**
         * Adds, to an array, all boxes to the left or right of some
         * start-box, in same row of some label in the map-copy.
         *
         * @param left If true, adds boxes to the left of startBox,
         * otherwise to the right.
         * @param startBox Start-box.
         * @param bs Accumulator for the new boxes.
         * @return The provided box-array (bs) prepended/appended
         * with all boxes to left/right of that box (in same label).
         *
         * @pre startBox a letter-box in the map-copy.
         */
        private Box[] addBoxes(boolean left, Box startBox, Box[] bs) {
            return null;
        }

        /**
         * Removes a label from the map-copy, i.e deactivates all its
         * points. Goes through the label-layout and deactivates every
         * point of every letter-box in the layout. Extends the size of
         * the letter-box-area slightly to ensure that no point is
         * missed.
         *
         * @param layout Layout for the label to be removed.
         */
        private void removeLabel(LabelLayout layout) {
        }
    }
}
