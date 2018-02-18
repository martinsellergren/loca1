package map;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Color;

/**
 * An image made up of tiles. Tiles are saved on hd and loaded
 * into memory when necessary. Only one tile is in memory at the time.
 * A tile is loaded when it is modified and stays loaded until another
 * image is loaded.
 *
 * The dims of the individual tiles are unspecified, but always follows:
 *  -All are perfect squares with same width/height, except
 *  -Last column tiles may be thinner
 *  -Last row tiles may be shorter.
 *
 * Tiles are saved in png-format in given directory, following
 * naming convention: tile-r-c.png.
 */
public class TiledImage {
    /** Directory where tiles reside. */
    private final Path dir;

    /** Tile in memory. Only one at the time - last modified tile. */
    private BasicImage memTile;

    /** Image data, so don't have to load and investigate. */
    private final int width, height, tileWidth, tileHeight, rows, cols;

    /**
     * Constructs the tiledImage from saved tiles.
     *
     * @param dir Directory where tiles reside, correctly named.
     */
    private TiledImage(Path dir, int w, int h, int tw, int th, int rs, int cs) {
        this.dir = dir;
        this.width = w;
        this.height = h;
        this.tileWidth = tw;
        this.tileHeight = th;
        this.rows = rs;
        this.cols = cs;
        this.memTile = loadTile(0, 0, dir);
    }

    /**
     * @return Tile at specified row/column.
     */
    private static BasicImage loadTile(int r, int c, Path dir) {
        return BasicImage.load(getTilePath(r, c, dir));
    }
    private BasicImage loadTile(int r, int c) {
        return loadTile(r, c, this.dir);
    }

    /**
     * @return Path to tile: x/y/tile-r-c.png
     */
    private static Path getTilePath(int r, int c, Path dir) {
        String fn = String.format("tile-%s-%s", r, c);
        return dir.resolve(fn);
    }
    private Path getTilePath(int r, int c) {
        return getTilePath(r, c, this.dir);
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
     * Assemble and save img.
     */
    public void save(Path p) {
        getOneImage().save(p);
    }

    /**
     * Loads a tiled image from a directory.
     *
     * @parm dir Directory where tiles resides.
     * @throws RuntimeException if bad tiles in directory.
     */
    public static TiledImage load(Path dir) {
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

        private int tileW = -1;
        private int tileH = -1;
        private int lastColW = -1;
        private int lastRowH = -1;

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
         * Adds a tile to the builder by saving it to file with
         * correct name. Adds 'left-to-right, row-by-row'.
         *
         * @pre All tiles perfect squares with same dims, except
         * last column (may be thinner), last row (may be shorter).
         * @throws RuntimeException if bad dims.
         */
        public void add(BasicImage tile) {
            testDims(tile);

            Path p = getTilePath(r, c, dir);
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

        /**
         * @throws RuntimeException if inconsistent dims.
         */
        private void testDims(BasicImage tile) {
            // set dims
            if (r == 0 && c == 0) {
                tileW = tile.getWidth();
                tileH = tile.getHeight();
            }
            if (c == cols-1 && r == 0) lastColW = tile.getWidth();
            if (r == rows-1 && c == 0) lastRowH = tile.getHeight();

            // test dims
            if (c == cols-1) assertEq(tile.getWidth(), lastColW);
            else assertEq(tile.getWidth(), tileW);

            if (r == rows-1) assertEq(tile.getHeight(), lastRowH);
            else assertEq(tile.getHeight(), tileH);
        }
        private void assertEq(int x, int y) {
            if (x != y) throw new RuntimeException("Bad tile dims");
        }

        /**
         * @return The TiledImage-object.
         * @throws RuntimeException if excpected more/less tiles.
         */
        public TiledImage build() {
            if (c != 0 || r != rows)
                throw new RuntimeException("Bad tile numbering");

            int w = tileW * (cols - 1) + lastColW;
            int h = tileH * (rows - 1) + lastRowH;

            return new TiledImage(dir, w, h, tileW, tileH, rows, cols);
        }
    }
}
