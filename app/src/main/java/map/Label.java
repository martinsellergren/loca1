package map;

import java.io.IOException;

/**
 * A label on a map-image: text and position.
 *
 * @inv length(txt) > 0
 * @inv No junk: One letter road-names..
 */
public class Label {
    public/***/ String text;
    public/***/ Category category;
    public/***/ LabelLayout layout;

    public static class JunkException extends Exception {
        public JunkException(String msg) { super(msg); }
    }

    /**
     * Finds text by decoding codeImg, category by decoding boxImg.
     *
     * @throws JunkException if lay describes a junk-label.
     */
    public Label(LabelLayout lay, TiledImage codeImg, TiledImage boxImg) throws JunkException, UnknownCharacterException, UnknownCategoryException, IOException {
        this.layout = lay;
        this.text = LabelTextDecoder.decode(lay, codeImg);

        try {
            this.category = CategoryDecoder.decode(lay, boxImg);
        }
        catch (UnknownCategoryException e) {
            throw new UnknownCategoryException("Unknown label category of label with text: " + this.text);
        }

        if (isJunkLabel(text, category))
            throw new JunkException(String.format("Junk: %s, %s", text, category));
    }

    /**
     * @return True if label has junk text/category.
     */
    public/***/ boolean isJunkLabel(String text, Category c) {
        return
            text.length() == 0 ||
            text.length() == 1 && c == Category.STREET;
    }

    /**
     * Construct a label from known blocks.
     */
    public Label(String txt, Category cat, LabelLayout lay) {
        if (txt.length() == 0)
            throw new IllegalArgumentException();

        this.text = txt;
        this.category = cat;
        this.layout = lay;
    }

    /**
     * @return Label's text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return Label's category.
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * @return Label's layout (a copy).
     */
    public LabelLayout getLayout() {
        return this.layout.copy();
    }

    /**
     * @return A deep copy of the label.
     */
    public Label copy() {
        return new Label(this.text, this.category, this.layout.copy());
    }

    @Override
    public String toString() {
        return String.format("text: %s\ncategory: %s\n midP: %s",
                             this.text,
                             this.category,
                             Math2.toInt(this.layout.getMid()));
    }
}
