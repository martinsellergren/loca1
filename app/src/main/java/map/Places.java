package map;

import java.util.LinkedList;
import java.util.Random;
import java.io.IOException;
import java.util.Iterator;
import java.awt.Color;

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
    public/***/ static final int LABEL_LAYOUT_ANALYSIS_SIZE = 1000;

    /**
     * List of places.
     * @inv Place-IDs are unique.
     */
    public/***/ LinkedList<Place> places = new LinkedList<Place>();

    /**
     * Finds label-layouts from a box-image, creates labels from the
     * layouts and a label-image, finally creates places using data
     * from internet.
     *
     * @param labelImg A label-image.
     * @param boxImg A box-image.
     * @param view A map-image-view describing labelImg and boxImg.
     * @param lang Language of text in labelImg.
     */
    public Places(TiledImage labelImg, TiledImage boxImg, MapImageView view, Language lang) throws IOException {
        LinkedList<LabelLayout> lays = getLabelLayouts(boxImg, view);
        LinkedList<Label> labs = getLabels(lays, labelImg, lang);
        LinkedList<Place> oneLabPs = getOneLabelPlaces(labs, view, lang);
        this.places = combinePlaces(oneLabPs);
    }

    /**
     * Constructs from complete list of places.
     * @pre Invariants hold in places-list.
     */
    public/***/ Places(LinkedList<Place> places) {
        this.places = places;
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

    //--------------------------------------------construction

    /**
     * Performs label-layout analysis for sub-images of a box-image.
     * Sub-image max-side-length is LABEL_LAYOUT_ANALYSIS_SIZE.
     *
     * @param bimg Box-image.
     * @param v View describing bimg.
     * @return Layouts of labels in bimg.
     */
    public/***/ LinkedList<LabelLayout> getLabelLayouts(TiledImage bimg, MapImageView v) throws IOException {
        LinkedList<LabelLayout> lays = new LinkedList<LabelLayout>();

        int extTerm = v.getExtensionTerm();
        int[] imgBs = new int[]{0, 0, bimg.getWidth()-1, bimg.getHeight()-1};
        LinkedList<int[]> bss = Math2.split(imgBs, LABEL_LAYOUT_ANALYSIS_SIZE);

        for (int[] bs : bss) {
            bs = Math2.extendBounds(bs, extTerm);
            BasicImage sub = bimg.getSubImage(bs);
            bs = Math2.getInsideBounds(bs, sub.getWidth(), sub.getHeight());

            LabelLayoutIterator iter = new LabelLayoutIterator(sub);
            LabelLayout lay;
            while ((lay = iter.next()) != null) {
                lay.addOffset(bs[0], bs[1]);
                lays.add(lay);
            }
        }

        return removeDuplicateLayouts(lays);
    }

    /**
     * Removes duplicate layous. Counts as duplicate if seems like
     * two labels have same text-label source in a map-image.
     *
     * @param lays Mighs contain duplicates.
     * @return List with no duplicates.
     */
    public/***/ LinkedList<LabelLayout> removeDuplicateLayouts(LinkedList<LabelLayout> lays) {
        LinkedList<LabelLayout> filtered = new LinkedList<LabelLayout>();
        for (LabelLayout lay : lays) {
            if (!containsLayout(lay, filtered))
                filtered.add(lay);
        }

        return filtered;
    }

    /**
     * @return True if lays contains lay (similar enough).
     */
    public/***/ boolean containsLayout(LabelLayout lay, LinkedList<LabelLayout> lays) {
        for (LabelLayout l : lays) {
            if (l.same(lay)) return true;
        }

        return false;
    }


    /**
     * Turn each label-layout into a label (unless junk!).
     *
     * @param lays Label-layouts.
     * @param limg Label-image.
     * @param lang Language of text in limg.
     * @return Labels described by lays.
     */
    public/***/ LinkedList<Label> getLabels(LinkedList<LabelLayout> lays, TiledImage limg, Language lang) throws IOException {
        LinkedList<Label> labs = new LinkedList<Label>();
        OCR ocr = new OCR(lang);

        for (LabelLayout lay : lays) {
            String txt = ocr.detectString(limg.extractLabel(lay));
            if (!isJunkLabelText(txt))
                labs.add(new Label(txt, lay));
        }

        ocr.end();
        return labs;
    }

    /**
     * @return True if txt couldn't possibly be a valid label text.
     */
    public/***/ boolean isJunkLabelText(String txt) {
        return txt.length() == 0;
    }

    /**
     * Try to turn each label into a place. Creates a place using
     * internet to fetch data. If no good data found, ignores label.
     *
     * @param labs Labels.
     * @param view View describing image of labs.
     * @return List of places each having only one label, where same
     * place-ID might occure multiple times.
     */
    public/***/ LinkedList<Place> getOneLabelPlaces(LinkedList<Label> labs, MapImageView view, Language lang) throws IOException {
        LinkedList<Place> ps = new LinkedList<Place>();

        for (Label lab : labs) {
            try {
                ps.add(new Place(lab, view, lang));
            }
            catch (UnknownPlaceException e) {
                System.out.println("Unknown place:\n" + e.getMessage());
            }
        }

        return ps;
    }

    /**
     * Combines places that have same id, i.e one place gets all
     * labels.
     *
     * @param oneLabPs List of one-label-places.
     * @return List of places where each place-ID is unique.
     */
    public/***/ LinkedList<Place> combinePlaces(LinkedList<Place> oneLabPs) {
        LinkedList<Place> ps = new LinkedList<Place>();

        for (Place oneLabP : oneLabPs) {
            Place p = findPlace(oneLabP.getID(), ps);

            if (p == null) ps.add(oneLabP);
            else p.addLabel(oneLabP.getLabels()[0]);
        }

        return ps;
    }

    /**
     * @return Place in ps with specified id, or NULL if none.
     */
    public/***/ Place findPlace(String id, LinkedList<Place> ps) {
        for (Place p : ps) {
            if (p.getID() == id) return p;
        }
        return null;
    }

    //--------------------------------------------for testing

    /**
     * Constructor without internet. Names from label-text and
     * categories hardcoded.
     * @param o Just to allow another constructor (pass null).
     */
    public Places(TiledImage labelImg, TiledImage boxImg, MapImageView view, Language lang, Object o) throws IOException {
        LinkedList<LabelLayout> lays = getLabelLayouts(boxImg, view);
        LinkedList<Label> labs = getLabels(lays, labelImg, lang);
        LinkedList<Place> oneLabPs = getOneLabelPlaces_(labs);
        this.places = combinePlaces(oneLabPs);
    }

    /**
     * @return One-label-places with name by label-text and hardcoded
     * category.
     */
    public/***/ LinkedList<Place> getOneLabelPlaces_(LinkedList<Label> labs) {
        LinkedList<Place> ps = new LinkedList<Place>();

        for (Label lab : labs) {
            ps.add(new Place(lab));
        }

        return ps;
    }
}
