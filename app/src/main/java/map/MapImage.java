package map;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Arrays;
import static loca.Utils.*;

/**
 * An image of a map with full specification of its labels
 * (letter-precise positions and categories). The image has a
 * location on earth and specified zoom through a MapImageView-object.
 */
public class MapImage {

    /**
     * Actual map-image. */
    public/***/ TiledImage img;

    /**
     * View describing img. */
    public/***/ MapImageView view;

    /**
     * Map-objects, i.e label-specification. */
    public/***/ MapObjects objects;

    /**
     * Language if text in labels. */
    public/***/ Language language;

    /**
     * Requested view, which was expanded..for testing. */
    public/***/ MapImageView reqView;

    /**
     * Fetches map-images from internet (full image, and auxiliary-
     * images used for analysis) and finds map-objects in them
     * (merged labels with unique text/category-combo).
     * Extends view to 1) Fit cut labels, 2) Detect edge-labels.
     * New image is stored in working-dir/zoom_level_x/full.
     *
     * @param v Describing area of interest. Created image will
     * be extended!
     * @param lang Fetch map-images with labels of this language.
     */
    public MapImage(MapImageView v, Language lang) throws IOException {
        MapImageView imgView = v.getExtendedView();
        Path p = Paths.get("zoom_level_" + v.zoom);
        MapRequest req = new MapRequest(imgView, p, lang);
        TiledImage[] imgs = req.fetch3();
        MapObjects mobs = new MapObjects(imgs[1], imgs[2], imgView);
        // imgs[1].delete();
        // imgs[2].delete();

        double[] viewBs = imgView.getPixelBoundsOfOtherView(v);
        mobs.filter(viewBs);

        this.img = imgs[0];
        this.view = imgView;
        this.objects = mobs;
        this.language = lang;
        this.reqView = v;
    }

    /**
     * Constructor from existing images.
     *
     * @param imgs [full label box]-img.
     * @param view Describing imgs.
     */
    public MapImage(TiledImage[] imgs, MapImageView view, Language lang) throws IOException {
        this.img = imgs[0];
        this.view = view;
        this.language = lang;
        this.objects = new MapObjects(imgs[1], imgs[2], view);
    }

    /**
     * @return Path of image (home for tiles).
     */
    public Path getImagePath() {
        return this.img.getDir();
    }

    /**
     * @return Map-objects in image, i.e detailed label-specification.
     */
    public MapObjects getObjects() {
        return this.objects;
    }

    /**
     * @return Language of map-labels.
     */
    public Language getLanguage() {
        return this.language;
    }

    /**
     * @return View describing map-image's location on earth.
     */
    public MapImageView getView() {
        return this.view;
    }

    //------------------------------------------------for testing

    /**
     * Beware of heap-overflow.
     */
    public BasicImage getImg() {
        return this.img.getOneImage();
    }

    /*
     * @return This image with drawn label-data.
     */
    public BasicImage getAssembledImg() {
        BasicImage img = getImg();
        for (MapObject mo : this.objects)
            img.drawMapObject(mo);

        double[] bs = this.view.getPixelBoundsOfOtherView(this.reqView);
        img.drawBounds(bs);

        return img;
    }
}
