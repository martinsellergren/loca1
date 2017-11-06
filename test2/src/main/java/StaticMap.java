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

public class StaticMap {
    static boolean useRetina = false;
    static String uname = "masel";
    static String token = "pk.eyJ1IjoibWFzZWwiLCJhIjoiY2o0ZTR2NWtrMHZudDJ3cDQzdXRwZ29zZCJ9.VrI0NDIYaP_5ZAXqnpaD1A";
    static String fullStyleID = "cj962wpa3p3g22spbnw89cisy";
    static String labelsStyleID = "cj962xk828lks2svxh8s2ahed";
    static int labelAlphaThreshold = 100; //pixels alpha-value- over means show, under means hide.

    static BufferedImage[] getImages(double lon, double lat, int zoom, int width, int height) {
        boolean attribution = false;
        BufferedImage full = getImage(fullStyleID, lon, lat, zoom, width, height, attribution);
        BufferedImage labels = getImage(labelsStyleID, lon, lat, zoom, width, height, attribution);
        fixLabelImage(labels);
        return (new BufferedImage[] {full, labels});
    }

    static void fixLabelImage(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y), true);
                int alpha = c.getAlpha();
                if (alpha > labelAlphaThreshold) {
                    img.setRGB(x, y, Color.black.getRGB());
                }
                else {
                    img.setRGB(x, y, new Color(0, 0, 0, 0).getRGB());
                }
            }
        }
    }

    // static int getRed(int clr) { return (clr & 0x00ff0000) >> 16; }
    // static int getGreen(int clr) { return (clr & 0x00ff0000) >> 16; }
    // static int getBlue(int clr) { return (clr & 0x00ff0000) >> 16; }

    static BufferedImage getImage(String style, double lon, double lat, int zoom, int width, int height, boolean attribution) {
        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
            .setAccessToken(token)
            .setUsername(uname)
            .setStyleId(style)
            .setLon(lon).setLat(lat) // Image center position
            .setZoom(zoom)
            .setWidth(width).setHeight(height) // Image size
            .setRetina(useRetina) // Retina 2x image will be returned
            .build();

        String imageUrl = staticImage.getUrl().toString();
        if (!attribution) {
                imageUrl += "&attribution=false&logo=false";
            }
        //System.out.println(imageUrl);

        BufferedImage img = null;

        try {
            img = ImageIO.read(new URL(imageUrl));
            //displayImg(img);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return img;
    }

    static void displayImg(BufferedImage img) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }

    static void saveImage(BufferedImage img, String name) {
        try {
            ImageIO.write(img, "png", new File(name));
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public static void main(String[] args) {
        double lon = -73.98572;
        double lat = 40.74843;
        int zoom = 16;
        int width = 500;//1280;
        int height = 500;//1280;

        BufferedImage[] imgs = getImages(lon, lat, zoom, width, height);

        saveImage(imgs[0], "full.png");
        saveImage(imgs[1], "labels.png");

        System.out.println("done");
    }
}
