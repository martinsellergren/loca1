package map;

import com.mapbox.services.api.staticimage.v1.MapboxStaticImage.Builder;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.mapbox.services.Constants;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;

/**
 * Fetch map images from mapbox server.
 */
public class MapFetcher {
    static String uname = "masel";
    static String token = "pk.eyJ1IjoibWFzZWwiLCJhIjoiY2o0ZTR2NWtrMHZudDJ3cDQzdXRwZ29zZCJ9.VrI0NDIYaP_5ZAXqnpaD1A";
    static String fullStyleID = "cj962wpa3p3g22spbnw89cisy";
    static String labelsStyleID = "cj962xk828lks2svxh8s2ahed";
    static String boxesStyleID = "cj962xk828lks2svxh8s2ahed";
    static boolean useRetina = false;

    /**
     * Creates map images with data from mapbox-servers.
     *
     * @param mb Basic map specification.
     * @return Array of images where
     *   [0] - full map img
     *   [1] - labels img
     *   [2] - letter bounding box img
     */
    public static MapImage[] fetchMapImages(MapBasics mb) {
        return null;
    }

    /**
     * Creates a map image from specified mapbox-style.
     * Data is collected from mapbox-servers.
     *
     * @param mb Basic map specification.
     * @param style Mapbox style ID.
     * @param useRetina True means a high-quality image.
     * @return A map image.
     */
    public static MapImage fetchMapImage(MapBasics mb, String style, boolean useRetina) {
        return null;
    }
}
