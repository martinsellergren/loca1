package map;

import java.awt.image.BufferedImage;
import java.awt.Color;

import javax.swing.*;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * An image with some loosly map-oriented behaviour.
 */
public class MapImage {
    private BufferedImage img;

    /**
     * Constructs a MapImage from a BufferedImage.
     */
    public MapImage(BufferedImage img) {
        this.img = img;
    }

    /**
     * Rotates image.
     * @param deg Rotation in degrees.
     */
    public void rotate(double deg) {
    }

    /**
     * Extracts a rectangular area from the image.
     *
     * @param b {@link Box} specifying area to extract.
     * @return New image - a non-rotated rectangle where the extracted area fits perfectly.
     */
    public MapImage extract(Box b) {
        return null;
    }

    /**
     * Detects text of label in the image specified by a label layout.
     * Uses OCR-methods.
     *
     * @param layout Specification for the label to be detected.
     * @return Text of label.
     */
    public String detectLabel(LabelLayout layout) {
        return null;
    }

    /**
     * @return A deep copy of this MapImage.
     */
    public MapImage copy() {
        return null;
    }

    public int getWidth() {
        return img.getWidth();
    }
    public int getHeight() {
        return img.getHeight();
    }

    /**
     * Concatenates multiple images into one.
     *
     * @param imgs 2d layout of concatenation-images.
     * @return One image.
     *
     * @pre All images in layout has same dimensions, with the
     * exceptions: imgs in last column may be thinner, and last row
     * may be shorter.
     */
    public static MapImage concatenateImages(MapImage[][] imgs) {
        int rows = imgs.length;
        int cols = imgs[0].length;
        int partWidth = imgs[0][0].getWidth();
        int partHeight = imgs[0][0].getHeight();

        int width = 0;
        int height = 0;
        for (int c = 0; c < cols; c++) width += imgs[0][c].getWidth();
        for (int r = 0; r < rows; r++) height += imgs[r][0].getHeight();

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int r = y / partHeight;
                int c = x / partWidth;
                BufferedImage part = imgs[r][c].img;
                int partX = x % partWidth;
                int partY = y % partHeight;
                boolean hasAlpha = true;
                Color clr = new Color(part.getRGB(partX, partY), hasAlpha);
                img.setRGB(x, y, clr.getRGB());
            }
        }

        return new MapImage(img);
    }


    /*******************************FOR TESTING


    /**
     * Draw img to screen.
     */
    public void display() {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(this.img)));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Save img.
     */
    public void save(String fileName) {
        try {
            ImageIO.write(img, "png", new File(fileName));
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
