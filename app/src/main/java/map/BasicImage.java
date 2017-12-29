package map;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.*;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * A basic image with some useful behavior. A wrapper of BufferedImage.
 */
public class BasicImage {
    public/***/ BufferedImage img;

    public BasicImage(BufferedImage img) {
        this.img = img;
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
     * Returns an element in the image contained inside a box.
     * The box (and element in image) may be rotated, but returned
     * element is not.
     *
     * @param b Box describing element to be extracted.
     * @return A new image where non-rotated element fits perfectly,
     * i.e an un-rotated subsection of this image.
     */
    public BasicImage extractElement(Box b) {
        return null;
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

    /**
     * A pixel walk, i.e a walk from one position to another on
     * a straight line, returning the positions that are stepped on.
     */
    public class PixelWalk {
        public/***/ int x, y;
        public/***/ int endX, endY;

        /**
         * Diagonal decider. If angle is between 45+-DD go diagonal
         * up-left. Same principal for the other four diagonals.
         */
        private final double DD = 15;

        public PixelWalk(int startX, int startY, int endX, int endY) {
            x = startX;
            y = startY;
        }

        /**
         * @returns true if isn't there yet.
         */
        public boolean hasMore() {
            return (x != endX || x != endY);
        }

        /**
         * Returns next position. Also updates state.
         *
         * @return next position.
         */
        public int[] next() {
            double ang = Math2.angle(new int[]{endX-x, endY-y});

            if (ang >= -45 + DD && ang <= 45 - DD) //right
                return new int[]{x+1, y};
            if (ang >= 45 - DD && ang <= 45 + DD) //up-right
                return new int[]{x+1, y-1};
            if (ang >= 45 + DD && ang <= 135 - DD) //up
                return new int[]{x, y-1};
            if (ang >= 135 - DD && ang <= 135 + DD) // up-left
                return new int[]{x-1, y-1};
            if (ang >= 135 + DD && ang <= -135 - DD) //left
                return new int[]{x-1, y};
            if (ang >= -135 - DD && ang <= -135 + DD) //down-left
                return new int[]{x-1, y+1};
            if (ang >= -135 + DD && ang <= -45 - DD) //down
                return new int[]{x, y+1};
            if (ang >= -45 - DD && ang <= -45 + DD) //down-right
                return new int[]{x+1, y+1};

            throw new RuntimeException("Dead-end");
        }
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
