package map;

import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;
import java.io.IOException;

/**
 * Fetch map images from mapbox server.
 */
public class MapRequest {
    private static final String USER_NAME = "masel";
    private static final String TOKEN = "pk.eyJ1IjoibWFzZWwiLCJhIjoiY2o0ZTR2NWtrMHZudDJ3cDQzdXRwZ29zZCJ9.VrI0NDIYaP_5ZAXqnpaD1A";
    public static final String FULL_STYLE_ID = "cj962wpa3p3g22spbnw89cisy";
    public static final String LABEL_STYLE_ID = "cj962xk828lks2svxh8s2ahed";
    public static final String BOX_STYLE_ID = "cj962xk828lks2svxh8s2ahed";
    public static final int IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT = 1280;
    public static final int DEFAULT_TILE_SIZE = 512;

    /** Added to dimensions so that all originally cut labels fit.
     * Determined through experiments. */
    private static final int EXTENSION_TERM = 100;

    /** Mid-point. */
    private double lon,lat;

    /** Dims in default tile-size. */
    private int width,height;

    /** 0 to 22. */
    private int zoom;

    /** Dense pixels. */
    private boolean doubleQuality;

    /** Show attribution. */
    private boolean attribution;

    /** Extend dims before fetching so cut labels fit. */
    private boolean extend;


    /**
     * Constructs the request from scratch. Dims in default quality.
     *
     * @param lon Center longitude.
     * @param lat Center latitude.
     * @param w Pixel-width. w > 0.
     * @param h Pixel-height. h > 0.
     * @param z Zoom. 0 <= z <= 22.
     * @param doubleQ Request a high quality image.
     * @param attrib Show attribution on fetched image.
     * @param extend Fetch extended image where cut labels fit.
     */
    public MapRequest(double lon, double lat, int w, int h, int z, boolean doubleQ, boolean attrib, boolean extend) {
        this.lon = lon;
        this.lat = lat;
        this.width = w;
        this.height = h;
        this.zoom = z;
        this.doubleQuality = doubleQ;
        this.attribution = attrib;
        this.extend = extend;
    }

    /**
     * Constructs the request from a defined map image view.
     * @param v Specifies the request; area, zoom, quality.
     * @param attrib Show attribution on fetched image.
     * @param extend Fetch extended image where cut labels fit.
     */
    public MapRequest(MapImageView v, boolean attrib, boolean extend) {
        this.lon = v.lon;
        this.lat = v.lat;
        this.zoom = v.zoom;
        this.attribution = attrib;

        if (v.tileSize == DEFAULT_TILE_SIZE) {
            this.doubleQuality = false;
            this.width = v.width;
            this.height = v.height;
        }
        else if (v.tileSize == DEFAULT_TILE_SIZE*2) {
            this.doubleQuality = true;
            this.width = (int)Math.ceil(v.width / 2.0);
            this.height = (int)Math.ceil(v.height / 2.0);
        }
        else {
            throw new RuntimeException("Bad tile size");
        }

        if (extend) {
            this.width += EXTENSION_TERM;
            this.height += EXTENSION_TERM;
        }
    }

    /**
     * @return [mapImage, labelImage, boxImage]
     * @throws IOException if failed to fetch image (bad internet-conn?)
     */
    public BasicImage[] fetch3() throws IOException {
        return new BasicImage[] {
            fetch(FULL_STYLE_ID),
            fetch(LABEL_STYLE_ID),
            fetch(BOX_STYLE_ID) };
    }

    /**
     * Fetches images without size limitation. Sub-images are fetched
     * from mapbox servers and concatenated.
     *
     * @param style Mapbox style ID.
     * @return A map image defined by this object and the style.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     */
    public BasicImage fetch(String style) throws IOException {
        MapRequest req = this;
        if (this.extend) {
            req = getExtendedRequest();
        }
        MapRequest[][] reqs = req.split(IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT);

        int rows = reqs.length;
        int cols = reqs[0].length;
        BasicImage[][] imgs = new BasicImage[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                imgs[r][c] = reqs[r][c].fetchRaw(style);
            }
        }

        return BasicImage.concatenateImages(imgs);
    }

    /**
     * Fetches an image from mapbox servers.
     *
     * @param style Mapbox style ID.
     * @return A map image defined by this object and the style.
     * @pre Max with,height defined by IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT.
     * @throws RuntimeError if dims too big.
     * @throws RuntimeException if illegal bounds. (!)
     * @throws IOException if failed to fetch image (bad internet-conn?)
     */
    public BasicImage fetchRaw(String style) throws IOException {
        if (this.width > IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT ||
            this.height > IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT) {
            throw new RuntimeException("Requested dims too big");
        }

        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
            .setAccessToken(TOKEN)
            .setUsername(USER_NAME)
            .setStyleId(style)
            .setLon(this.lon).setLat(this.lat)
            .setZoom(this.zoom)
            .setWidth(this.width).setHeight(this.height)
            .setRetina(this.doubleQuality)
            .build();

        String imageUrl = staticImage.getUrl().toString();
        if (!this.attribution) {
                imageUrl += "&attribution=false&logo=false";
        }

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
     * length of a certain length. If "enough map is left", the
     * maximum side length becomes the side length of the block.
     * That means, only blocks in last row or column can be smaller
     * than the maximum side length.
     *
     * @param l Maximum side length.
     * @return A 2d-layout of requests.
     */
    private MapRequest[][] split(int l) {
        int rows = this.height / l;
        int cols = this.width / l;
        if (this.height % l != 0) rows += 1;
        if (this.width % l != 0) cols += 1;

        boolean highQ = false;
        MapImageView view = new MapImageView(this.lon, this.lat, this.width, this.height, this.zoom, highQ);
        MapRequest[][] reqs = new MapRequest[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x0 = c * l;
                int y0 = r * l;
                int x1 = Math.min(x0 + l, this.width);
                int y1 = Math.min(y0 + l, this.height);
                double xMid = (x0 + x1) / 2d;
                double yMid = (y0 + y1) / 2d;
                double[] latlon = view.getGeoCoordinates(xMid, yMid);

                boolean attrib = false;
                boolean ext = false;
                reqs[r][c] = new MapRequest(latlon[0], latlon[1], x1-x0, y1-y0, this.zoom, this.doubleQuality, attrib, ext);
            }
        }

        return reqs;
    }

    /**
     * @return A new request where dims are extended where
     * originally cut labels fit.
     */
    private MapRequest getExtendedRequest() {
        //check not outside world
        int w = this.width + EXTENSION_TERM;
        int h = this.height + EXTENSION_TERM;
        boolean extend = false;
        return new MapRequest(this.lon, this.lat, w, h, this.zoom, this.doubleQuality, this.attribution, extend);
    }
}
