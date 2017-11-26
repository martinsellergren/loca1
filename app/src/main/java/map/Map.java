package map;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A map with full specification of its labels.
 * A map is represented as basic map data (area covered, zoom),
 * a map-image and detailed label data (letter-precice details for
 * each label).
 *
 * @inv mapImage width, height > 0
 */
public class Map {
    private MapBasics mapBasics;
    private MapImage mapImage;
    private Labels labels;

    /**
     * Constructs a map from a provided map-image and by
     * extracting label-info from a label-image and a box-image.
     *
     * For creating this map, only local data and functions are used
     * (like image-analysis and ocr-method). No need for a server
     * connection. Therefore, label categories are unspecified (needs
     * a server query). So use fetchAndSetLabelCategories() afterwords.
     *
     * @param mb Map-basics (covered area, zoom).
     * @param mapImg Map-image.
     * @param labelImg Label-image: equal to the map-image except
     * that everying except labels are transparent. Label areas are
     * extracted from this image in order to detect the label-text
     * (using OCR). If labelImg is NULL, a copy of the map-image is
     * used as labal-image.
     * @param boxImg Box-image: equal to the labelImg except that
     * instead of letters are boxes (see {@link BoxImage}). Used
     * for detecting where labels are located and their internal
     * layout.
     *
     * @pre mapImg, labelImg, boxImg all correspond to the basic
     * map specification and are equal except above described
     * differences.
     */
    public Map(MapBasics mb, MapImage mapImg, MapImage labelImg, BoxImage boxImg) {
    }

    /**
     * Convenient constructor, same as above except that the three
     * images are represented as (raw) buffered images in an array.
     *
     * @param mb Map-basics (covered area, zoom).
     * @param imgs
     *  imgs[0] - map-image
     *  imgs[1] - label-image
     *  imgs[2] - box-image
     */
    public Map(MapBasics mb, BufferedImage[] imgs) {
    }

    /**
     * Constructs a map from a map-layout-structure. This structure
     * is a quadratic matrix where each element is a subpart of the
     * constructed map.
     *
     * @param maps A map-layout-structure of subset-maps.
     * @pre The subset-maps are equally dimensioned and has same zoom.
     * They fit perfectly together by concatenating the map-images.
     */
    public Map(Map[][] maps) {
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
