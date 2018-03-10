package map;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * An image of a map with full specification of its labels
 * (letter-precise positions and osm-data). The image has a
 * location on earth and specified zoom through a MapImageView-object.
 */
public class MapImage {
    private TiledImage img;
    public/***/ MapImageView view;
    public/***/ Places places;

    /**
     * Fetches map-images from internet, finds places (labels) using
     * OCR and internet.
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
        this.places = new Places(imgs[1], imgs[2], view, lang);
        imgs[1].delete();
        imgs[2].delete();
    }

    /**
     * Constructor from existing images. Uses internet for place-data.
     * @param imgs [full label box]-img.
     */
    public MapImage(TiledImage[] imgs, MapImageView v, Language l) throws IOException {
        this.img = imgs[0];
        this.view = v;
        this.places = new Places(imgs[1], imgs[2], v, l);
    }

    /**
     * @return Path of image (home for tiles).
     */
    public Path getImagePath() {
        return this.img.getDir();
    }

    /**
     * @return Places in map-image.
     */
    public LinkedList<Place> getPlaces() {
        return this.places.getPlaces();
    }

    /**
     * @return View describing map-image location on earth.
     */
    public MapImageView getView() {
        return this.view;
    }

    //-------------------------for testing

    /**
     * Constructor from existing images. Uses internet for place-data.
     * @param imgs [full label box]-img.
     * @param o Just to allow another constructor (pass null).
     */
    public MapImage(TiledImage[] imgs, MapImageView v, Language l, Object o) throws IOException {
        this.img = imgs[0];
        this.view = v;
        this.places = new Places(imgs[1], imgs[2], v, l);
    }

    public BasicImage getImg() {
        return this.img.getOneImage();
    }
}
