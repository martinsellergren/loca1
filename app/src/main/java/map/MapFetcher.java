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
    public static final String uname = "masel";
    public static final String token = "pk.eyJ1IjoibWFzZWwiLCJhIjoiY2o0ZTR2NWtrMHZudDJ3cDQzdXRwZ29zZCJ9.VrI0NDIYaP_5ZAXqnpaD1A";
    public static final String fullStyleID = "cj962wpa3p3g22spbnw89cisy";
    public static final String labelStyleID = "cj962xk828lks2svxh8s2ahed";
    public static final String boxStyleID = "cj962xk828lks2svxh8s2ahed";
    public static final boolean useRetina = false;

    /**
     * Creates map images with data from mapbox-servers. Uses default
     * values for styleIDs and mapbox-user-data etc.
     *
     * @param mb Basic map specification.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return Array of images where
     *   [0] - full map img
     *   [1] - labels img
     *   [2] - letter bounding box img
     */
    public static MapImage[] fetchMapImages(MapBasics mb) throws IOException {
        return null;
    }

    /**
     * Creates a map image from specified mapbox-style.
     * Image(s) are fetched from mapbox servers and concatenated by
     * need. If dims <= 1280 (mapbox limit) there is no concatenation:
     * only one image is fetched from server and returned.
     *
     * @param mb Basic map specification.
     * @param style Mapbox style ID.
     * @param useRetina True means a high-quality image.
     * @param attribution Adds mapbox-attribution to the map if True.
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return A map image.
     */
    public static MapImage fetchMapImage(MapBasics mb, String style, boolean useRetina, boolean attribution) throws IOException {
        int d = 1280;
        int nwidths = mb.width / d;
        int nheights = mb.height / d;
        int rows = nwidths;
        int cols = nheights;
        if (mb.width % d != 0) rows += 1;
        if (mb.height % d != 0) cols += 1;

        MapImage[][] imgLayout = new MapImage[rows][cols];

        for (int r = 0; r < imgLayout.length; r++) {
            for (int c = 0; c < imgLayout[r].length; c += d) {
                //imgLayout[r][c]

            }
        }
        return null;
    }

    /**
     * Fetches an image from mapbox servers.
     *
     * @param mb Basic map specs. width,height <= 1280 (mapbox limit)
     * @param style Mapbox style ID.
     * @param useRetina True means a high-quality image.
     * @param attribution Adds mapbox-attribution to the map if True.
     * @pre 0 < width,height <= 1280
     * @throws IOException if failed to fetch image (bad internet-conn?)
     * @return Static mapbox-image.
     */
    public static BufferedImage fetchRawImage(MapBasics mb, String style, boolean useRetina, boolean attribution) throws IOException {
        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
            .setAccessToken(token)
            .setUsername(uname)
            .setStyleId(style)
            .setLon(mb.x).setLat(mb.y)
            .setZoom(mb.zoom)
            .setWidth(mb.width).setHeight(mb.height)
            .setRetina(useRetina)
            .build();

        String imageUrl = staticImage.getUrl().toString();
        if (!attribution) {
                imageUrl += "&attribution=false&logo=false";
        }

        BufferedImage img = ImageIO.read(new URL(imageUrl));
        return img;
    }
}
