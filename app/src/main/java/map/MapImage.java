package map;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Arrays;
import static loca.Utils.*;

/**
 * An image of a map with full specification of its labels
 * (letter-precise positions and categories). The image has a
 * location on earth and specified zoom through a MapImageView-object.
 */
public class MapImage {

    /**
     * Actual map-image. */
    public/***/ TiledImage img;

    /**
     * View describing img. */
    public/***/ MapImageView view;

    /**
     * Map-objects, i.e label-specification. */
    public/***/ MapObjects objects;

    /**
     * Language if text in labels. */
    public/***/ Language language;

    /**
     * Requested view, which was expanded. */
    public/***/ MapImageView reqView;

    /**
     * Fetches map-images from internet (full image, and auxiliary-
     * images used for analysis) and finds map-objects in them
     * (merged labels with unique text/category-combo).
     * Extends view to 1) Fit cut labels, 2) Detect edge-labels.
     * New image is stored in working-dir/zoom_level_x/full.
     *
     * @param v Describing area of interest. Created image will
     * be extended!
     * @param lang Fetch map-images with labels of this language.
     */
    public MapImage(MapImageView v, Language lang) throws IOException {
        MapImageView imgView = v.getExtendedView();
        Path p = Paths.get("zoom_level_" + v.zoom);
        MapRequest req = new MapRequest(imgView, p, lang);
        TiledImage[] imgs = req.fetch3();
        MapObjects mobs = new MapObjects(imgs[1], imgs[2], imgView);
        imgs[1].delete();
        imgs[2].delete();

        double[] viewBs = imgView.getPixelBoundsOfOtherView(v);
        mobs.filter(viewBs);

        this.img = imgs[0];
        this.view = imgView;
        this.objects = mobs;
        this.language = lang;
        this.reqView = v;
    }

    /**
     * Constructor from existing images.
     *
     * @param imgs [full label box]-img.
     * @param view Describing imgs.
     */
    public MapImage(TiledImage[] imgs, MapImageView view, Language lang) throws IOException {
        this.img = imgs[0];
        this.view = view;
        this.language = lang;
        this.objects = new MapObjects(imgs[1], imgs[2], view);
    }

    /**
     * @return Path of image (home for tiles).
     */
    public Path getImagePath() {
        return this.img.getDir();
    }

    /**
     * @return Map-objects in image, i.e detailed label-specification.
     */
    public MapObjects getObjects() {
        return this.objects;
    }

    /**
     * @return Language of map-labels.
     */
    public Language getLanguage() {
        return this.language;
    }

    /**
     * @return View describing map-image's location on earth.
     */
    public MapImageView getView() {
        return this.view;
    }

    //---------------------------------------------WRAPPERS

    /**
     * Use double quality images. Slower to fetch and analyse. */
    public static final boolean X2 = true;

    /**
     * Max allowed zoom-level. */
    public static final int MAX_ZOOM = 17;

    /**
     * Min allowed side-length, in pixels defined in default
     * pixel-density. */
    public/***/ static int MIN_SIDE_LENGTH = 200;

    /**
     * Preferred area of init-image, in pixels defined in
     * default pixel-density. */
    public/***/ static int PREFERRED_INIT_AREA = 300 * 300;

    /**
     * Max allowed zoom for init-image. */
    public/***/ static int MAX_INIT_ZOOM = MAX_ZOOM - 3;

    /**
     * Finds and fetches an "initial" zoomed-out image. Zoom
     * level is adjusted to comply with init-dims-constraints.
     *
     * @param wsen Map-image bounds. Must be valid (call validView()).
     * @param lang Language of text-labels.
     * @return An "initial" image.
     */
    public static MapImage fetchInit(double[] wsen, Language lang) throws IOException {
        if (!validBounds(wsen))
            throw new IllegalArgumentException("Bad bounds!");

        int z = getBestZoom(wsen, PREFERRED_INIT_AREA);
        MapImageView v = new MapImageView(wsen, z, X2);
        LOGGER.info("Fetches init-map-image, z=" + z);

        return new MapImage(v, lang);
    }

    /**
     * @return False if bounds incorrectly assembled, or defining a
     * too smalll area (with regards to constraints).
     */
    public static boolean validBounds(double[] wsen) {
        MapImageView v;
        try {
            boolean x2 = false;
            v = new MapImageView(wsen, MAX_INIT_ZOOM, x2);
        }
        catch (IllegalArgumentException e) {
            return false;
        }

        return
            v.width >= MIN_SIDE_LENGTH &&
            v.height >= MIN_SIDE_LENGTH;
    }

    /**
     * @param prefArea Preferred area of map-image, in pixels defined in
     * default pixel-quality.
     * @return The zoom-level that renders a map with closest area
     * to prefArea.
     * @pre Bounds are valid.
     */
    public/***/ static int getBestZoom(double[] wsen, int prefArea) {
        long minDiff = Long.MAX_VALUE;
        int bestZ = -1;
        boolean x2 = false;

        for (int z = 0; z <= MAX_ZOOM; z++) {
            MapImageView v = new MapImageView(wsen, z, x2);
            Long diff = Math.abs((long)v.width * v.height - prefArea);
            if (diff < minDiff) {
                minDiff = diff;
                bestZ = z;
            }
        }

        return bestZ;
    }

    /**
     * Fetch a map image covering same area and has same language
     * as source-image but has an increased zoom-level.
     *
     * @param src Source-image.
     * @return Next map-image (increased zoom-level), or NULL if
     * no more nexts (no more zoom-levels..).
     */
    public static MapImage fetchNext(MapImage mimg) throws IOException {
        int z = mimg.reqView.zoom + 1;
        if (z == MAX_ZOOM) return null;

        double[] wsen = mimg.reqView.getGeoBounds();
        MapImageView v = new MapImageView(wsen, z, X2);

        LOGGER.info("Fetches next map-image, z=" + z);
        return new MapImage(v, mimg.getLanguage());
    }

    //------------------------------------------------for testing

    /**
     * Beware of heap-overflow.
     */
    public BasicImage getImg() {
        return this.img.getOneImage();
    }

    /*
     * @return This image with drawn label-data.
     */
    public BasicImage getAssembledImg() {
        BasicImage img = getImg();
        for (MapObject mo : this.objects)
            img.drawMapObject(mo);

        double[] bs = this.view.getPixelBoundsOfOtherView(this.reqView);
        img.drawBounds(bs);

        return img;
    }
}
