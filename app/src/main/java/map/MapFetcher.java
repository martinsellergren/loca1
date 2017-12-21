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
public class MapFetcher {
    public static final String USER_NAME = "masel";
    public static final String TOKEN = "pk.eyJ1IjoibWFzZWwiLCJhIjoiY2o0ZTR2NWtrMHZudDJ3cDQzdXRwZ29zZCJ9.VrI0NDIYaP_5ZAXqnpaD1A";
    public static final String FULL_STYLE_ID = "cj962wpa3p3g22spbnw89cisy";
    public static final String LABEL_STYLE_ID = "cj962xk828lks2svxh8s2ahed";
    public static final String BOX_STYLE_ID = "cj962xk828lks2svxh8s2ahed";
    public static final boolean USE_HIGH_QUALITY_IMAGE = false;
    public static final int IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT = 1280;

    /**
     * Creates map images with data from mapbox-servers. Uses default
     * values for styleIDs and mapbox-user-data etc.
     *
     * @param v Basic map image specification.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return Array of images where
     *   [0] - full map img
     *   [1] - labels img
     *   [2] - letter bounding box img
     */
    public static BasicImage[] fetchMapImages(MapView v) throws IOException {
        return null;
    }

    /**
     * Creates a map image from specified mapbox-style.
     * Image(s) are fetched from mapbox servers and concatenated by
     * need. If dims <= IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT there is no
     * concatenation: only one image is fetched from server and
     * returned.
     *
     * @param v Basic map image specification.
     * @param style Mapbox style ID.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return A map image.
     */
    public static BasicImage fetchMapImage(MapView v, String style) throws IOException {
        boolean highQ = false;
        int w = v.width;
        int h = v.height;
        if (v.highQuality) {
            w = (int) Math.ceil(w / 2.0);
            h = (int) Math.ceil(h / 2.0);
        }

        MapView[][] vs = new MapView(v.lon, v.lat, w, h, v.zoom, highQ, v.attribution).split(IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT);
        int rows = vs.length;
        int cols = vs[0].length;
        BasicImage[][] imgs = new MapImage[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                MapView p = vs[r][c];
                p = new MapView(p.lon, p.lat, p.width, p.height, p.zoom, v.highQuality, p.attribution);

                imgs[r][c] = fetchRawImage(p, style);
            }
        }

        return BasicImage.concatenateImages(imgs);
    }

    /**
     * Fetches an image from mapbox servers. Request details are held
     * in a map-image-view.
     *
     * This map-image-view is special in regard of pixel dimensions and
     * density. Dimensions are defined in low-pixel-density regardless
     * of the quality-property. The quality-property merely determines
     * if a high quality image is requested. By this, the returned
     * image will match the map-image-view.
     *
     * @param v Map image view details.
     * @param style Mapbox style ID.
     * @pre v's width,height <= IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return Static mapbox-image.
     */
    public static BasicImage fetchRawImage(MapView v, String style) throws IOException {
        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
            .setAccessToken(TOKEN)
            .setUsername(USER_NAME)
            .setStyleId(style)
            .setLon(v.lon).setLat(v.lat)
            .setZoom(v.zoom)
            .setWidth(v.width).setHeight(v.height)
            .setRetina(v.highQuality)
            .build();

        String imageUrl = staticImage.getUrl().toString();
        if (!v.attribution) {
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
}
