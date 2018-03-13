package map;

import java.util.LinkedList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.IOException;
import com.google.gson.JsonObject;

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

    /**
     * Expand label-area with this to get query-area. */
    public/***/ final double QUERY_AREA_EXPANSION_FACTOR = 1;

    /**
     * Expand label-bounds with this to get overlay-image dims. */
    public/***/ final double LABEL_OVERLAY_SCALE_FACTOR = 1.2;

    public/***/ String name;
    public/***/ Category category;
    public/***/ String id;
    public/***/ JsonObject data;
    public/***/ LinkedList<Label> labels = new LinkedList<Label>();

    /**
     * Constructs a place with one single label, by fetching data
     * from internet.
     *
     * @param lab Label.
     * @param view Describes the image of lab.
     * @param lang Language of preferred place-names.
     */
    public Place(Label lab, MapImageView view, Language lang) throws IOException, UnknownPlaceException {
        this.data = fetchData(lab, view, lang);
        this.name = PlaceQuery.getName(data);
        this.category = PlaceQuery.getCategory(data);
        this.id = PlaceQuery.getID(data);
        labels.add(lab);

        if (isJunk(lab, category))
            throw new UnknownPlaceException("Junk place:\n" + this.data);
    }

    /**
     * Constructor from known blocks.
     */
    public/***/ Place(String name, Category cat, String id, JsonObject data, LinkedList<Label> labs) {
        this.name = name;
        this.category = cat;
        this.id = id;
        this.data = data;
        this.labels = labs;
    }

    /**
     * @return True if label doesn't seem valid.
     */
    public/***/ boolean isJunk(Label lab, Category cat) {
        return
            this.category == Category.STREET
            && lab.getText().length() < 2;
    }

    /**
     * Fetch data about place from internet. Expands layout-bounds
     * to find query-area.
     *
     * @param lab A label of this place.
     * @param view Describing image of lab.
     * @param lang Prefered language of fetched data.
     */
    public/***/ JsonObject fetchData(Label lab, MapImageView view, Language lang) throws IOException, UnknownPlaceException {
        double[] bs = Math2.scaleBounds(lab.getLayout().getBounds(), QUERY_AREA_EXPANSION_FACTOR);
        double[] wsen = view.getGeoBounds(bs);
        return PlaceQuery.fetch(lab.getText(), wsen, lang);
    }

    /**
     * Adds label to the place, if unique layout.
     */
    public void addLabel(Label lab) {
        if (!hasLayout(lab.getLayout())) {
            labels.add(lab);
        }
    }

    /**
     * @return True if place has layout lay.
     */
    public boolean hasLayout(LabelLayout lay) {
        for (Label lab : labels) {
            if (lab.getLayout().same(lay)) return true;
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
     * @return ID of place.
     */
    public String getID() {
        return this.id;
    }

    /**
     * @return Labels of this place (copies).
     */
    public Label[] getLabels() {
        Label[] labs = new Label[this.labels.size()];

        for (int i = 0; i < this.labels.size(); i++) {
            labs[i] = this.labels.get(i).copy();
        }

        return labs;
    }

    /**
     * @return Number of labels of this place.
     */
    public int getNoLabels() {
        return this.labels.size();
    }

    /**
     * @return A deep copy of this object (data not a copy).
     */
    public Place copy() {
        LinkedList<Label> labs = new LinkedList<Label>();

        for (Label lab : this.labels) {
            labs.add(lab.copy());
        }

        return new Place(this.name, this.category, this.id, this.data, labs);
    }

    /**
     * Creates images (one per label) that covers the text of this
     * label.
     *
     * @param c Color of image.
     * @return Label-overlays, one per label in no particular order.
     */
    class LabelOverlay {
        public final BasicImage img;
        public final int[] topLeft;
        public/***/ LabelOverlay(BasicImage img, int[] tl) {
            this.img = img; this.topLeft = tl;
        }
    }
    public LabelOverlay[] getLabelOverlays(Color c) {
        Color col = Color.RED;
        LabelOverlay[] ovs = new LabelOverlay[this.labels.size()];

        for (int i = 0; i < ovs.length; i++) {
            LabelLayout lay = this.labels.get(i).getLayout();
            int[] bs = Math2.toIntBounds(Math2.scaleBounds(lay.getBounds(), LABEL_OVERLAY_SCALE_FACTOR));

            int[] tl = new int[]{bs[0], bs[1]};
            BasicImage img = new BasicImage(bs[2]-bs[0]+1, bs[3]-bs[1]+1);
            lay.addOffset(-tl[0], -tl[1]);
            img.drawLabelOverlay(lay, col);
            ovs[i] = new LabelOverlay(img, tl);
        }

        return ovs;
    }


    //---------------------------for testing

    /**
     * Constructor without internet and data. Name from label-text..
     */
    public Place(Label lab) {
        this.data = null;
        this.name = lab.getText();
        this.category = Category.OCEAN;
        this.id = this.name;
        labels.add(lab);
    }
}
