package map;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.*;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A basic image with some useful behavior. A wrapper of BufferedImage.
 */
public class BasicImage {
    public/***/ BufferedImage img;

    public BasicImage(BufferedImage img) {
        this.img = img;
    }

    /**
     * Constructor for empty image. (r,g,b,a)=(0,0,0,0) for every pixel.
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
    public Color getColor(int[] p) {
        return getColor(p[0], p[1]);
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

    // /**
    //  * Returns an element in the image contained inside a box.
    //  * The box (and element in image) may be rotated, but returned
    //  * element is not.
    //  *
    //  * @param b Box describing element to be extracted.
    //  * @return A new image where non-rotated element fits perfectly,
    //  * i.e an un-rotated subsection of this image.
    //  */
    // public BasicImage extractElement(Box box) {
    //     int[] bs = box.getIntBounds();
    //     BasicImage rotated = getSubImage(bs);
    //     BasicImage straight = rotated.rotate(-box.getRotation());
    //     int w = Math2.toInt(box.getWidth());
    //     int h = Math2.toInt(box.getHeight());
    //     int x0 = (straight.getWidth() - w) / 2;
    //     int y0 = (straight.getHeight() - h) / 2;

    //     return straight.getSubImage(x0, y0, x0+w, y0+h);
    // }

    // /**
    //  * @return Rotated image, by angle degrees. Dims...
    //  */
    // public BasicImage rotate(double angle) {
    //     angle *= -1;
    //     float radianAngle = (float) Math.toRadians(angle) ;

    //     float sin = (float) Math.abs(Math.sin(radianAngle));
    //     float cos = (float) Math.abs(Math.cos(radianAngle));

    //     int w = getWidth() ;
    //     int h = getHeight();

    //     int neww = (int) Math.round(w * cos + h * sin);
    //     int newh = (int) Math.round(h * cos + w * sin);

    //     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    //     GraphicsDevice gd = ge.getDefaultScreenDevice();
    //     GraphicsConfiguration gc = gd.getDefaultConfiguration();

    //     BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
    //     Graphics2D g = result.createGraphics();

    //     //-----------------------MODIFIED--------------------------------------
    //     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
    //     g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC) ;
    //     g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY) ;

    //     AffineTransform at = AffineTransform.getTranslateInstance((neww-w)/2, (newh-h)/2);
    //     at.rotate(radianAngle, w/2, h/2);
    //     //---------------------------------------------------------------------

    //     g.drawRenderedImage(this.img, at);
    //     g.dispose();

    //     return new BasicImage(result);
    // }

    // /**
    //  * Rounds to int-point so that color-overwriting is avoided if
    //  * possible. Only overwrites more transparent pixels.
    //  *
    //  * If point is outside of image regardless of rounding, does
    //  * nothing. If point is overwriting a less transparant pixel
    //  * regardless of rounding, instead does nothing.
    //  *
    //  * @param p Point to be rounded and corresponding pixel colored.
    //  * @param clr Color of new pixel.
    //  */
    // public/***/ void cleverSetColor(double[] p, Color clr) {
    //     LinkedList<int[]> ps = new LinkedList<int[]>();
    //     ps.add(Math2.lCeil(p));
    //     ps.add(Math2.rCeil(p));
    //     ps.add(Math2.rFloor(p));
    //     ps.add(Math2.lFloor(p));

    //     int newAlpha = clr.getAlpha();
    //     int maxAlphaDiff = Integer.MIN_VALUE;
    //     int maxAlphaI = -1;

    //     for (int i = 0; i < ps.size(); i++) {
    //         int[] q = ps.get(i);
    //         if (isInside(q)) {
    //             int alphaDiff = newAlpha - getColor(q).getAlpha();
    //             if (alphaDiff > maxAlphaDiff) {
    //                 maxAlphaDiff = alphaDiff;
    //                 maxAlphaI = i;
    //             }
    //         }
    //     }

    //     if (maxAlphaDiff > 0) {
    //         int[] q = ps.get(maxAlphaI);
    //         setColor(q, clr);
    //     }
    // }

    /**
     * @return True if point p is inside image-bounds.
     */
    public boolean isInside(int[] p) {
        return
            p[0] >= 0 &&
            p[1] >= 0 &&
            p[0] < this.getWidth() &&
            p[1] < this.getHeight();
    }

    // /**
    //  * Creates a new image made up of all letters in the layout
    //  * on a straight line, in correct label-order.
    //  *
    //  * @param lay Label-layout.
    //  * @param padding Space between letters.
    //  * @return One-line letter-image with hight of tallest letter-img.
    //  */
    // public BasicImage extractLabel(LabelLayout lay, int padding) {
    //     LinkedList<BasicImage> ls = new LinkedList<BasicImage>();

    //     for (Box b : lay.getBoxesWithNewlines()) {
    //         if (b != null) {
    //             ls.add(this.extractElement(b));
    //         }
    //         else {
    //             int h = Math2.toInt(lay.getAverageBoxHeight());
    //             int w = Math2.toInt(h * 0.7f);
    //             BasicImage space = new BasicImage(w, h);
    //             ls.add(space);
    //         }
    //     }

    //     BasicImage img = concatenateImages(ls, padding);
    //     img = img.addAlpha(100);
    //     //img = img.addBackground(Color.WHITE);

    //     return img;
    // }

    // /**
    //  * Uses default padding = average box height.
    //  *
    //  * @param lay Label-layout.
    //  * @return One-line letter-image with hight of tallest letter-img.
    //  */
    // public BasicImage extractLabel(LabelLayout lay) {
    //     int bh = Math2.toInt(lay.getAverageBoxHeight());
    //     return extractLabel(lay, bh);
    // }

    /**
     * Add term to every pixel-alpha-value that isn't transparent.
     * @param add Value to add. (0 <= alpha-value <= 255).
     */
    public BasicImage addAlpha(int add) {
        BasicImage cpy = copy();

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Color c = getColor(x, y);
                //if (c.getAlpha() == 0) continue;

                int newA = c.getAlpha() + add;
                if (newA < 0) newA = 0;
                if (newA > 255) newA = 255;

                Color c_ = new Color(c.getRed(), c.getGreen(), c.getBlue(), newA);
                cpy.setColor(x, y, c_);
            }
        }
        return cpy;
    }

    /**
     * Adds a background color.
     */
    public BasicImage addBackground(Color c) {
        BasicImage back = new BasicImage(getWidth(), getHeight());
        back.color(c);

        Graphics2D g = back.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g.drawImage(this.img, 0, 0, null);
        g.dispose();

        return back;
    }

    /**
     * @return A deep copy of this image.
     */
    public BasicImage copy() {
        BasicImage dest = new BasicImage(getWidth(), getHeight());
        Graphics2D g = dest.createGraphics();
        g.drawImage(this.img, null, 0, 0);
        return dest;
    }

    /**
     * @return A BufferedImgage of this image.
     */
    public BufferedImage getBufferedImage() {
        return copy().img;
    }

    /**
     * @return New dimensioned image.
     * @pre Positions inside this image. If outside, snaped inside.
     * @pre mins < maxes.
     */
    public BasicImage getSubImage(int[] bs) {
        bs = Math2.getInsideBounds(bs, getWidth(), getHeight());

        BufferedImage croped = this.img.getSubimage(bs[0], bs[1], bs[2]-bs[0]+1, bs[3]-bs[1]+1);
        return new BasicImage(croped);
    }
    public BasicImage getSubImage(int xmin, int ymin, int xmax, int ymax) {
        return getSubImage(new int[]{xmin, ymin, xmax, ymax});
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
    public static BasicImage concatenateImages(LinkedList<LinkedList<BasicImage>> imgs) {
        return concatenateImages(to2DArray(imgs));
    }

    /**
     * @return 2d-array of images, from 2d-list of images.
     */
    public static BasicImage[][] to2DArray(LinkedList<LinkedList<BasicImage>> list) {
        if (list.size() == 0) return new BasicImage[0][0];

        BasicImage[][] arr = new BasicImage[list.size()][list.get(0).size()];

        for (int r = 0; r < list.size(); r++) {
            LinkedList<BasicImage> row = list.get(r);

            for (int c = 0; c < row.size(); c++) {
                arr[r][c] = row.get(c);
            }
        }
        return arr;
    }

    // /**
    //  * @param imgs One row of images with varying dims.
    //  * @param padding between images.
    //  * @return All imgs consecutively concatenated into
    //  * one image, with width=sum(widths) and height max(heights).
    //  */
    // public static BasicImage concatenateImages(LinkedList<BasicImage> imgs, int padding) {
    //     int width = 0;
    //     int height = -1;
    //     for (BasicImage img : imgs) {
    //         width += img.getWidth();
    //         if (height < img.getHeight()) height = img.getHeight();
    //     }
    //     width += padding * (imgs.size()+1);

    //     BasicImage res = new BasicImage(width, height);
    //     Graphics2D g = res.createGraphics();
    //     int x = padding;

    //     for (BasicImage img : imgs) {
    //         int y = (res.getHeight() - img.getHeight()) / 2;
    //         g.drawImage(img.getBufferedImage(), null, x, y);
    //         x += img.getWidth() + padding;
    //     }

    //     return res;
    // }

    /**
     * Save img. Overwrites if file exists.
     */
    public void save(String fileName) throws IOException {
        save(Paths.get(fileName));
    }
    public void save(Path p) throws IOException {
        p.toFile().mkdirs();
        ImageIO.write(this.img, "png", p.toFile());
    }

    /**
     * Load from file.
     */
    public static BasicImage load(String fileName) throws IOException {
        return load(Paths.get(fileName));
    }
    public static BasicImage load(Path p) throws IOException {
        BufferedImage img = ImageIO.read(p.toFile());
        return new BasicImage(img);
    }

    /**
     * Draws an overlay covering a label.
     *
     * @param l Label to be covered.
     * @param c Color used for the overlay.
     *
     * @pre l fits inside this image with some margin.
     */
    public void drawLabelOverlay(LabelLayout l, Color c) {
        Graphics2D g = createGraphics();
        g.setColor(c);
        g.setStroke(new BasicStroke((float) l.getTallestBoxHeight(),
                                    BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_MITER));

        for (int i = 0; i < l.getNoRows(); i++) {
            LinkedList<Box> row = l.getRow(i);
            int[] cursor = Math2.toInt(row.get(0).getLeftMid());

            for (Box b : row) {
                int[] lm = Math2.toInt(b.getLeftMid());
                int[] rm = Math2.toInt(b.getRightMid());
                g.drawLine(cursor[0], cursor[1], lm[0], lm[1]);
                g.drawLine(lm[0], lm[1], rm[0], rm[1]);
                cursor = Math2.toInt(b.getLeftMid());
            }
        }
    }

    /**
     * Return graphics for drawing on image etc.
     */
    public Graphics2D createGraphics() {
        return this.img.createGraphics();
    }

    /**
     * Finds average color of all pixels at specified points.
     * Ignores points outside image. Ignores alpha-values.
     *
     * @param ps Points.
     * @return Average color.
     */
    public Color getAverageColor(LinkedList<int[]> ps) {
        int r = 0;
        int g = 0;
        int b = 0;
        double count = 0;

        for (int[] p : ps) {
            if (isInside(p)) {
                Color c = getColor(p);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
                count++;
            }
        }

        r = Math2.toInt(r / count);
        g = Math2.toInt(g / count);
        b = Math2.toInt(b / count);
        return new Color(r, g, b);
    }

    //*******************************FOR TESTING

    /**
     * Save img. Overwrites if file exists.
     */
    public void save_(String fileName) {
        save_(Paths.get(fileName));
    }
    public void save_(Path p) {
        try {
            p.toFile().mkdirs();
            ImageIO.write(this.img, "png", p.toFile());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Load from file.
     */
    public static BasicImage load_(String fileName) {
        return load_(Paths.get(fileName));
    }
    public static BasicImage load_(Path p) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(p.toFile());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new BasicImage(img);
    }


    /**
     * Color image in one color.
     */
    public void color(Color c) {
        Graphics2D g = img.createGraphics();
        g.setPaint(c);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Draws Place on image:
     *  1) Draws place-overlays.
     *  2) Draws place-name in corner of each overlay.
     *  3) Draws place-category in corner.
     *  4) Draws a number indicating number of labels in corner.
     */
    public void drawPlace(Place p) {
        Color overlayC = Color.RED;
        Color col = Color.BLACK;

        for (Label lab : p.getLabels()) {
            LabelLayout lay = lab.getLayout();
            drawLabelLayout(lay);
            //drawLabelOverlay(lay, overlayC);
            drawLabelData(p.getName(), p.getCategory(), p.getNoLabels(), lay, col);
        }
    }

    /**
     * Draws label name, category and label-count on label.
     */
    public void drawLabelData(String text, Category cat, int count, LabelLayout lay, Color c) {
        drawLabelString(text + ", " + cat.toString() + ", x" + count, lay, c);
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
     * Draws a LabelLayout on the image.
     */
    public void drawLabelLayout(LabelLayout lay) {
        Graphics2D g = createGraphics();

        // for (Box b : lay.getBoxes())
        //     drawBox(b);

        int[] tl = Math2.toInt(lay.getBox(0,0).getTopLeft());
        int[] tr = Math2.toInt(lay.getBox(0,-1).getTopRight());
        int[] br = Math2.toInt(lay.getBox(-1,-1).getBottomRight());
        int[] bl = Math2.toInt(lay.getBox(-1,0).getBottomLeft());

        int test = Math2.randInt(-5,5);

        g.setPaint(Color.RED);
        g.drawLine(tl[0]+test, tl[1], tr[0], tr[1]);
        g.drawLine(tr[0], tr[1], br[0], br[1]);
        g.drawLine(br[0], br[1], bl[0], bl[1]);
        g.drawLine(bl[0], bl[1], tl[0]+test, tl[1]);
    }


    /**
     * Draws a string "inside" a layout.
     */
    public void drawLabelString(String text, LabelLayout lay, Color c) {
        Graphics2D g = createGraphics();
        g.setPaint(c);
        double[] bs = lay.getBounds();
        g.drawString(text, (float)bs[0], (float)(bs[1]+bs[3])/2);
    }

    /**
     * Draws a label (layout and text).
     */
    public void drawLabel(Label lab) {
        Color c = Color.RED;
        drawLabelLayout(lab.getLayout());
        drawLabelString(lab.getText(), lab.getLayout(), c);
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
}
