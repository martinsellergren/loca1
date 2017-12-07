package map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class StaticMap {
    static int labelAlphaThreshold = 100; //pixels alpha-value- over means show, under means hide.

    static BufferedImage[] getImages(double lon, double lat, int zoom, int width, int height) {
        boolean useRetina = true;
        boolean attribution = false;
        MapBasics mb = new MapBasics(lon, lat, width, height, zoom, useRetina, attribution);
        BufferedImage full = null;
        BufferedImage labels = null;

        try {
            full = MapFetcher.fetchRawImage(mb, MapFetcher.fullStyleID);
            labels = MapFetcher.fetchRawImage(mb, MapFetcher.labelStyleID);
        } catch (Exception e) { e.printStackTrace(); }
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
        // double lon = -73.98572;
        // double lat = 40.74843;
        // int zoom = 16;
        // int width = 500;//1280;
        // int height = 500;//1280;

        // BufferedImage[] imgs = getImages(lon, lat, zoom, width, height);

        // saveImage(imgs[0], "full.png");
        // saveImage(imgs[1], "labels.png");

        double lon = 0;
        double lat = 170;
        int width = 200;
        int height = 200;
        int zoom = 0;
        MapBasics mb = new MapBasics(lon, lat, width, height, zoom);

        try {
            BufferedImage img = MapFetcher.fetchRawImage(mb, MapFetcher.fullStyleID);
            saveImage(img, "full.png");
            System.out.println("done");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
