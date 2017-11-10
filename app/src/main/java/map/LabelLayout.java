package map;

/**
 * A layout specification of a label, describing where each letter
 * in the label is located (in some a map).
 * Represented as an 2d-array of Box-objects where each box represents
 * a letter in the label, like:
 * letterBox[ row ][ pos in row ].
 *
 * @inv Number of letters > 0
 */
public class LabelLayout {
    private Box[][] letterBox;

    /**
     * Constructs a LabelLayout from a label-row.
     *
     * @param row Letter boxes for one row in the label.
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
}
