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
    private LinkedList<Place> places = new LinkedList<Place>();

    /**
     * Constructs from complete list of places.
     * @pre Invariants hold in places-list.
     */
    private Places(LinkedList<Place> places) {
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
     * Class for building the Places-object. Uses ocr. Does not use
     * internet until final build().
     */
    public static class Builder {

        /**
         * Width and height of image sent to label-layout-iter.
         * If too large, risk of heap-overflow. */
        private static final int LABEL_ANALYSIS_SIZE = 1000;

        /**
         * Uncomplete place-object (created without internet).
         */
        private class PlaceData {
            final String name;
            final LinkedList<LabelLayout> layouts = new LinkedList<LabelLayout>();

            private PlaceData(String name, LabelLayout lay) {
                this.name = name;
                this.layouts.add(lay);
            }

            private void addLayout(LabelLayout l) {
                this.layouts.add(l);
            }

            /**
             * @return True if place has layout lay.
             */
            private boolean hasLayout(LabelLayout lay) {
                for (LabelLayout l : layouts) {
                    if (l.same(lay)) return true;
                }
                return false;
            }
        }

        private MapImageView view;
        private LinkedList<PlaceData> places = new LinkedList<PlaceData>();

        /**
         * Finds label-layouts from a box-image, and their text using
         * a label-image and ocr.
         *
         * @param labelImg A label-image.
         * @param boxImg A box-image.
         * @param view A map-image-view describing labelImg and boxImg.
         * @param lang Language of text in labelImg.
         */
        public Builder(TiledImage labelImg, TiledImage boxImg, MapImageView view, Language lang) throws IOException {
            this.view = view;
            OCR ocr = new OCR(lang);
            int extTerm = view.getExtensionTerm();
            int[] imgBs = new int[]{0, 0, boxImg.getWidth()-1, boxImg.getHeight()-1};
            LinkedList<int[]> bss = Math2.split(imgBs, LABEL_ANALYSIS_SIZE);

            for (int[] bs : bss) {
                bs = new int[]{bs[0]-extTerm, bs[1]-extTerm, bs[2]+extTerm, bs[3]+extTerm};
                BasicImage boxImg_ = boxImg.getSubImage(bs);
                bs = Math2.getInsideBounds(bs, boxImg_.getWidth(), boxImg_.getHeight());
                LabelLayoutIterator iter = new LabelLayoutIterator(boxImg_);

                LabelLayout lay;
                while ((lay = iter.next()) != null) {
                    lay.addOffset(bs[0], bs[1]);
                    add(lay, labelImg, ocr);
                }
            }
            ocr.end();
        }

        /**
         * Adds a place to the list of places, or updates a current place.
         * Uses OCR method to find label-text (i.e place name). The
         * category of a new added place is set to unknown.
         *
         * - If layout exists in any place in list: done.
         * - If extracted label-text (i.e place-name) exists: add layout.
         * - Else add new place.
         *
         * @param lay Specification of label (letter positions).
         * @param labelImg An image containing the label described by lay.
         * @param ocr Ocr-engine set up for correct language.
         */
        private void add(LabelLayout lay, TiledImage labelImg, OCR ocr) throws IOException {
            if (layoutExists(lay)) return;

            String name = ocr.detectString(labelImg.extractLabel(lay));
            PlaceData p = findPlace(name);
            if (p != null)
                p.addLayout(lay);
            else
                this.places.add(new PlaceData(name, lay));
        }

        /**
         * @return True if any place has any layout same as lay.
         */
        private boolean layoutExists(LabelLayout lay) {
            for (PlaceData p : this.places) {
                if (p.hasLayout(lay)) return true;
            }
            return false;
        }

        /**
         * @return Place with specified name, or NULL.
         */
        private PlaceData findPlace(String name) {
            for (PlaceData p : this.places) {
                if (p.name.equals(name)) return p;
            }
            return null;
        }

        /**
         * Builds the Places-object. The incomplete Place-objects
         * (PlaceData) is evolved into real Place-objects using
         * internet.
         *
         * @param view Specifies the image where the label-layouts
         * resides.
         */
        public Places build() throws IOException {
            LinkedList<Place> ps = new LinkedList<Place>();

            for (PlaceData pd : this.places) {
                try {
                    ps.add(new Place(pd.name, pd.layouts, this.view));
                }
                catch (PlaceQuery.UnknownPlaceException e) {
                    System.out.println("Place not found:\n" + e.getMessage());
                }
            }

            return new Places(ps);
        }


        //--------------------------for testing

        /**
         * Build without internet. Category is dummy, data is null.
         */
        public Places build_() {
            LinkedList<Place> ps = new LinkedList<Place>();

            for (PlaceData pd : this.places) {
                ps.add(new Place(pd.name, pd.layouts));
            }

            return new Places(ps);
        }
    }
}
