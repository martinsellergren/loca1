package map;

/**
 * Representation of a map-label.
 *
 * A label has text, category (e.g country/lake/road/) and layout.
 * The layout specifies integer-coordinates for all letters in the
 * label. The convention is that these coordinates are indexes in
 * a map-image where (0,0) means top-left point.
 *
 * @inv length(text) > 0
 */
public class Label {
    public/***/ String text;
    public/***/ Category category;
    public/***/ LabelLayout layout;

    /**
     * Constructs a new label from a layout and the layout's
     * corresponding label-image. OCR-method is used to find
     * label-text. The category of the label is set to unspecified
     * (use setCategory() afterwords).
     *
     * @param layout Specification of label (letter positions).
     * @param labelImg A label-image; see MapImage-constructor for a
     * description.
     *
     * @pre layout and labelImg are associated.
     */
    public Label(LabelLayout layout, BasicImage labelImg) {
    }

    /**
     * Construct a Label from known data.
     *
     * @param text Label-text. length(text) > 0
     * @param l Specification of the label.
     * @param c Label's category.
     */
    public Label(String text, LabelLayout l, Category c) {
    }

    /**
     * Fetches category from online-servers based on label's text and
     * geo-position. Geo-position obtained from label's layout and
     * specification about the image where the label resides.
     *
     * @param v Specification about the map where the label resides.
     * @return Category of the label.
     */
    public Category fetchCategory(MapView v) {
        return null;
    }

    /**
     * Finds the geo-coordinates [lon, lat] for corner-points of
     * a non-rotated bounding box of this label.
     *
     * @param v Specification about the map where the label resides
     * for coordinate reference.
     * @return points[0] - top left corner point
     *         points[1] - top right
     *         points[2] - bottom right
     *         points[3] - bottom left
     */
    public/***/ double[][] getBoundingBoxCoordinates(MapView v) {
        return null;
    }

    /**
     * Sets category.
     */
    public void setCategory(Category c) {
        this.category = c;
    }
}
