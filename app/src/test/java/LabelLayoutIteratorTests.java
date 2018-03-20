import org.junit.*;
import static org.junit.Assert.*;
import map.*;
import java.util.LinkedList;
import java.util.Arrays;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.IOException;
import java.io.File;

public class LabelLayoutIteratorTests {
    MapRequest.ViewAndImgs vis = MapRequest.lidingo();

    // TiledImage img = TiledImage.load_("../imgs/lidingo/box");
    // int[] area = new int[]{0, 0, img.getWidth(), img.getHeight()};

    // @Test
    // public void constructor() {
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img, area);
    //     iter.toImg().save("test_LabelLayoutIterator_constructor.png");
    // }

    // @Test
    // public void isBoxPoint() {
    //     //BasicImage img = BasicImage.load("../test_box_small.png");
    //     int[] bp = new int[]{2136, 1156};
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img);
    // }

    // @Test
    // public void expandToBoxPoints() {
    //     //BasicImage img = BasicImage.load("../test_box.png");
    //     int[] bp = new int[]{858, 261};
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img);
    //     img = iter.toImg();
    //     LinkedList<int[]> bps = iter.expandToBoxPoints(bp);
    //     //LabelLayoutIterator.toImg(bps).save("test_LabelLayoutIterator_expandToBoxPoints.png");
    // }


    // @Test
    // public void expandToBox() {
    //     //BasicImage img = BasicImage.load("../test_box.png");
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img);

    //     int[] bp = new int[]{583, 413};
    //     Box box = iter.expandToBox(bp);
    //     //System.out.println(box);
    //     img.drawBox(box);
    //     img.save("test_LabelLayoutIterator_expandToBox.png");
    // }

    // @Test
    // public void expandToBox_all() {
    //     //BasicImage img = BasicImage.load("../test_box_small.png");
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img);

    //     for (int y = 0; y < img.getHeight()-1; y++) {
    //         for (int x = 0; x < img.getWidth()-1; x++) {
    //             try {
    //                 if (iter.isBoxPoint(x, y)) {
    //                     Box bx = iter.expandToBox(new int[]{x,y});
    //                     if (bx != null)
    //                         img.drawBox(bx);

    //                     iter.expandAndRemove(new int[]{x,y});
    //                 }
    //             }
    //             catch (Exception e) {
    //                 System.out.println(x + ", " + y);
    //                 throw e;
    //             }
    //         }
    //     }
    //     img.save("test_LabelLayoutIterator_expandToBox_all.png");
    // }

    @Test
    public void findLabelLayouts() throws IOException {
        BasicImage fimg = vis.imgs[0].getOneImage();
        LinkedList<LabelLayout> lays = LabelLayoutIterator.getLayouts(vis.imgs[2], vis.view);

        for (LabelLayout lay : lays) {
            try {
                fimg.drawLabelLayout(lay);
            }
            catch (Exception e) {
                System.out.println(lay);
                throw e;
            }
        }

        fimg.save("test_LabelLayoutIterator_findLabelLayouts.png");
    }

    // @Test
    // public void expandToLabelLayout() {
    //     BasicImage img_ = img.getOneImageWithGrid();
    //     LabelLayoutIterator iter = new LabelLayoutIterator(img_);
    //     LabelLayout l = iter.expandToLabelLayout(new int[]{4950,2971});
    //     img_.drawLabelLayout(l);
    //     img_.save_("test_LabelLayoutIterator_expandToLabelLayout.png");
    // }
}
