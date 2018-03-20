package map;

import java.awt.Color;

/**
 * Creates images that covers labels in a map-image.
 *
 * @param c Color of image.
 * @return Label-overlays, one per label in no particular order.
 */
class LabelOverlay {

    /**
     * Expand label-bounds with this to get overlay-image dims. */
    public/***/ final double SCALE_FACTOR = 1.2;

    public final BasicImage img;
    public final int[] topLeft;

    /**
     * Constructs from a label-layout.
     */
    public LabelOverlay(LabelLayout lay) { this(lay, Color.RED); }
    public LabelOverlay(LabelLayout lay, Color col) {
        int[] bs = Math2.toIntBounds(Math2.scaleBounds(lay.getBounds(), SCALE_FACTOR));
        BasicImage img = new BasicImage(bs[2]-bs[0]+1, bs[3]-bs[1]+1);

        int[] tl = new int[]{bs[0], bs[1]};
        lay = lay.addOffset(-tl[0], -tl[1]);
        img.drawLabelOverlay(lay, col);

        this.img = img;
        this.topLeft = tl;
    }

    /**
     * Constructor from known blocks.
     */
    public LabelOverlay(BasicImage img, int[] tl) {
        this.img = img;
        this.topLeft = tl;
    }

    /**
     * @return Overlay-image.
     */
    public BasicImage getOverlayImage() {
        return this.img.copy();
    }

    /**
     * @return Top-left corner where overlay-image should be put.
     */
    public int[] getTopLeft() {
        return this.topLeft;
    }
}
