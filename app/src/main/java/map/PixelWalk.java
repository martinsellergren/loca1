package map;

/**
 * A pixel walk, i.e a walk from one position to another on
 * a straight line, returning the positions that are stepped on.
 * Positions in an int-grid.
 */
public class PixelWalk {
    public/***/ int nextX, nextY;
    public/***/ int endX, endY;

    /**
     * Diagonal decider. If angle is between 45+-DD go diagonal
     * up-left. Same principal for the other four diagonals.
     */
    private final double DD = 15;

    /**
     * Constructs the pixel-walk.
     */
    public PixelWalk(int startX, int startY, int endX, int endY) {
        this.nextX = startX;
        this.nextY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    /**
     * Returns true if not there yet. Also sets next if there's more.
     */
    public boolean hasMore() {
        boolean hasMore = nextX != endX || nextY != endY;

        if (hasMore) {
            double ang = Math2.angle(new int[]{endX-nextX, endY-nextY});

            if (ang >= -45 + DD && ang <= 45 - DD) {//right
                nextX += 1;
            } else if (ang >= 45 - DD && ang <= 45 + DD) {//up-right
                nextX += 1;
                nextY -= 1;
            } else if (ang >= 45 + DD && ang <= 135 - DD) {//up
                nextY -= 1;
            } else if (ang >= 135 - DD && ang <= 135 + DD) {// up-left
                nextX -= 1;
                nextY -= 1;
            } else if (ang >= 135 + DD && ang <= -135 - DD) {//left
                nextX -= 1;
            } else if (ang >= -135 - DD && ang <= -135 + DD) {//down-left
                nextX -= 1;
                nextY += 1;
            } else if (ang >= -135 + DD && ang <= -45 - DD) {//down
                nextY += 1;
            } else if (ang >= -45 - DD && ang <= -45 + DD) {//down-right
                nextX += 1;
                nextY += 1;
            } else {
                throw new RuntimeException("Dead-end");
            }
        }

        return hasMore;
    }

    /**
     * @return next position.
     */
    public int[] next() {
        return new int[]{nextX, nextY};
    }
}
