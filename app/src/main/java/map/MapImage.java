package map;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * An image of a map with full specification of its labels
 * (letter-precise positions and categories). The image has a
 * location on earth and specified zoom through a MapImageView-object.
 */
public class MapImage {
    public/***/ TiledImage img;
    public/***/ MapImageView view;
    public/***/ MapObjects objects;

    /**
     * Fetches map-images from internet (full image, and auxiliary-
     * images used for analysis) and finds map-objects in them
     * (merged labels with unique text/category-combo).
     * Extends view to 1) Fit cut labels, 2) Detect edge-labels.
     * New image is stored in working-dir/zoom_level_x/full.
     *
     * @param view Describing area of interest; location on earth,
     * zoom-level, quality. Created image will be extended.
     * @param lang Fetch map-images with labels of this language.
     */
    public MapImage(MapImageView v, Language lang) throws IOException {
        this.view = v.getExtendedView();
        Path p = Paths.get("zoom_level_" + v.zoom);
        MapRequest req = new MapRequest(view, p, lang);
        TiledImage[] imgs = req.fetch3();
        this.img = imgs[0];
        this.objects = new MapObjects(imgs[1], imgs[2], view);
        imgs[1].delete();
        imgs[2].delete();
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
     * @return View describing map-image location on earth.
     */
    public MapImageView getView() {
        return this.view;
    }

    //-------------------------for testing

    /**
     * Beware of heap-overflow.
     */
    public BasicImage getImg() {
        return this.img.getOneImage();
    }
}
