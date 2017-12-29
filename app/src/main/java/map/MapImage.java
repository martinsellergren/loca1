package map;

import java.io.IOException;

/**
 * An image of a map with full specification of its labels
 * (letter-precise details). The image has a location on earth
 * and specified zoom through a MapImageView-object.
 */
public class MapImage extends BasicImage {
    public/***/ Label[] labels;
    public/***/ MapImageView view;

    /**
     * Constructs a map-image from an image of a map and additional
     * auxilary images for detection of labels. Given image is croped
     * to fit inside specified viewport.
     *
     * For creating this map-image only local data and functions are
     * used (like image-analysis and ocr-method). No need for a server
     * connection. Therefore, label categories are unspecified (need
     * a server query). So use fetchAndSetLabelCategories() afterwords.
     *
     * @param vv Viewport view, describing final viewport.
     * @param iv Image view, describing images.
     * @param mapImg A basic image of a map.
     * @param labelImg Equal to the map-image except
     * that everying except labels are transparent. Label areas are
     * extracted from this image in order to detect the label-text
     * (using OCR).
     * @param boxImg Box-image: equal to the labelImg except that
     * instead of letters are boxes (see {@link BoxImage}). Used
     * for detecting where labels are located and their internal
     * layout.
     *
     * @pre The viewport-view fits inside the image-view. Preferebly
     * it's substantially smaller so that the cut trash-labels
     * from image-sides doesn't show.
     * @pre mapImg, labelImg, boxImg all correspond to the image-view
     * (i.e same mid, dims, zoom, pixel-density) and are equal
     * except above described differences.
     */
    public MapImage(MapImageView vv, MapImageView iv, BasicImage mapImg, BasicImage labelImg, BasicImage boxImg) {
        super(null);
    }

    /**
     * Convenient constructor, same as above except that the three
     * images are represented in an array.
     *
     * @param vv Viewport view.
     * @param iw Image view.
     * @param imgs [mapImg, labelImg, boxImg]
     */
    public MapImage(MapImageView vv, MapImageView iv, BasicImage[] imgs) {
        this(vv, iv, imgs[0], imgs[1], imgs[2]);
    }

    /**
     * Fetches category for each label from online sources and sets
     * each label's category. Only sets the category if label-text
     * was found in online database (inside relevant bounding-box).
     *
     * @throws IOException if communication problem (network error?)
     */
    public void fetchAndSetLabelCategories() throws IOException {
    }

    /**
     * Removes all labels with unspecified category.
     */
    public void removeUnspecifiedLabels() throws IOException {
    }
}
