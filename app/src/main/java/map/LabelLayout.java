package map;

/**
 * A layout specification of a label, describing where each letter
 * in the label is located in some a integer-coordinate system.
 * Represented as a 2d-array of Box-objects where each box represents
 * a letter in the label, like: letterBox[ row ][ pos in row ].
 *
 * @inv Number of letters > 0
 */
public class LabelLayout {
    public/***/ Box[][] letterBoxes;

    /**
     * Constructs a LabelLayout from a label-row.
     *
     * @param row Letter boxes for one row in the label.
     * @pre length(row) > 0
     */
    public LabelLayout(Box[] row) {
    }

    /**
     * Adds a first row.
     */
    public void addRowFirst(Box[] row) {
    }

    /**
     * Adds a last row.
     */
    public void addRowLast(Box[] row) {
    }

    /**
     * @return All letter-boxes in a 1d-array. Letter box copies.
     */
    public Box[] getBoxes() {
        return null;
    }

    /**
     * @return Letter-box at specified row and position in row. A copy.
     */
    public Box getBox(int row, int pos) {
        return null;
    }

    /**
    * @return The height of the tallest box.
    */
    public double getTallestBoxHeight() {
        return 0;
    }

    /**
     * @return [xmin, ymin, xmax, ymax]
     */
    public double[] getBounds() {
        return null;
    }

    /**
     * @return True if any letter-box has a significant rotation,
     * i.e if the label is rotated as a whole, or has a curver baseline.
     */
    public boolean hasRotation() {
        return false;
    }


    /**
     * Iterator that returns letter-boxes of the label-layout.
     * Usage: call hasMore() before getNext().
     */
    public class LetterBoxIterator {
        public/***/ int row = 0;
        public/***/ int pos = 0;
        public/***/ Box next = null;

        /**
         * @return Next letter-box in iterator.
         */
        public Box next() {
            return next;
        }

        /**
         * Indicates if more letter-boxes in iterator, and updates
         * next if there is. Always call this before next().
         *
         * @return True if iter has more.
         */
        public boolean hasMore() {
            return false;
        }
    }
}
