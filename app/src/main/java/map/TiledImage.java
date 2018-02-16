package map;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Color;

public class TiledImage {
    private Path dir;

    /**
     * Constructs the tiledImage from saved tiles.
     *
     * @param dir Directory where tiles reside, correctly named.
     */
    public TiledImage(Path dir) {
        this.dir = dir;
    }

    /**
     * @return Width of image.
     */
    public int getWidth() {
        return 0;
    }

    /**
     * @return Height of image.
     */
    public int getHeight() {
        return 0;
    }

    /**
     * @return Color of specified point.
     */
    public Color getColor(int[] p) {
        return null;
    }

    /**
     * Sets color at specified pixel. Overwrites current pixel.
     */
    public void setColor(int[] p, Color c) {
    }

    /**
     * Set color at specified pixel. Blends with current pixel value
     * by laws defined at AlphaComposite-SRC_OVER.
     */
    public void drawPixel(int[] p, Color c) {
    }

    /**
     * Calls drawPixel on all pixels inside defined area.
     * @param bs [xmin, ymin, xmax, ymax]
     */
    public void drawRect(int[] bs, Color c) {
    }

    /**
     * @return true if point p is inside image.
     */
    public boolean isInside(int[] p) {
        return false;
    }

    /**
     * @return All tiles concatenated into one basicImage. Be careful
     * with heap overflows.
     */
    public BasicImage getOneImage() {
        return null;
    }

    /**
     * @param bs [xmin, ymin, xmax, ymax]
     * @return A subimage defined by bounds.
     */
    public BasicImage getSubImage(int[] bs) {
        return null;
    }


    /**
     * Builds the TiledImage by saving tiles to files.
     */
    public static class Builder {
        private int c = 0;
        private int r = 0;

        private Path dir;
        private int rows;
        private int cols;

        /**
         * @param rs No rows in tile layout.
         * @param cs No columns in tile layout.
         * @param dir Direction where tiles will be saved.
         */
        public Builder(int rs, int cs, Path dir) {
            this.rows = rs;
            this.cols = cs;
            this.dir = dir;
        }

        /**
         * Adds a tile to the builder by saving it for file with
         * correct name. Adds "row-by-row".
         */
        public void add(BasicImage tile) {
            String fileName = String.format("tile-%s-%s", r, c);
            Path p = this.dir.resolve(fileName);
            tile.save(p);

            c++;
            if (c > cols) {
                c = 0;
                r++;
            }
            if (r > rows) {
                throw new RuntimeException("Too many tiles");
            }
        }

        public TiledImage build() {
            if (c != 0 || r != rows)
                throw new RuntimeException("Bad tile numbering");

            return new TiledImage(this.dir);
        }
    }
}
