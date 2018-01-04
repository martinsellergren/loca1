package map;

/**
 * A pixel walk, i.e a walk from one position to another on
 * a straight line, returning the positions that are stepped on.
 * Positions in an int-grid.
 */
public class PixelWalk {
    public/***/ int nextX, nextY;
    public/***/ int endX, endY;
    private boolean done = false;

    /**
     * Diagonal decider. If angle is between 45+-DD go diagonal
     * up-left. Same principal for the other four diagonals.
     */
    private final double DD = 10;

    /**
     * Constructs the pixel-walk.
     */
    public PixelWalk(int startX, int startY, int endX, int endY) {
        this.nextX = startX;
        this.nextY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public PixelWalk(int[] start, int[] end) {
        this(start[0], start[1], end[0], end[1]);
    }

    /**
     * Returns next pos if not there yet, otherwise null.
     * Also updates state.
     */
    public int[] next() {
        if (this.done) return null;
        else {
            int currentX = this.nextX;
            int currentY = this.nextY;
            setDone();
            updateState();
            return new int[]{currentX, currentY};
        }
    }

    private void setDone() {
        this.done = nextX == endX && nextY == endY;
    }

    private void updateState() {
        double ang = Math2.angle(new int[]{endX-nextX, endY-nextY});

        if (ang >= -45 + DD && ang <= 45 - DD) {//right
            this.nextX += 1;
        } else if (ang >= 45 - DD && ang <= 45 + DD) {//up-right
            this.nextX += 1;
            this.nextY -= 1;
        } else if (ang >= 45 + DD && ang <= 135 - DD) {//up
            this.nextY -= 1;
        } else if (ang >= 135 - DD && ang <= 135 + DD) {// up-left
            this.nextX -= 1;
            this.nextY -= 1;
        } else if (ang >= 135 + DD || ang <= -135 - DD) {//left
            this.nextX -= 1;
        } else if (ang >= -135 - DD && ang <= -135 + DD) {//down-left
            this.nextX -= 1;
            this.nextY += 1;
        } else if (ang >= -135 + DD && ang <= -45 - DD) {//down
            this.nextY += 1;
        } else if (ang >= -45 - DD && ang <= -45 + DD) {//down-right
            this.nextX += 1;
            this.nextY += 1;
        } else {
            throw new RuntimeException("Dead-end, ang=" + ang);
        }
    }
}
