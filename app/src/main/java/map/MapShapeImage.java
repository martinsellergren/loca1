package map;

import java.io.IOException;
import java.util.Iterator;
import static loca.Utils.*;

/**
 * A map-image with map-object that roam inside a shape.
 */
public class MapShapeImage extends MapImage {

    /**
     * Use double quality images. Slower to fetch and analyse. */
    public static final boolean X2 = true;


    /**
     * Shape defining area of interest. This area is complete with
     * image and map-objects. */
    private Shape shape;

    /**
     * Fetches the map-image from bounds where shape fits perfectly,
     * then filters out map-objects outside shape sh.
     */
    public MapShapeImage(Shape sh, int z, Language lang) throws IOException {
        super(getView(sh, z), lang);
        this.shape = sh;
        this.objects.filter(sh, this.view);
    }

    /**
     * From existing images.
     */
    public MapShapeImage(TiledImage[] imgs, Shape sh, int z, Language lang) throws IOException {
        super(imgs, getView(sh, z), lang);
        this.shape = sh;
        this.objects.filter(sh, this.view);
    }

    /**
     * @return View..
     */
    private static MapImageView getView(Shape sh, int z) {
        return new MapImageView(sh.getBounds(), z, X2);
    }


    //---------------------------------------------WRAPPERS


    /**
     * Max allowed zoom-level. */
    public static final int MAX_ZOOM = 17;

    /**
     * Min allowed side-length of init-image, in pixels defined in
     * default pixel-density. */
    public/***/ static int MIN_INIT_SIDE_LENGTH = 200;

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
     * @param sh Shape. Must be valid (call validInitShape()).
     * @param lang Language of text-labels.
     * @return An "initial" image.
     */
    public static MapShapeImage fetchInit(Shape sh, Language lang) throws IOException {
        if (!validInitShape(sh))
            throw new IllegalArgumentException("Bad bounds!");

        int z = getBestZoom(sh, PREFERRED_INIT_AREA);
        if (z == 0) z = 1;
        LOGGER.info("Fetches init-map-image, z=" + z);

        return new MapShapeImage(sh, z, lang);
    }

    /**
     * @return False if bounds incorrectly assembled, or defining a
     * too smalll area (with regards to constraints).
     */
    public static boolean validInitShape(Shape sh) {
        double[] wsen = sh.getBounds();

        MapImageView v;
        try {
            boolean x2 = false;
            v = new MapImageView(wsen, MAX_INIT_ZOOM, x2);
        }
        catch (IllegalArgumentException e) {
            return false;
        }

        return
            v.width >= MIN_INIT_SIDE_LENGTH &&
            v.height >= MIN_INIT_SIDE_LENGTH;
    }

    /**
     * @param prefArea Preferred area of map-image, in pixels defined in
     * default pixel-quality.
     * @return The zoom-level that renders a map with closest area
     * to prefArea.
     * @pre Bounds are valid.
     */
    public/***/ static int getBestZoom(Shape sh, int prefArea) {
        long minDiff = Long.MAX_VALUE;
        int bestZ = -1;
        boolean x2 = false;
        double[] wsen = sh.getBounds();

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
    public static MapShapeImage fetchNext(MapShapeImage msimg) throws IOException {
        int z = msimg.view.zoom + 1;
        if (z == MAX_ZOOM) return null;

        LOGGER.info("Fetches next map-image, z=" + z);
        return new MapShapeImage(msimg.shape, z, msimg.language);
    }


    //------------------------------------------------testing

    @Override
    public BasicImage getAssembledImg() {
        BasicImage img = getImg();
        for (MapObject mo : this.objects)
            img.drawMapObject(mo);

        this.shape.drawYourself(img, this.view);
        return img;
    }
}
