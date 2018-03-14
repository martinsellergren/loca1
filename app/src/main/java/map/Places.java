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
        LinkedList<LabelLayout> lays = LabelLayoutIterator.getLayouts(boxImg, view);
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
        LinkedList<LabelLayout> lays = LabelLayoutIterator.getLayouts(boxImg, view);
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
