package map;

import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Fetch map images from mapbox server.
 */
public class MapRequest {
    public/***/ static final String USER_NAME = "masel";
    public/***/ static final String TOKEN = "pk.eyJ1IjoibWFzZWwiLCJhIjoiY2o0ZTR2NWtrMHZudDJ3cDQzdXRwZ29zZCJ9.VrI0NDIYaP_5ZAXqnpaD1A";

    //eng
    public static final String FULL_STYLE_ID_ENG = "cjd9dx1oe9t712roe6qmxmfgr";
    public static final String LABEL_STYLE_ID_ENG = "cjd9dxg7i84ev2snt9rudqcgg";
    public static final String BOX_STYLE_ID_ENG = "cjd9dxq0w9tpb2ss0ysjtiwlr";

    //swe (same for now..)
    public static final String FULL_STYLE_ID_SWE = "cjd9dx1oe9t712roe6qmxmfgr";
    public static final String LABEL_STYLE_ID_SWE = "cjd9dxg7i84ev2snt9rudqcgg";
    public static final String BOX_STYLE_ID_SWE = "cjd9dxq0w9tpb2ss0ysjtiwlr";

    /**
     * Max width and height in pixels of request from server. */
    public static final int IMAGE_REQUEST_SIZE_LIMIT = 1280;

    /**
     * Width and height in pixels of of requested images from server,
     * and also of tiledImage-tiles returned by fetch.
     *  -Actual width/height will be double if x2 is true.
     *  -Last column width is 'width % this' (or 'this' if evens out).
     *  -Last row height is 'height % this' (or 'this' if evens out).
     * @inv this <= IMAGE_REQUEST_SIZE_LIMIT */
    public static final int REQUEST_SIZE = 512;

    /** Mid-point. */
    public/***/ double lon,lat;

    /** Dims in default pixel-density. */
    public/***/ int width,height;

    /** 0 to 20. */
    public/***/ int zoom;

    /** Double pixel-density. */
    public/***/ boolean x2;

    /** Tile-size of tiledImage returned by fetch(). Must be an even
     * number so that raw images can be requested correctly. */
    private int tileSize;

    /** Directory for the fetched fetched tiledImage. */
    private Path saveDir;

    /** Language of labels returned by fetch3(). */
    private Language lang;


    /**
     * Constructs the request from a defined map-image-view.
     * @param v Specifies the request; area, zoom, quality.
     * @param saveDir Directory where fetched images are saved.
     * @param lang Language of labels returned by fetch3().
     *
     * NOTE: All files in saveDir will be deleted!
     */
    public MapRequest(MapImageView v, Path saveDir, Language lang) {
        this.lon = v.lon;
        this.lat = v.lat;
        this.zoom = v.zoom;
        this.saveDir = saveDir;
        this.lang = lang;

        this.x2 = false;
        this.width = v.width;
        this.height = v.height;
        if (v.x2) {
            this.x2 = true;
            this.width = (int)Math.ceil(v.width / 2.0);
            this.height = (int)Math.ceil(v.height / 2.0);
        }
    }
    public MapRequest(MapImageView v, String saveDir, Language lang) {
        this(v, Paths.get(saveDir), lang);
    }

    /**
     * @return [mapImage, labelImage, boxImage]
     * @throws IOException if failed to fetch image (bad internet-conn?)
     */
    public TiledImage[] fetch3() throws IOException {
        String fullID = "";
        String labelID = "";
        String boxID = "";

        switch (this.lang) {
        case ENG:
            fullID = FULL_STYLE_ID_ENG;
            labelID = LABEL_STYLE_ID_ENG;
            boxID = BOX_STYLE_ID_ENG;
            break;
        case SWE:
            fullID = FULL_STYLE_ID_SWE;
            labelID = LABEL_STYLE_ID_SWE;
            boxID = BOX_STYLE_ID_SWE;
            break;
        }

        return new TiledImage[] {
            fetch(fullID, "full"),
            fetch(labelID, "label"),
            fetch(boxID, "box") };
    }

    /**
     * Fetches images without size limitation. Sub-images are fetched
     * from mapbox servers and concatenated.
     *
     * @param style Mapbox style ID.
     * @param subdir Directory-name within saveDir.
     * @return A map image defined by this object and the style.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     */
    public TiledImage fetch(String style, String subdir) throws IOException {
        MapRequest[][] reqs = this.split();
        Path dir = this.saveDir.resolve(subdir);

        int rows = reqs.length;
        int cols = reqs[0].length;
        TiledImage.Builder builder = new TiledImage.Builder(rows, cols, dir);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                BasicImage img = reqs[r][c].fetchRaw(style);
                builder.add(img);
            }
        }

        return builder.build();
    }

    /**
     * Fetches an image from mapbox servers.
     *
     * @param style Mapbox style ID.
     * @return A map image defined by this object and the style.
     * @pre Max with,height defined by IMAGE_REQUEST_SIZE_LIMIT.
     * @throws RuntimeError if dims too big.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     */
    public BasicImage fetchRaw(String style) throws IOException {
        if (this.width > IMAGE_REQUEST_SIZE_LIMIT ||
            this.height > IMAGE_REQUEST_SIZE_LIMIT) {
            throw new RuntimeException("Requested dims too big");
        }

        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
            .setAccessToken(TOKEN)
            .setUsername(USER_NAME)
            .setStyleId(style)
            .setLon(this.lon).setLat(this.lat)
            .setZoom(this.zoom)
            .setWidth(this.width).setHeight(this.height)
            .setRetina(this.x2)
            .build();

        String imageUrl = staticImage.getUrl().toString();
        imageUrl += "&attribution=false&logo=false";

        //System.out.println(imageUrl);

        try {
            BufferedImage img = ImageIO.read(new URL(imageUrl));
            return new BasicImage(img);
        }
        catch (IOException e) {
            throw new IOException("request: " + imageUrl, e);
        }
    }

    /**
     * Splits request into smaller blocks, each with a maximum side-
     * length of defined tile-size. If "enough map is left", the
     * maximum side length becomes the side length of the block.
     * That means, only blocks in last row or column can be smaller
     * than the maximum side length.
     *
     * @return A 2d-layout of requests.
     */
    public/***/ MapRequest[][] split() {
        boolean x2 = false;
        MapImageView[][] vs = new MapImageView(this.lon, this.lat, this.width, this.height, this.zoom, x2).split(REQUEST_SIZE);

        int rows = vs.length;
        int cols = vs[0].length;
        MapRequest[][] reqs = new MapRequest[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                MapImageView v = vs[r][c];
                reqs[r][c] = new MapRequest(vs[r][c], this.saveDir, this.lang);
                reqs[r][c].x2 = this.x2;
            }
        }

        return reqs;
    }

    @Override
    public String toString() {
        return String.format("lon(%s)_lat(%s)_w(%s)_h(%s)_z(%s)_2x(%s)_@(%s)",
                             lon, lat, width, height, zoom, x2, saveDir);
    }



    //----------------------------------------for testing. local reqs.

    static String imgsDir = "../imgs/uppercase";

    public static class ViewAndImgs {
        public MapImageView view;
        public TiledImage[] imgs;

        public ViewAndImgs(String dir, MapImageView v) {
            this.view = v;
            this.imgs = new TiledImage[] {
                TiledImage.load_(imgsDir + "/" + dir + "/full"),
                TiledImage.load_(imgsDir + "/" + dir + "/label"),
                TiledImage.load_(imgsDir + "/" + dir + "/box") };
        }
    }

    public static ViewAndImgs world() {
        return new ViewAndImgs("world", MapImageView.world());
    }
    public static ViewAndImgs europe() {
        return new ViewAndImgs("europe", MapImageView.europe());
    }
    public static ViewAndImgs sweden() {
        return new ViewAndImgs("sweden", MapImageView.sweden());
    }
    public static ViewAndImgs uppsala() {
        return new ViewAndImgs("uppsala", MapImageView.uppsala());
    }
    public static ViewAndImgs luthagen() {
        return new ViewAndImgs("luthagen", MapImageView.luthagen());
    }
    public static ViewAndImgs lidingo() {
        return new ViewAndImgs("lidingo", MapImageView.lidingo());
    }
    public static ViewAndImgs rudboda() {
        return new ViewAndImgs("rudboda", MapImageView.rudboda());
    }
    public static ViewAndImgs mefjard() {
        return new ViewAndImgs("mefjard", MapImageView.mefjard());
    }
    public static ViewAndImgs lonEdge() {
        return new ViewAndImgs("lonEdge", MapImageView.lonEdge());
    }
}
