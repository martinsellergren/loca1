package map;

import java.awt.image.BufferedImage;

/**
 * An image with some loosly map-oriented behaviour.
 */
public class MapImage {
    private BufferedImage img;

    /**
     * Constructs a MapImage from a BufferedImage.
     */
    public MapImage(BufferedImage img) {
    }

    /**
     * Rotates image.
     * @param deg Rotation in degrees.
     */
    public void rotate(double deg) {
    }

    /**
     * Extracts a rectangular area from the image.
     *
     * @param b {@link Box} specifying area to extract.
     * @return New image - a non-rotated rectangle where the extracted area fits perfectly.
     */
    public MapImage extract(Box b) {
        return null;
    }

    /**
     * Detects text of label in the image specified by a label layout.
     * Uses OCR-methods.
     *
     * @param layout Specification for the label to be detected.
     * @return Text of label.
     */
    public String detectLabel(LabelLayout layout) {
        return null;
    }
}
