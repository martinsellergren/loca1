package map;

import java.util.LinkedList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Color;

/**
 * Representation of a place on a map described by one or many labels.
 *
 * A place has a name, a category (e.g country/lake/road/) and layouts.
 * The name is the text in each label (described by layouts).
 * Each layout specifies integer-coordinates for all letters in the
 * label. The convention is that these coordinates are indexes in
 * a map-image where (0,0) means top-left point.
 *
 * @inv length(text) > 0
 * @inv length(layouts) >= 1
 */
public class Place {
    public/***/ String name;
    public/***/ Category category;
    public/***/ LinkedList<LabelLayout> layouts = new LinkedList<LabelLayout>();

    /**
     * Constructor from one layout.
     *
     * @param name Place-name. length(name) > 0
     * @param l Specification of the place-label's layout.
     * @param c Place-category.
     */
    public Place(String name, LabelLayout l, Category c) {
        this.name = name;
        this.layouts.add(l);
        this.category = c;
    }

    /**
     * Constructor with unknown category. Use fetch/setCategory()
     * later.
     */
    public Place(String name, LabelLayout l) {
        this(name, l, Category.UNKNOWN);
    }

    /**
     * Constructor from multiple layouts.
     */
    private Place(String name, LinkedList<LabelLayout> ls, Category c) {
        this.name = name;
        this.layouts = ls;
        this.category = c;
    }

    /**
     * Adds a unique layout to the place.
     * @return True if layout added. False if layout not unique so
     * not added.
     */
    public boolean addLayout(LabelLayout lay) {
        if (hasLayout(lay)) return false;
        layouts.add(lay);
        return true;
    }

    /**
     * @return True if place has layout lay.
     */
    public boolean hasLayout(LabelLayout lay) {
        for (LabelLayout l : layouts) {
            if (l.same(lay)) return true;
        }
        return false;
    }

    /**
     * @return Name of place.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Category of place.
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * @return Number of labels of this place.
     */
    public int getNoLabels() {
        return this.layouts.size();
    }

    /**
     * Add offset to every label of place.
     */
    public void addOffset(int addX, int addY) {
        for (LabelLayout l : this.layouts) {
            l.addOffset(addX, addY);
        }
    }

    /**
     * Fetches category from online-servers based on label's text
     * (i.e place-name) and geo-position.
     *
     * @param bounds [WNES] geo-bounds of the place.
     * @return Places' category.
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

    /**
     * @return A deep copy of this object.
     */
    public Place copy() {
        LinkedList<LabelLayout> ls = new LinkedList<LabelLayout>();

        for (LabelLayout l : layouts) {
            ls.add(l.copy());
        }

        return new Place(this.name, ls, this.category);
    }

    /**
     * Creates images (one per label) that covers the text of this
     * label.
     *
     * @param c Color of image.
     * @return Label-overlays, one per label in no particular order.
     */
    class LabelCover {
        public final BasicImage img;
        public final int[] topLeft;
        private LabelCover(BasicImage img, int[] tl) {
            this.img = img; this.topLeft = tl;
        }
    }
    public LabelCover[] getLabelOverlays(Color c) {
        LabelCover[] covs = new LabelCover[this.layouts.size()];
        double SCALE_FACTOR = 1.2;
        Color col = Color.RED;

        for (int i = 0; i < layouts.size(); i++) {
            LabelLayout lay = this.layouts.get(i).copy();
            int[] bs = Math2.toInt(Math2.scaleBounds(lay.getBounds(), SCALE_FACTOR));

            int[] tl = new int[]{bs[0], bs[1]};
            BasicImage img = new BasicImage(bs[2]-bs[0]+1, bs[3]-bs[1]+1);
            lay.addOffset(-tl[0], -tl[1]);
            img.drawLabelCover(lay, col);
            covs[i] = new LabelCover(img, tl);
        }

        return covs;
    }
}
