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
     * @return Color of pixel.
     */
    public Color getColor(int x, int y) {
        boolean hasAlpha = true;
        return new Color(img.getRGB(x, y), hasAlpha);
    }

    /**
     * Set color of pixel.
     */
    public void setColor(int x, int y, Color c) {
        img.setRGB(x, y, c.getRGB());
    }
   public void setColor(int[] xy, Color c) {
       setColor(xy[0], xy[1], c);
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
        int[] tl = Math2.toInt(Math2.rotate(box.getTopLeft(), rot));
        int[] br = Math2.toInt(Math2.rotate(box.getBottomRight(), rot));
        BasicImage elemImg = new BasicImage(br[0]-tl[0], br[1]-tl[1]);

        int[] bounds = Math2.toInt(box.getBounds());

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
                    elemImg.setColor(elemP[0], elemP[1], clr);
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
        BufferedImage croped = this.img.getSubimage(xmin, ymin, xmax-xmin, ymax-ymin);
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
     * Draws a box on the image.
     */
    public void drawBox(Box box) {
        float alpha = 1f;

        Color red = new Color(1, 0, 0, alpha);
        Color green = new Color(0, 1, 0, alpha);
        Color blue = new Color(0, 0, 1, alpha);
        Color yellow = new Color(1, 1, 0, alpha);
        Color orange = new Color(1, 165/255f, 0, alpha);
        Color white = new Color(1, 1, 1, alpha);
        Color cyan = new Color(0, 1, 1, alpha);
        Color pink = new Color(1, 192/255f, 203/255f, alpha);

        Graphics2D g = createGraphics();
        int[] tl = Math2.toInt(box.getTopLeft());
        int[] tr = Math2.toInt(box.getTopRight());
        int[] bl = Math2.toInt(box.getBottomLeft());
        int[] br = Math2.toInt(box.getBottomRight());
        g.setPaint(red);
        g.drawLine(tl[0], tl[1], tr[0], tr[1]);
        g.setPaint(green);
        g.drawLine(tr[0], tr[1], br[0], br[1]);
        g.setPaint(blue);
        g.drawLine(br[0], br[1], bl[0], bl[1]);
        g.setPaint(yellow);
        g.drawLine(bl[0], bl[1], tl[0], tl[1]);

        // int[] tm = Math2.toInt(box.getTopMid());
        // int[] m = Math2.toInt(box.getMid());
        // int[] bm = Math2.toInt(box.getBottomMid());
        // g.setPaint(orange);
        // g.drawLine(tm[0], tm[1], m[0], m[1]);
        // g.setPaint(white);
        // g.drawLine(m[0], m[1], bm[0], bm[1]);

        // int[] lm = Math2.toInt(box.getLeftMid());
        // int[] rm = Math2.toInt(box.getRightMid());
        // g.setPaint(cyan);
        // g.drawLine(lm[0], lm[1], rm[0], rm[1]);
    }

    public void drawBoxBounds(Box b) {
        drawPoints(new int[][] {
                Math2.toInt(b.getTopLeft()),
                Math2.toInt(b.getTopRight()),
                Math2.toInt(b.getBottomRight()),
                Math2.toInt(b.getBottomLeft())});
    }

    /**
     * Put colored dots on points.
     */
    public void drawPoints(int[][] cs) {
        float alpha = 1f;
        Color red = new Color(1, 0, 0, alpha);
        Color green = new Color(0, 1, 0, alpha);
        Color blue = new Color(0, 0, 1, alpha);
        Color yellow = new Color(1, 1, 0, alpha);

        Color[] cols = new Color[]{red, green, blue, yellow};
        int ci = 0;

        for (int[] c : cs) {
            this.setColor(c, cols[ci++ % 4]);
        }
    }

    /**
     * Draw a LabelLayout on the image.
     */
    public void drawLabelLayout(LabelLayout lay) {
        Graphics2D g = createGraphics();

        // for (Box b : lay.getBoxes())
        //     drawBox(b);

        int[] tl = Math2.toInt(lay.getBox(0,0).getTopLeft());
        int[] tr = Math2.toInt(lay.getBox(0,-1).getTopRight());
        int[] br = Math2.toInt(lay.getBox(-1,-1).getBottomRight());
        int[] bl = Math2.toInt(lay.getBox(-1,0).getBottomLeft());

        g.setPaint(Color.RED);
        g.drawLine(tl[0], tl[1], tr[0], tr[1]);
        g.drawLine(tr[0], tr[1], br[0], br[1]);
        g.drawLine(br[0], br[1], bl[0], bl[1]);
        g.drawLine(bl[0], bl[1], tl[0], tl[1]);
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

    /**
     * Load from file.
     */
    public static BasicImage load(String file) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new BasicImage(img);
    }
}
