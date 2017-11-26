package map;

import java.awt.geom.Point2D;

/**
 * Representation of a map-label.
 *
 * A label has text, category (e.g country/lake/road/) and layout.
 * The layout specifies integer-coordinates for all letters in the
 * label. The convention is that these coordinates are indexes in
 * a mapImage where (0,0) means top-left point.
 *
 * @inv length(text) > 0
 */
public class Label {
    private String text;
    private Category category;
    private LabelLayout layout;

    /**
     * Construct a Label from known data.
     *
     * @param text Label-text. length(text) > 0
     * @param l Basics; pos, dim.
     * @param c Label's category.
     */
    public Label(String text, LabelLayout l, Category c) {
    }

    /**
     * Constructs a label whithout category. Category is set to
     * unspecified.
     *
     * @param text Label-text. length(text) > 0
     * @param l Basics; pos, dim.
     */
    public Label(String text, LabelLayout l) {
    }

    /**
     * Fetches category from online-servers based on label-text and
     * layout together with basic map-data about the map where the
     * label resides. The layout and map-data is used to find a
     * bounding box when querying the online-database. The map-data
     * provides a coordinate reference (latitudes and longitudes).
     *
     * @param mb Map-data about the map where the label resides.
     * @return Category of the label.
     */
    public Category fetchCategory(MapBasics mb) {
        return null;
    }

    /**
     * Finds the coordinates for a non-rotated bounding box of this
     * label.
     *
     * @param mb Map-data for coordinate reference.
     * @return points[0] - top left corner point
     *         points[1] - top right
     *         points[2] - bottom right
     *         points[3] - bottom left
     */
    private Point2D.Double[] getBoundingBoxCoordinates(MapBasics mb) {
        return null;
    }

    /**
     * Sets category.
     */
    public void setCategory() {
    }
}
