package map;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.awt.Color;
import java.util.Arrays;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.io.IOException;

/**
 * An image made up of tiles. Tiles are saved on hdd and loaded
 * into memory when necessary. Only one tile is cached in memory
 * at a time.
 *
 * The dims of the tiles are unspecified, but always follows:
 *  -All are perfect squares with same width/height, except
 *  -Last column tiles may be thinner,
 *  -Last row tiles may be shorter.
 *
 * Tiles are saved in png-format in given directory, following
 * the naming convention: tile-r-c.png.
 */
public class TiledImage_writable_untested {

    /**
     * Directory where tiles reside. */
    private final Path dir;

    /**
     * Tile in memory (cached), at specified row and column.
     * Only one cached tile at the time: last loaded tile.
     * Marked modified whenever if is or might has been modified. */
    private BasicImage memTile;
    private int memTileRow;
    private int memTileCol;
    private boolean memTileModified;

    /**
     * Image data, so don't have to load and investigate.
     * Note: last-column-width = width % tileWidth
     *       last-row-height = height % tileHeight. */
    private final int width, height, tileWidth, tileHeight, rows, cols;

    /**
     * Constructs the tiledImage from saved tiles.
     *
     * @param dir Directory where tiles reside, correctly named.
     * @param w Width of image.
     * @param h Height of image.
     * @param tw Tile width of all tiles except those in last column.
     * @param th Tile height of all tiles except those in last row.
     * @param rs Number of rows in tile-layout.
     * @param cs Number of columns in tile-layout.
     */
    private TiledImage_writable_untested(Path dir, int w, int h, int tw, int th, int rs, int cs) {
        this.dir = dir;
        this.width = w;
        this.height = h;
        this.tileWidth = tw;
        this.tileHeight = th;
        this.rows = rs;
        this.cols = cs;

        this.memTile = loadTile(0, 0, dir);
        this.memTileRow = 0;
        this.memTileCol = 0;
        this.memTileModified = false;
    }

    /**
     * @return Width of image.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return Height of image.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @return Color of specified point.
     */
    public Color getColor(int[] p) {
        int[] rc_xy = getTileAndPos(p);
        BasicImage tile = getTile(rc_xy[0], rc_xy[1]);
        return tile.getColor(rc_xy[2], rc_xy[3]);
    }

    /**
     * Sets color at specified pixel. Overwrites current pixel.
     */
    public void setColor(int[] p, Color c) {
        int[] rc_xy = getTileAndPos(p);
        BasicImage tile = getTile(rc_xy[0], rc_xy[1]);
        tile.setColor(rc_xy[2], rc_xy[3], c);
        this.memTileModified = true;
    }

    /**
     * Set color at specified pixel. Blends with current pixel value
     * by laws defined at AlphaComposite-SRC_OVER.
     */
    public void paintPixel(int[] p, Color c) {
    }

    /**
     * Calls setColor on all pixels inside defined area.
     * @param bs [xmin, ymin, xmax, ymax]
     */
    public void paintRect(int[] bs, Color c) {
        for (int y = bs[1]; y <= bs[3]; y++) {
            for (int x = bs[0]; x <= bs[2]; x++) {
                setColor(new int[]{x, y}, c);
            }
        }
    }

    /**
     * @return True if point p is inside image.
     */
    public boolean isInside(int[] p) {
        return
            p[0] >= 0 &&
            p[1] >= 0 &&
            p[0] < getWidth() &&
            p[1] < getHeight();
    }

    /**
     * Crops out a subimage. Tiles are concatenated as needed.
     * Beware of StackOverflows.
     *
     * @param bs [xmin, ymin, xmax, ymax]
     * @return A subimage defined by bounds.
     */
    public BasicImage getSubImage(int[] bs) {
        if (!isInside(new int[]{bs[0], bs[1]}) ||
            !isInside(new int[]{bs[2], bs[3]}))
            throw new RuntimeException();

        int width = bs[2] - bs[0] + 1;
        int height = bs[3] - bs[1] + 1;
        BasicImage img = new BasicImage(width, height);
        Graphics2D g = img.createGraphics();

        for (int y0 = bs[1]; y0 < height; ) {
            int rowHeight = -1;

            for (int x0 = bs[0]; x0 < width; ) {
                int[] rc_xy = getTileAndPos(new int[]{x0, y0});
                BasicImage tile = loadTile(rc_xy[0], rc_xy[1]);
                rowHeight = tile.getHeight();
                int x1 = Math.min(x0 + tile.getWidth() - 1, bs[2]);
                int y1 = Math.min(y0 + rowHeight - 1, bs[3]);

                BasicImage part = tile.getSubImage(x0, y0, x1, y1);
                g.drawImage(part.getBufferedImage(), null, x0, y0);

                x0 = x1 + 1;
            }

            y0 += rowHeight;
        }

        return img;
    }

    /**
     * Returns specified tile. If tile not in memory, loads from
     * hdd. NOTE: Returned tile is cached.
     *
     * @param r Row.
     * @param c Column.
     * @return Tile at [r,c].
     */
    private BasicImage getTile(int r, int c) {
        if (r == this.memTileRow && c == this.memTileCol)
            return this.memTile;

        BasicImage tile = loadTile(r, c);
        cache(r, c, tile);
        return tile;
    }

    /**
     * Caches specified tile.
     */
    private void cache(int r, int c, BasicImage tile) {
        updateMemTileOnHdd();
        this.memTileRow = r;
        this.memTileCol = c;
        this.memTile = tile;
    }

    /**
     * Makes sure hdd-version of memTile is equal to cached version.
     * If memTileModified is false, does nothing, else saves
     * memTile to hdd, overwriting existing file.
     */
    private void updateMemTileOnHdd() {
        if (this.memTileModified) {
            Path p = getTilePath(this.memTileRow, this.memTileCol);
            this.memTile.save(p);
            this.memTileModified = false;
        }
    }

    /**
     * @return Tile at specified row/column loaded from hdd.
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
     * @return [r, c, x, y] for row/col of tile containing point p,
     * and x,y local point in this tile.
     */
    private int[] getTileAndPos(int[] p) {
        if (!isInside(p)) throw new RuntimeException("Out of bounds");
        int r = p[1] / this.tileHeight;
        int c = p[0] / this.tileWidth;
        int x = p[0] % this.tileWidth;
        int y = p[1] % this.tileHeight;
        return new int[]{r, c, x, y};
    }

    /**
     * Loads a tiled image from a directory. Investigates tile-layout
     * through file-names and loads top-left and bottom-right tiles
     * for finding dims etc.
     *
     * @parm dir Directory where tiles resides.
     * @param Loaded TileImage with top-left tile as memTile.
     * @throws RuntimeException if bad tiles in directory.
     */
    public static TiledImage_writable_untested load(Path dir) throws IOException {
        if (!dir.toFile().isDirectory())
            throw new IOException("Bad dir");

        File[] fs = dir.toFile().listFiles();
        if (fs.length == 0)
            throw new IOException("Empty dir");

        Arrays.sort(fs);

        String fnTL = fs[0].getName();
        String fnBR = fs[fs.length - 1].getName();
        int[] rcTL = getRowCol(fnTL);
        int[] rcBR = getRowCol(fnBR);

        BasicImage tl = loadTile(rcTL[0], rcTL[1], dir);
        BasicImage br = loadTile(rcBR[0], rcBR[1], dir);

        int tileW = tl.getWidth();
        int tileH = tl.getHeight();
        int rows = rcBR[0] + 1;
        int cols = rcBR[1] + 1;
        int width = tileW * (cols - 1) + br.getWidth();
        int height = tileH * (rows - 1) + br.getHeight();

        return new TiledImage_writable_untested(dir, width, height, tileW, tileH, rows, cols);
    }

    /**
     * @param fn FileName: tile-r-c.png
     * @return [r, c]
     */
    private static int[] getRowCol(String fn) throws IOException {
        try {
            String[] ps = fn.split("-");
            int r = Integer.parseInt(ps[1]);
            int c = Integer.parseInt(ps[2]);
            return new int[]{r, c};
        }
        catch (Exception e) {
            throw new IOException("Bad file name");
        }
    }


    //---------------------------------------------------for testing

    /**
     * Assemble and save img. Be careful with heap overflows.
     */
    public void save(Path p) {
        getOneImage().save(p);
    }
    public void save(String fn) {
        save(Paths.get(fn));
    }

    /**
     * @return All tiles concatenated into one basicImage. Be careful
     * with heap overflows.
     */
    public BasicImage getOneImage() {
        BasicImage[][] lay = new BasicImage[this.rows][this.cols];

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                lay[r][c] = loadTile(r, c);
            }
        }

        return BasicImage.concatenateImages(lay);
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
         * Initiates builder AND DELETES ALL FILES IN dir.
         *
         * @param rs No rows in tile layout.
         * @param cs No columns in tile layout.
         * @param dir Direction where tiles will be saved.
         */
        public Builder(int rs, int cs, Path dir) {
            this.rows = rs;
            this.cols = cs;
            this.dir = dir;

            cleanDir(dir.toFile());
        }


        /**
         * Deletes all content in dir.
         */
        private static void cleanDir(File dir) {
            if (!dir.isDirectory()) return;

            for (File f : dir.listFiles()) {
                if (f.isDirectory()) cleanDir(f);
                else f.delete();
            }
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
        public TiledImage_writable_untested build() {
            if (c != 0 || r != rows)
                throw new RuntimeException("Bad tile numbering");

            int w = tileW * (cols - 1) + lastColW;
            int h = tileH * (rows - 1) + lastRowH;

            return new TiledImage_writable_untested(dir, w, h, tileW, tileH, rows, cols);
        }
    }
}
