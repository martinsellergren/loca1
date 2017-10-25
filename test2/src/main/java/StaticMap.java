import com.mapbox.services.api.staticimage.v1.MapboxStaticImage.Builder;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.mapbox.services.Constants;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;

public class StaticMap {
    static boolean useRetina = true;
    static String token = "pk.eyJ1IjoibWFzZWwiLCJhIjoiY2o0ZTR2NWtrMHZudDJ3cDQzdXRwZ29zZCJ9.VrI0NDIYaP_5ZAXqnpaD1A";

    static BufferedImage getImage(String user, String style, double lon, double lat, int zoom, int width, int height, boolean attribution) {
        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
            .setAccessToken(token)
            .setUsername(user)
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
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return img;
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
        int width = 500;
        int height = 500;

        BufferedImage imgFull = getImage(Constants.MAPBOX_USER, Constants.MAPBOX_STYLE_STREETS, lon, lat, zoom, width, height, true);
        BufferedImage imgLabels = getImage("masel", "cj4e78lie24l12rqdsp7u54xz", lon, lat, zoom, width, height, false);

        saveImage(imgFull, "full.png");
        saveImage(imgLabels, "labels.png");

        System.out.println("done");
    }
}
