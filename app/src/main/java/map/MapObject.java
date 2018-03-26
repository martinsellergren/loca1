package map;

import java.util.LinkedList;
import java.util.List;
import java.awt.Color;
import java.io.IOException;

/**
 * Representation of a map-object described by one or many
 * map-label-layouts on an image of a map.
 *
 * @inv len(name) > 0
 * @inv len(layouts) >= 1
 * @inv Layouts in layouts-list are unique.
 */
public class MapObject {

    public/***/ String name;
    public/***/ Category category;
    public/***/ LinkedList<LabelLayout> layouts = new LinkedList<LabelLayout>();

    /**
     * Constructs a one-layout map-object from a label.
     */
    public MapObject(Label lab) {
        this.name = lab.getText();
        this.category = lab.getCategory();
        this.layouts.add(lab.getLayout());
    }

    /**
     * Constructor from known blocks, where invariants hold.
     */
    public/***/ MapObject(String n, Category c, LinkedList<LabelLayout> lays) {
        this.name = n;
        this.category = c;
        this.layouts = lays;
    }

    /**
     * Adds a unique layout to list of layouts.
     * @return True if lay added (is unique).
     */
    public boolean addLayout(LabelLayout lay) {
        if (!lay.sameAsAny(this.layouts)) {
            this.layouts.add(lay);
            return true;
        }
        else return false;
    }

    /**
     * @return True if map-object corresponds to label, i.e:
     *  - mob.name = lab.text, and
     *  - mod.category = lab.category
     */
    public boolean correspondsTo(Label lab) {
        return
            getName().equals(lab.getText()) &&
            getCategory() == lab.getCategory();
    }

    /**
     * @return Name of map-object.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Category of map-object.
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * @return Array of layouts (copies).
     */
    public LabelLayout[] getLayouts() {
        LabelLayout[] lays = new LabelLayout[ this.layouts.size() ];
        for (int i = 0; i < lays.length; i++) {
            lays[i] = this.layouts.get(i).copy();
        }
        return lays;
    }

    /**
     * @return Number of layouts.
     */
    public int getNoLayouts() {
        return this.layouts.size();
    }

    /**
     * @return A deep copy.
     */
    public MapObject copy() {
        LinkedList<LabelLayout> laysCpy = new LinkedList<LabelLayout>();
        for (LabelLayout lay : this.layouts) {
            laysCpy.add(lay.copy());
        }
        return new MapObject(this.name, this.category, laysCpy);
    }

    /**
     * Removes layouts outside bounds.
     *
     * @param bs [xmin ymin xmax ymax] in pixels.
     * @return Map-object with <= current number of layouts, or NULL
     * if no layouts left.
     */
    public MapObject filter(double[] bs) {
        LinkedList<LabelLayout> filtered = new LinkedList<LabelLayout>();

        for (LabelLayout lay : this.layouts) {
            double[] mid = lay.getMid();
            if (Math2.isInsideBounds(bs, mid))
                filtered.add(lay);
        }

        if (filtered.size() > 0)
            return new MapObject(this.name, this.category, filtered);
        else return null;
    }

    /**
     * Removes layouts outside shape.
     *
     * @param sh Shape.
     * @param v View describing image where this map-object resides.
     * @return Map-object with <= current number of layouts, or NULL
     * if no layouts left.
     */
    public MapObject filter(Shape sh, MapImageView v) {
        LinkedList<LabelLayout> filtered = new LinkedList<LabelLayout>();

        for (LabelLayout lay : this.layouts) {
            double[] mid = lay.getMid();
            double[] ll = v.getGeoCoordinates(mid[0], mid[1]);

            if (sh.isInside(ll) ||
                sh.isInside(new double[]{ll[0]-360, ll[1]}) ||
                sh.isInside(new double[]{ll[0]+360, ll[1]}))
                filtered.add(lay);
        }

        if (filtered.size() > 0)
            return new MapObject(this.name, this.category, filtered);
        else return null;
    }
}
