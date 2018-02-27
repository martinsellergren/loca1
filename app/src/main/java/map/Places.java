package map;

import java.util.LinkedList;
import java.util.Random;
import java.io.IOException;
import java.util.Iterator;

/**
 * A list of places (location, name, category).
 *
 * @inv Place-names unique.
 * @inv Every layout of every place is unique.
 * @inv No label has unknown category.
 */
public class Places {

    /**
     * Width and height of image sent to label-layout-iter.
     * If too large, risk of heap-overflow. */
    private static final int LABEL_ANALYSIS_SIZE = 1000;

    private LinkedList<Place> places = new LinkedList<Place>();

    /**
     * Finds label-layouts from a box-image, and their text using
     * a label-image and ocr. Label-categories from internet using
     * the label-text and location on earth. Removes labels that
     * failes to receive a category.
     *
     * @param labelImg A label-image.
     * @param boxImg A box-image.
     * @param view A map-image-view describing labelImg and boxImg.
     * @param lang Language of text in labelImg.
     */
    public Places(TiledImage labelImg, TiledImage boxImg, MapImageView view, Language lang) throws IOException {
        OCR ocr = new OCR(lang);
        int extTerm = view.getExtensionTerm();
        int[] imgBs = new int[]{0, 0, boxImg.getWidth()-1, boxImg.getHeight()-1};
        LinkedList<int[]> bss = Math2.split(imgBs, LABEL_ANALYSIS_SIZE);

        for (int[] bs : bss) {
            bs = new int[]{bs[0]-extTerm, bs[1]-extTerm, bs[2]+extTerm, bs[3]+extTerm};
            LabelLayoutIterator iter = new LabelLayoutIterator(boxImg, bs);

            LabelLayout lay;
            while ((lay = iter.next()) != null) {
                lay.addOffset(bs[0], bs[1]);
                add(lay, labelImg, ocr);
            }
        }
        ocr.end();
        fetchLabelCategories(view);
        removeUnknownPlaces();
    }

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
     * @param labelImg An image containing the label described by lay.
     * @param ocr Ocr-engine set up for correct language.
     */
    private void add(LabelLayout lay, TiledImage labelImg, OCR ocr) {
        if (layoutExists(lay)) return;

        String name = ocr.detectString(labelImg.extractLabel(lay));
        Place p = findPlace(name);
        if (p != null) {
            p.addLayout(lay);
        }
        else {
            this.places.add(new Place(name, lay));
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

    /**
     * Fetches and sets categories for every place. Queries the area
     * around the place-location (one of them). If fails to find a
     * category, leaves this place untouched.
     */
    private void fetchLabelCategories(MapImageView v) throws IOException {
        for (Place p : this.places) {
            p.setCategory(Category.OCEAN);
        }
    }

    /**
     * Removes all places with unknown category.
     */
    private void removeUnknownPlaces() {
        Iterator<Place> iter = this.places.iterator();
        while (iter.hasNext()) {
            Place p = iter.next();

            if (p.getCategory() == Category.UNKNOWN)
                iter.remove();
        }
    }
}
