package map;

import java.io.IOException;

/**
 * A representation of a set of {@link Label}s.
 */
public class Labels {
    /**
     * Constructs a new set of labels by extracting label-data from
     * a labelImg and is's corresponding boxImg. OCR-method is used.
     *
     * @param labelImg A map-image where only the labels are visible.
     * @param boxImg Same as labelImg but instead of letters there are
     * boxes. A box in the boxImg reviles the location of a letter in
     * the labelImg for an image analyser.
     * @pre The labelImg and the boxImg have same dimensions, cover
     * same map-area and have same layout (font, sizes etc- except
     * that the boxImg has boxes instead of letters..).
     * @pre A clear font is used.
     * @pre The letters are far enough appart so that no boxes in
     * the boxImg connect.
     */
    public Labels(MapImage labelImg, BoxImage boxImg) {
    }

    /**
     * Fetches categories for each label from online sources and sets
     * the label's category.
     *
     * @param mb Map-data for the map of the labels. Needed for a
     * coordinate reference for the category-search-query bounding box.
     * @throws IOException if communication problem (network error?)
     */
    public void fetchAndSetCategories(MapBasics mb) throws IOException {
    }
}
