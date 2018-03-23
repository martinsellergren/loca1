// package map;

// import java.util.LinkedList;
// import java.io.IOException;

// /**
//  * A map is represented as a defined area on earth and an array
//  * of map-images ({@link MapImage}) of different zoom-levels
//  * covering this area.
//  *
//  * @inv Every zoom-level's map-image covers the map's area.
//  * @inv Strictly increasing zoom-level in zoom-level-imgs-list.
//  * @inv len(zoom-level-imgs) > 0
//  */
// public class Map {

//     /**
//      * Use double quality images. Slower to fetch and analyse. */
//     public static final boolean X2 = true;

//     /**
//      * Max allowed zoom-level. */
//     public/***/ static final int MAX_ZOOM = 17;

//     /**
//      * Preferred area of first-zoom-level-image, in pixels defined in
//      * default pixel-density (i.e no doubled image-quality). */
//     public/***/ static int PREFERRED_INIT_AREA = 1000 * 1000;

//     /**
//      * Min allowed side-length of first-zoom-level-image, in pixels
//      * defined in default pixel-density. */
//     public/***/ static int MIN_INIT_SIDE_LENGTH = 1000;

//     /**
//      * Max allowed zoom for first zoom-level. */
//     public/***/ static int MAX_INIT_ZOOM = MAX_ZOOM - 4;


//     /** [west, south, east, north]-latlon-bounds. */
//     public/***/ final double[] wsen;

//     /** Map-images of zoom-levels, ordered by increasing zoom. */
//     public/***/ final LinkedList<MapImage> zimgs;

//     /** Language of map-labels. */
//     public/***/ final Language language;

//     /**
//      * Constructs a map with one zoom-level. Fetches and analyses
//      * map-images.
//      *
//      * @param wsen Lat/lon-bounds.
//      * @param lang Language of label-text in zoomLevel-images.
//      * @throws IllegalArgumentException if bad bounds. Test bounds
//      * beforehand!
//      */
//     public Map(double[] wsen, Language lang) throws IOException {
//         MapImage zimg0 = findFirstZImg(wsen, lang);
//         if (zimg0 == null)
//             throw new IllegalArgumentException("Bad map-bounds");

//         this.wsen = wsen.clone();
//         this.language = lang;
//         this.zimgs = new LinkedList<MapImage>();
//         zimgs.add(zimg0);
//     }

//     /**
//      * Finds and fetches first zoom-level-image, i.e an image of some
//      * zoom-level with dimensions matching preference defined by
//      * constants.
//      *
//      * @return First zoom-level-image of this map, or NULL if
//      * none appropriate (bad wsen-bounds).
//      */
//     public/***/ static MapImage findFirstZImg(double[] wsen, Language lang) throws IOException {
//         if (!validStartBounds(wsen)) return null;
//         int z = getZoom(wsen, PREFERRED_INIT_AREA);
//         MapImageView v = new MapImageView(wsen, z, X2);
//         return new MapImage(v, lang);
//     }


//     /**
//      * Fetches and adds next zoom-level-image, i.e and image with
//      * one step closer zoom. If already has closest zoom-level,
//      * adds nothing.
//      *
//      * @return True if zoom-level added.
//      */
//     public boolean addNextZImg() throws IOException {
//         if (zimgs.getLast().getView().zoom == MAX_ZOOM)
//             return false;

//         int z = zimgs.getLast().getView().zoom + 1;
//         MapImageView v = new MapImageView(this.wsen, z, this.X2);
//         this.zimgs.add( new MapImage(v, this.language) );
//         return true;
//     }

//     // /**
//     //  * @param z Zoom-level.
//     //  * @return True if z present.
//     //  */
//     // public boolean hasZoomLevel(int z) {
//     //     for (MapImage mimg : this.zimgs)
//     //         if (z == mimg.getView().zoom) return true;
//     //     return false;
//     // }

//     /**
//      * @return [WSEN].
//      */
//     public double[] getBounds() {
//         return this.wsen.clone();
//     }

//     /**
//      * @return Language of map-labels.
//      */
//     public Language getLanguage() {
//         return this.language;
//     }

//     /**
//      *
//      */
//     public MapImage findZImage(int i) {
//         if (hasZoomLevel(z)
//     }
// }
