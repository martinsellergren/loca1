package map;

import java.awt.Point;

/**
 * A layout specification of a label, describing where each letter
 * in the label is located in some a integer-coordinate system.
 * Represented as an 2d-array of Box-objects where each box represents
 * a letter in the label, like:
 * letterBox[ row ][ pos in row ].
 *
 * @inv Number of letters > 0
 */
public class LabelLayout {
    private Box[][] letterBoxes;

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
     * @return All letter-boxes in a 1d-array.
     */
    public Box[] getBoxes() {
        return null;
    }

    /**
     * @return Letter-box at specified row and position in row.
     */
    public Box getBox(int row, int pos) {
        return null;
    }

    /**
     * Predict the line-height of the label.
     *
     * @return A line-height prediction.
     */
    public double predictLineHeight() {
        return getAverageHeight();
    }

    /**
     * @return Average rotation of the boxes.
     */
    public double getAverageRotation() {
        return 0;
    }

    public Point getHighest() {
        return null;
    }
    public Point getLowest() {
        return null;
    }
    public Point getLeftest() {
        return null;
    }
    public Point getRightest() {
        return null;
    }

    /**
     * @return A prediction of the length of one space-character.
     */
    private double predictSpaceWidth() {
        return 0;
    }

    /**
    * @return The average height of the boxes in the layout.
    */
    private double getAverageHeight() {
        return 0;
    }


    /**
     * Iterator that returns letter-boxes of the label-layout.
     * Also indicates how letters group into words.
     * Usage: call hasMore() before getNext().
     */
    public class LetterBoxIter {
        private int row = 0;
        private int pos = 0;
        private Box next = null;

        /**
         * @return Next letter-box in iterator. NULL if next is a
         * delimiter (space/newline).
         */
        public Box next() {
            return next;
        }

        /**
         * Indicates if more letter-boxes in iterator, and updates
         * next if there is. next=null if next pos is delimiter.
         * Always call this before next().
         *
         * @return True if iter has more.
         */
        public boolean hasMore() {
            return false;
        }
    }
}
