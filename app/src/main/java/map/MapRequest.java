package map;

import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Fetch map images from mapbox server.
 */
public class MapRequest {
    public/***/ static final String USER_NAME = "masel";
    public/***/ static final String TOKEN = "sk.eyJ1IjoibWFzZWwiLCJhIjoiY2pkM2YwYXlvMncyZTMzczYzbXJ1Mzd3ayJ9.OBMENh5mWq6iwPZut8s0iw";
    public static final String FULL_STYLE_ID = "cjd9dx1oe9t712roe6qmxmfgr";
    public static final String LABEL_STYLE_ID = "cjd9dxg7i84ev2snt9rudqcgg";
    public static final String BOX_STYLE_ID = "cjd9dxq0w9tpb2ss0ysjtiwlr";
    public static final int IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT = 1280;

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


    /**
     * Constructs the request from a defined map-image-view.
     * @param v Specifies the request; area, zoom, quality.
     * @param ts Tile-size of resulting tiledImage. Is even.
     * @param saveDir Directory where fetched images are saved.
     */
    public MapRequest(MapImageView v, int ts, Path saveDir) {
        this.lon = v.lon;
        this.lat = v.lat;
        this.zoom = v.zoom;
        this.tileSize = ts;
        if (ts % 2 != 0) throw new RuntimeException("Bad tile size");
        this.saveDir = saveDir;

        this.x2 = false;
        this.width = v.width;
        this.height = v.height;
        if (v.x2) {
            this.x2 = true;
            this.width = (int)Math.ceil(v.width / 2.0);
            this.height = (int)Math.ceil(v.height / 2.0);
        }
    }

    /**
     * @return [mapImage, labelImage, boxImage]
     * @throws IOException if failed to fetch image (bad internet-conn?)
     */
    public TiledImage[] fetch3() throws IOException {
        return new TiledImage[] {
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
    public TiledImage fetch(String style) throws IOException {
        MapRequest[][] reqs = this.split();

        int rows = reqs.length;
        int cols = reqs[0].length;
        TiledImage.Builder builder = new TiledImage.Builder(rows, cols, this.saveDir);

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
     * @pre Max with,height defined by IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT.
     * @throws RuntimeError if dims too big.
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
        int l = this.tileSize;
        if (this.x2)
            l /= 2;

        boolean x2 = false;
        MapImageView[][] vs = new MapImageView(this.lon, this.lat, this.width, this.height, this.zoom, x2).split(l);

        int rows = vs.length;
        int cols = vs[0].length;
        MapRequest[][] reqs = new MapRequest[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                MapImageView v = vs[r][c];
                reqs[r][c] = new MapRequest(vs[r][c], this.tileSize, this.saveDir);
                reqs[r][c].x2 = this.x2;
            }
        }

        return reqs;
    }

    @Override
    public String toString() {
        return String.format("lon(%s)_lat(%s)_w(%s)_h(%s)_z(%s)_2x(%s)",
                             lon, lat, width, height, zoom, x2);
    }
}
