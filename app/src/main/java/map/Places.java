package map;

import java.util.LinkedList;
import java.util.Random;

/**
 * A list of places.
 *
 * @inv Place-names unique.
 * @inv Every layout of every place is unique.
 */
public class Places {
    private LinkedList<Place> places = new LinkedList<Place>();

    /**
     * Empty constructor: use add().
     */
    public Places() {}

    /**
     * Constructs from complete list of places.
     * @pre Invariants hold in places-list.
     */
    private Places(LinkedList<Place> places) {
        this.places = places;
    }

    /**
     * Adds a place to the list of places, or updates a current place.
     * Uses OCR method to find label-text (i.e place name). The
     * category of a new added place is set to unknown
     * (use setCategory() later).
     *
     * - If layout exists in any place in list: done.
     * - If extracted label-text (i.e place-name) exists: add layout.
     * - Else add new place.
     *
     * @param lay Specification of label (letter positions).
     * @param img An image containing the label described by lay.
     * @param ocr Ocr-engine set up for correct language.
     */
    public void add(LabelLayout lay, BasicImage img, OCR ocr) {
        if (layoutExists(lay)) return;

        String name = ocr.detectString(img.extractLabel(lay));
        Place p = findPlace(name);
        if (p != null) {
            p.addLayout(lay);
        }

        else {
            places.add(new Place(name, lay));
        }
    }

    /**
     * @return True if any place has any layout same as lay.
     */
    private boolean layoutExists(LabelLayout lay) {
        for (Place p : this.places) {
            if (p.hasLayout(lay)) return true;
        }
        return false;
    }

    /**
     * @return Place with specified name, or NULL.
     */
    public Place findPlace(String name) {
        for (Place p : this.places) {
            if (p.getName().equals(name)) return p;
        }
        return null;
    }

    /**
     * Removes a random place from list.
     * @return Removed element.
     */
    public Place removeRandom() {
        int i = new Random().nextInt(this.places.size());
        return this.places.remove(i);
    }

    /**
     * @return A list of the places (not copies).
     */
    public LinkedList<Place> getPlaces() {
        return (LinkedList<Place>) this.places.clone();
    }

    /**
     * @return A deep copy of this object.
     */
    public Places copy() {
        LinkedList<Place> ps = new LinkedList<Place>();

        for (Place p : this.places) {
            ps.add(p.copy());
        }

        return new Places(ps);
    }
}
