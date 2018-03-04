package map;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An image of a map with full specification of its labels
 * (letter-precise details). The image has a location on earth
 * and specified zoom through a MapImageView-object.
 */
public class MapImage {
    private TiledImage img;
    public/***/ MapImageView view;
    public/***/ Places places;

    /**
     * Fetches map-images from internet, finds places (labels) using
     * OCR and internet.
     * Extends bounds to 1) Fit cut labels, 2) Detect edge-labels.
     *
     * @param wsen [west south east north] bounds.
     * @param zoom 0-20.
     * @param doubleQ Double quality image (double pixel density).
     * @param lang Fetch map-images with labels of this language.
     */
    public MapImage(double[] wsen, int zoom, boolean doubleQ, Language lang) throws IOException {
        // this.view = new MapImageView(wsen, zoom, doubleQ).getExtendedView();
        // Path p = Paths.get("zoom_level_" + zoom);
        // MapRequest req = new MapRequest(view, p, lang);
        // TiledImage[] imgs = req.fetch3();
        // this.img = imgs[0];
        // this.places = new Places(imgs[1], imgs[2], view, lang);
        // places.fetchCategories(view);
        // places.removeUnknownPlaces();
    }

    // /**
    //  * Constructs a map-image from an image of a map and additional
    //  * auxilary images for detection of labels. Given image is croped
    //  * to fit inside specified viewport.
    //  *
    //  * For creating this map-image only local data and functions are
    //  * used (like image-analysis and ocr-method). No need for a server
    //  * connection. Therefore, label categories are unspecified (need
    //  * a server query). So use fetchAndSetLabelCategories() afterwords.
    //  *
    //  * @param vv Viewport view, describing final viewport.
    //  * @param iv Image view, describing images.
    //  * @param mapImg A basic image of a map.
    //  * @param labelImg Equal to the map-image except
    //  * that everying except labels are transparent. Label areas are
    //  * extracted from this image in order to detect the label-text
    //  * (using OCR).
    //  * @param boxImg Box-image: equal to the labelImg except that
    //  * instead of letters are boxes (see {@link BoxImage}). Used
    //  * for detecting where labels are located and their internal
    //  * layout.
    //  *
    //  * @pre The viewport-view fits inside the image-view. Preferebly
    //  * it's substantially smaller so that the cut trash-labels
    //  * from image-sides doesn't show.
    //  * @pre mapImg, labelImg, boxImg all correspond to the image-view
    //  * (i.e same mid, dims, zoom, pixel-density) and are equal
    //  * except above described differences.
    //  */
    // public MapImage(MapImageView vv, MapImageView iv, BasicImage mapImg, BasicImage labelImg, BasicImage boxImg) {
    //     super(null);
    // }

    // /**
    //  * Convenient constructor, same as above except that the three
    //  * images are represented in an array.
    //  *
    //  * @param vv Viewport view.
    //  * @param iw Image view.
    //  * @param imgs [mapImg, labelImg, boxImg]
    //  */
    // public MapImage(MapImageView vv, MapImageView iv, BasicImage[] imgs) {
    //     this(vv, iv, imgs[0], imgs[1], imgs[2]);
    // }

    // /**
    //  * Fetches category for each label from online sources and sets
    //  * each label's category. Only sets the category if label-text
    //  * was found in online database (inside relevant bounding-box).
    //  *
    //  * @throws IOException if communication problem (network error?)
    //  */
    // public void fetchAndSetLabelCategories() throws IOException {
    // }

    // /**
    //  * Removes all labels with unspecified category.
    //  */
    // public void removeUnspecifiedLabels() throws IOException {
    // }
}
