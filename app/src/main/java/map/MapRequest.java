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
    public/***/ static final String USER_NAME = "masel";
    public/***/ static final String TOKEN = "pk.eyJ1IjoibWFzZWwiLCJhIjoiY2o0ZTR2NWtrMHZudDJ3cDQzdXRwZ29zZCJ9.VrI0NDIYaP_5ZAXqnpaD1A";
    public static final String FULL_STYLE_ID = "cj962wpa3p3g22spbnw89cisy";
    public static final String LABEL_STYLE_ID = "cj962xk828lks2svxh8s2ahed";
    public static final String BOX_STYLE_ID = "cj962xk828lks2svxh8s2ahed";
    public static final int IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT = 1280;
    public static final int DEFAULT_TILE_SIZE = 512;

    /** Mid-point. */
    public/***/ double lon,lat;

    /** Dims in default tile-size. */
    public/***/ int width,height;

    /** 0 to 20. */
    public/***/ int zoom;

    /** Dense pixels. */
    public/***/ boolean doubleQuality;

    /** Show attribution. */
    public/***/ boolean attribution;


    /**
     * Constructs the request from a defined map image view.
     * @param v Specifies the request; area, zoom, quality.
     * @param attrib Show attribution on fetched image.
     */
    public MapRequest(MapImageView v, boolean attrib) {
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
    }

    /**
     * No attribution-constructor.
     */
    public MapRequest(MapImageView v) {
        this(v, false);
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
        MapRequest[][] reqs = split(IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT);

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
    public/***/ MapRequest[][] split(int l) {
        boolean doubleQ = false;
        MapImageView[][] vs = new MapImageView(this.lon, this.lat, this.width, this.height, this.zoom, doubleQ).split(l);

        int rows = vs.length;
        int cols = vs[0].length;
        MapRequest[][] reqs = new MapRequest[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                MapImageView v = vs[r][c];
                boolean attrib = false;
                reqs[r][c] = new MapRequest(vs[r][c], attrib);
                reqs[r][c].doubleQuality = this.doubleQuality;
            }
        }

        return reqs;
    }

    @Override
    public String toString() {
        return String.format("lon(%s)_lat(%s)_w(%s)_h(%s)_z(%s)_2x(%s)",
                             lon, lat, width, height, zoom, doubleQuality);
    }
}
