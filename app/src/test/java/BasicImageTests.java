// import org.junit.Test;
// import static org.junit.Assert.*;
// import map.*;
// import java.awt.image.BufferedImage;
// import java.awt.Color;
// import java.awt.Graphics2D;
// import java.util.Arrays;

// public class BasicImageTests {

//     @Test
//     public void extractElement() {
//         BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
//         Box box = new Box(new int[]{10, 40}, new int[]{50, 10}, 60);

//         colorImg(img, Color.WHITE);
//         Graphics2D g = img.createGraphics();
//         int[] tl = box.getTopLeft();
//         int[] tr = box.getTopRight();
//         int[] bl = box.getBottomLeft();
//         int[] br = box.getBottomRight();
//         g.setPaint(Color.BLUE);
//         g.drawLine(tl[0], tl[1], tr[0], tr[1]);

//         System.out.println(Arrays.toString(tr));
//         System.out.println(Arrays.toString(br));

//         g.setPaint(Color.PINK);
//         g.drawLine(tr[0], tr[1], br[0], br[1]);
//         // g.setPaint(Color.GREEN);
//         // g.drawLine(br[0], br[1], bl[0], bl[1]);
//         // g.setPaint(Color.BLACK);
//         // g.drawLine(bl[0], bl[1], tl[0], tl[1]);

//         // int[] tm = box.getTopMid();
//         // int[] bm = box.getBottomMid();
//         // g.drawLine(tm[0], tm[1], bm[0], bm[1]);

//         new BasicImage(img).save("test_element.png");
//     }

//     @Test
//     public void concatenateImages_2x2Layout_squares() {
//         int w = 10;
//         int h = 10;
//         int type = BufferedImage.TYPE_INT_ARGB;
//         BufferedImage img1 = new BufferedImage(w, h, type);
//         colorImg(img1, Color.RED);
//         BufferedImage img2 = new BufferedImage(w, h, type);
//         colorImg(img2, Color.GREEN);
//         BufferedImage img3 = new BufferedImage(w, h, type);
//         colorImg(img3, Color.BLUE);
//         BufferedImage img4 = new BufferedImage(w, h, type);
//         colorImg(img4, Color.YELLOW);

//         BasicImage[][] imgs = new BasicImage[][]{
//                 {new BasicImage(img1), new BasicImage(img2)},
//                 {new BasicImage(img3), new BasicImage(img4)}
//             };
//         BasicImage img = BasicImage.concatenateImages(imgs);
//         assertEquals(img.getWidth(), 20);
//         assertEquals(img.getHeight(), 20);

//         //img.save("test_concatenateImages_2x2Layout_squares.png");
//     }

//     public/***/ void colorImg(BufferedImage img, Color c) {
//         Graphics2D g = img.createGraphics();
//         g.setPaint(c);
//         g.fillRect(0, 0, img.getWidth(), img.getHeight());
//     }

// }
