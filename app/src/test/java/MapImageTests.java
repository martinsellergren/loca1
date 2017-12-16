// import org.junit.Test;
// import static org.junit.Assert.*;
// import map.MapImage;
// import java.awt.image.BufferedImage;
// import java.awt.Color;
// import java.awt.Graphics2D;

// public class MapImageTests {

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

//         MapImage[][] imgs = new MapImage[][]{
//                 {new MapImage(img1), new MapImage(img2)},
//                 {new MapImage(img3), new MapImage(img4)}
//             };
//         MapImage img = MapImage.concatenateImages(imgs);
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
