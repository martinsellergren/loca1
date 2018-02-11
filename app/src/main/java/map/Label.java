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
     * @param lay Specification of label (letter positions).
     * @param labelImg A label-image; see MapImage-constructor for a
     * description.
     * @param ocr Ocr-engine set up for correct language.
     *
     * @pre layout and labelImg are associated.
     */
    public Label(LabelLayout lay, BasicImage labelImg, OCR ocr) {
        this.text = ocr.detectString(labelImg.extractLabel(lay));
        this.category = Category.UNSPECIFIED;
        this.layout = lay;
    }

    /**
     * Construct a Label from known data.
     *
     * @param text Label-text. length(text) > 0
     * @param l Specification of the label.
     * @param c Label's category.
     */
    public Label(String text, LabelLayout l, Category c) {
        this.text = text;
        this.category = c;
        this.layout = l;
    }

    /**
     * @return Text of label.
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return Category of label.
     */
    public Category getCategory() {
        return this.category;
    }

    // /**
    //  * @return Layout of label.
    //  */
    // public LabelLayout getLayout() {
    //     return this.layout;
    // }

    /**
     * Add offset to every position in label.
     */
    public void addOffset(int addX, int addY) {
        this.layout.addOffset(addX, addY);
    }

    /**
     * Fetches category from online-servers based on label's text and
     * geo-position.
     *
     * @param bounds [WNES] geo-bounds of the label.
     * @return Category of the label.
     */
    public Category fetchCategory(double[] bounds) {
        return null;
    }

    /**
     * Sets category.
     */
    public void setCategory(Category c) {
        this.category = c;
    }
}
