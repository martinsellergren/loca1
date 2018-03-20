package map;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

/**
 * A list of map-objects.
 *
 * @inv Map-objects in list are unique, meaning unique name+category.
 * @inv Every layout of every place is unique.
 */
public class MapObjects implements Iterable<MapObject> {

    private List<MapObject> mobs = new LinkedList<MapObject>();

    /**
     * Constructs by extracting labels from code-image and box-image,
     * and merging labels into list of map-objects.
     *
     * @param cimg Code-image.
     * @param bimg Box-image.
     * @param view Describing cimg and bimg.
     */
    public MapObjects(TiledImage cimg, TiledImage bimg, MapImageView view) throws IOException {
        LabelTextDecoder.init();
        CategoryDecoder.init();

        List<LabelLayout> lays = LabelLayoutIterator.getLayouts(bimg, view);
        List<Label> labs = getLabels(lays, cimg, bimg);
        this.mobs = mergeLabels(labs);
    }

    /**
     * Constructs from a existing list, where invariants hold.
     */
    private MapObjects(List<MapObject> mobs) {
        this.mobs = mobs;
    }

    /**
     * Turns label-layouts into labels. If fails to get label from
     * layout (junk etc), log and continue with next layout.
     *
     * @param lays Label-layouts, all unique.
     * @param cimg Code-image.
     * @param bimg Box-image.
     * @return Corresponding labels. All with unique layouts.
     */
    private List<Label> getLabels(List<LabelLayout> lays, TiledImage cimg, TiledImage bimg) throws IOException {
        List<Label> labs = new LinkedList<Label>();

        for (LabelLayout lay : lays) {
            try {
                labs.add(new Label(lay, cimg, bimg));
            }
            catch (UnknownCharacterException e) {
                System.out.println(e);
            }
            catch (UnknownCategoryException e) {
                System.out.println(e);
            }
            catch (Label.JunkException e) {
                System.out.println(e);
            }
        }

        return labs;
    }

    /**
     * Turn list of labels into list of map-objects. Labels describing
     * same feature (i.e same label-text and label-category) are
     * merged into one map-object.
     *
     * @param labs List of labels. Has unique layouts.
     * @return List of map-objects. All layouts unique.
     */
    private List<MapObject> mergeLabels(List<Label> labs) {
        List<MapObject> mobs = new LinkedList<MapObject>();

        for (Label lab : labs) {
            MapObject mob = findCorrespondingMapObject(lab, mobs);

            if (mob == null) mobs.add(new MapObject(lab));
            else mob.addLayout(lab.getLayout());
        }

        return mobs;
    }

    /**
     * @return Map-object where lab.text=mob.name and lab.category=
     * mob.category, or NULL if none in list.
     */
    private MapObject findCorrespondingMapObject(Label lab, List<MapObject> mobs) {
        for (MapObject mob : mobs) {
            if (mob.correspondsTo(lab)) return mob;
        }
        return null;
    }

    /**
     * @return A deep copy.
     */
    public MapObjects copy() {
        List<MapObject> mobsCpy = new LinkedList<MapObject>();
        for (MapObject mob : this.mobs) {
            mobsCpy.add(mob.copy());
        }
        return new MapObjects(mobsCpy);
    }

    @Override
    public Iterator<MapObject> iterator() {
        return this.mobs.iterator();
    }
}
