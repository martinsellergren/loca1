package map;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.*;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;

/**
 * A basic image with some useful behavior. A wrapper of BufferedImage.
 */
public class BasicImage {
    public/***/ BufferedImage img;

    public BasicImage(BufferedImage img) {
        this.img = img;
    }

    /**
     * Constructor for empty image.
     */
    public BasicImage(int width, int height) {
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }


    /**
     * @return Img width, i.e no of pixels on width.
     */
    public int getWidth() {
        return img.getWidth();
    }

    /**
     * @return Img height, i.e no of pixels on height.
     */
    public int getHeight() {
        return img.getHeight();
    }

    /**
     * Set pixel.
     */
    public void setRGB(int x, int y, Color c) {
        img.setRGB(x, y, c.getRGB());
    }

    /**
     * Returns an element in the image contained inside a box.
     * The box (and element in image) may be rotated, but returned
     * element is not.
     *
     * @param b Box describing element to be extracted.
     * @return A new image where non-rotated element fits perfectly,
     * i.e an un-rotated subsection of this image.
     */
    public BasicImage extractElement(Box box) {
        double rot = -box.getRotation();
        int[] tl = Math2.rotate(box.getTopLeft(), rot);
        int[] br = Math2.rotate(box.getBottomRight(), rot);
        BasicImage elemImg = new BasicImage(br[0]-tl[0], br[1]-tl[1]);

        int[] bounds = box.getBounds();

        for (int y = bounds[1]; y <= bounds[3]; y++) {
            for (int x = bounds[0]; x <= bounds[2]; x++) {
                int[] imgP = new int[]{x, y};
                int[] elemP = Math2.minus(Math2.rotate(imgP, rot), tl);

                if (elemP[0] >= 0 &&
                    elemP[1] >= 0 &&
                    elemP[0] < elemImg.getWidth() &&
                    elemP[1] < elemImg.getHeight()) {
                    boolean hasAlpha = true;
                    Color clr = new Color(img.getRGB(imgP[0], imgP[1]), hasAlpha);
                    elemImg.setRGB(elemP[0], elemP[1], clr);
                }
            }
        }
        return elemImg;
    }

    /**
     * @return A deep copy of this image.
     */
    public BasicImage copy() {
        BufferedImage b = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return new BasicImage(b);
    }

    /**
     * @return New dimensioned image.
     * @pre Positions inside this image.
     * @pre mins < maxes.
     */
    public BasicImage crop(int xmin, int ymin, int xmax, int ymax) {
        BufferedImage croped = img.getSubimage(xmin, ymin, xmax-xmin, ymax-ymin);
        return new BasicImage(croped);
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
    public static BasicImage concatenateImages(BasicImage[][] imgs) {
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

        return new BasicImage(img);
    }


    //*******************************FOR TESTING

    /**
     * Color image in one color.
     */
    public void color(Color c) {
        Graphics2D g = img.createGraphics();
        g.setPaint(c);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Return graphics for drawing on image etc.
     */
    public Graphics2D createGraphics() {
        return this.img.createGraphics();
    }

    /**
     *
     */
    public void drawBox(Box box) {
        Graphics2D g = createGraphics();
        int[] tl = box.getTopLeft();
        int[] tr = box.getTopRight();
        int[] bl = box.getBottomLeft();
        int[] br = box.getBottomRight();
        g.setPaint(Color.BLUE);
        g.drawLine(tl[0], tl[1], tr[0], tr[1]);
        g.setPaint(Color.PINK);
        g.drawLine(tr[0], tr[1], br[0], br[1]);
        g.setPaint(Color.GREEN);
        g.drawLine(br[0], br[1], bl[0], bl[1]);
        g.setPaint(Color.BLACK);
        g.drawLine(bl[0], bl[1], tl[0], tl[1]);

        int[] tm = box.getTopMid();
        int[] m = box.getMid();
        int[] bm = box.getBottomMid();
        g.setPaint(Color.ORANGE);
        g.drawLine(tm[0], tm[1], m[0], m[1]);
        g.setPaint(Color.RED);
        g.drawLine(m[0], m[1], bm[0], bm[1]);

        int[] lm = box.getLeftMid();
        int[] rm = box.getRightMid();
        g.setPaint(Color.CYAN);
        g.drawLine(lm[0], lm[1], rm[0], rm[1]);
    }

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
