package map;

import java.util.LinkedList;
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

    private String name;
    private Category category;
    private LinkedList<LabelLayout> layouts = new LinkedList<LabelLayout>();

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
    private MapObject(String n, Category c, LinkedList<LabelLayout> lays) {
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
}
