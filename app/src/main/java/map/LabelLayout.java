package map;

import java.util.LinkedList;

/**
 * A layout specification of a label, describing where each letter
 * in the label is located in some a float-precise-coordinate system.
 * Represented as a 2d-array of Box-objects where each box represents
 * a letter in the label, like: letterBoxes[ row ][ pos in row ].
 *
 * @inv Number of letters > 0
 */
public class LabelLayout {
    public/***/ LinkedList<LinkedList<Box>> letterBoxes = new LinkedList<LinkedList<Box>>();

    /**
     * Constructs a LabelLayout from a label-row.
     *
     * @param row Letter boxes for one row in the label.
     * @pre length(row) > 0
     */
    public LabelLayout(LinkedList<Box> row) {
        if (row.size() == 0) throw new IllegalArgumentException();
        letterBoxes.add(row);
    }

    /**
     * Adds a first row.
     */
    public void addRowFirst(LinkedList<Box> row) {
        letterBoxes.addFirst(row);
    }

    /**
     * Adds a last row.
     */
    public void addRowLast(LinkedList<Box> row) {
        letterBoxes.addLast(row);
    }

    /**
     * @return All letter-boxes in a 1d-array. Letter box copies.
     * Ordered correctly, letter by letter.
     */
    public Box[] getBoxes() {
        Box[] bs = new Box[getNoBoxes()];
        int i = 0;
        for (LinkedList<Box> row : letterBoxes) {
            for (Box b : row) {
                bs[i++] = b.copy();
            }
        }
        return bs;
    }

    public int getNoBoxes() {
        int count = 0;
        for (LinkedList<Box> row : letterBoxes) {
            count += row.size();
        }
        return count;
    }

    /**
     * @return Number of rows.
     */
    public int getNoRows() {
        return letterBoxes.size();
    }

    /**
     * @return Letter-box at specified row and column. A copy.
     */
    public Box getBox(int r, int c) {
        if (r < 0) r = getNoRows() + r;
        LinkedList<Box> row = letterBoxes.get(r);
        if (c < 0) c = row.size() + c;
        return row.get(c);
    }

    /**
    * @return The height of the tallest box.
    */
    public double getTallestBoxHeight() {
        double max = -1;
        for (Box b : getBoxes()) {
            if (b.getHeight() > max) max = b.getHeight();
        }
        return max;
    }

    /**
    * @return The height of the shortest box.
    */
    public double getShortestBoxHeight() {
        double min = Double.MAX_VALUE;
        for (Box b : getBoxes()) {
            if (b.getHeight() < min) min = b.getHeight();
        }
        return min;
    }

    /**
     * @return Avg box height.
     */
    public double getAverageBoxHeight() {
        double sum = 0;
        for (Box b : getBoxes()) {
            sum += b.getHeight();
        }
        return sum / getNoBoxes();
    }

    /**
     * @return Avg box height.
     */
    public double getAverageBoxWidth() {
        double sum = 0;
        for (Box b : getBoxes()) {
            sum += b.getWidth();
        }
        return sum / getNoBoxes();
    }

    /**
     * @return [xmin, ymin, xmax, ymax]
     */
    public double[] getBounds() {
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymax = Double.MIN_VALUE;

        for (Box b : getBoxes()) {
            double[] bb = b.getBounds();
            if (bb[0] < xmin) xmin = bb[0];
            if (bb[1] < ymin) ymin = bb[1];
            if (bb[2] > xmax) xmax = bb[2];
            if (bb[3] > ymax) ymax = bb[3];
        }
        return new double[]{xmin, ymin, xmax, ymax};
    }

    /**
     * @return True if any letter-box has a significant rotation,
     * i.e if the label is rotated as a whole, or has a curver baseline.
     * Rather claims 'no rotation' than rotation.
     */
    public boolean hasObviousRotation() {
        int LIMIT = 15;
        for (Box b : getBoxes()) {
            if (Math.abs(b.getRotation()) >= LIMIT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add offset to every position in layout.
     */
    public void addOffset(double addX, double addY) {
        for (LinkedList<Box> row : letterBoxes) {
            for (Box b : row) {
                b.addOffset(addX, addY);
            }
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (LinkedList<Box> row : letterBoxes) {
            for (Box b : row) {
                s += b.toString() + "\n";
            }
            s += "\n";
        }
        return s;
    }


    // /**
    //  * Iterator that returns letter-boxes of the label-layout.
    //  * Usage: call hasMore() before getNext().
    //  */
    // public class LetterBoxIterator {
    //     public/***/ int row = 0;
    //     public/***/ int pos = 0;
    //     public/***/ Box next = null;

    //     /**
    //      * @return Next letter-box in iterator.
    //      */
    //     public Box next() {
    //         return next;
    //     }

    //     /**
    //      * Indicates if more letter-boxes in iterator, and updates
    //      * next if there is. Always call this before next().
    //      *
    //      * @return True if iter has more.
    //      */
    //     public boolean hasMore() {
    //         return false;
    //     }
    // }
}
