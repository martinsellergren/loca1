package map;

/**
 * A label on a map-image: text and position.
 *
 * @inv length(txt) > 0
 */
public class Label {
    public/***/ String text;
    public/***/ LabelLayout layout;

    /**
     * Construct a label.
     */
    public Label(String txt, LabelLayout lay) {
        if (txt.length() == 0)
            throw new IllegalArgumentException();

        this.text = txt;
        this.layout = lay;
    }

    /**
     * @return Label text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return Label layout (a copy).
     */
    public LabelLayout getLayout() {
        return this.layout.copy();
    }

    /**
     * @return A deep copy of the label.
     */
    public Label copy() {
        return new Label(this.text, this.layout.copy());
    }
}
