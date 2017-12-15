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
     * @param b Basic map image specification.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return Array of images where
     *   [0] - full map img
     *   [1] - labels img
     *   [2] - letter bounding box img
     */
    public static MapImage[] fetchMapImages(MapImageBasics b) throws IOException {
        return null;
    }

    /**
     * Creates a map image from specified mapbox-style.
     * Image(s) are fetched from mapbox servers and concatenated by
     * need. If dims <= IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT there is no
     * concatenation: only one image is fetched from server and
     * returned.
     *
     * @param b Basic map image specification.
     * @param style Mapbox style ID.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return A map image.
     */
    public static MapImage fetchMapImage(MapImageBasics b, String style) throws IOException {
        // MapImageBasics[][] bs = b.split(IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT);
        // int rows = bs.length;
        // int cols = bs[0].length;
        // MapImage[][] imgs = new MapImage[rows][cols];

        // for (int r = 0; r < rows; r++) {
        //     for (int c = 0; c < cols; c++) {
        //         BufferedImage bimg = fetchRawImage(bs[r][c], style);
        //         imgs[r][c] = new MapImage(bimg);
        //     }
        // }

        // return MapImage.concatenateImages(imgs);
        return null;
    }

    /**
     * Fetches an image from mapbox servers.
     *
     * @param b Basic map img specs.
     * @param style Mapbox style ID.
     * @pre b's width,height <= IMAGE_REQUEST_WIDTH_HEIGHT_LIMIT
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return Static mapbox-image.
     */
    public static BufferedImage fetchRawImage(MapImageBasics b, String style) throws IOException {
        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
            .setAccessToken(TOKEN)
            .setUsername(USER_NAME)
            .setStyleId(style)
            .setLon(b.lon).setLat(b.lat)
            .setZoom(b.zoom)
            .setWidth(b.width).setHeight(b.height)
            .setRetina(b.highQuality)
            .build();

        String imageUrl = staticImage.getUrl().toString();
        if (!b.attribution) {
                imageUrl += "&attribution=false&logo=false";
        }

        try {
            BufferedImage img = ImageIO.read(new URL(imageUrl));
            return img;
        }
        catch (IOException e) {
            throw new IOException("request: " + imageUrl, e);
        }
    }
}
