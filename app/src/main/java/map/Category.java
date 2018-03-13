package map;

import java.util.LinkedList;
import java.io.IOException;
import java.awt.Color;

/**
 * Categories of map-objects, mainly map-labels.
 */
public enum Category {
    BAY,
    LAKE,
    NATURE_RESERVE,
    SEA,
    STREET,
    OCEAN,

    WATERWAY,
    RIVER,
    STREAM,
    CANAL,
    DRAIN,
    DITCH,

    SHOP,
    CAFE,

    HIGHWAY,

    //PLACE-TAG
    //Administratively declared places
    COUNTRY,
    STATE,
    REGION,
    PROVINCE,
    DISTRICT,
    COUNTY,
    MUNICIPALITY,

    //Populated settlements, urban
    CITY,
    BOROUGH,
    SUBURB,
    QUARTER,
    NEIGHBOURHOOD,
    CITY_BLOCK,
    PLOT,

    //Populated settlements, urban and rural
    TOWN,
    VILLAGE,
    HAMLET,
    ISOLATED_DWELLING,
    FARM,
    ALLOTMENTS,

    //Other places
    CONTINENT,
    ARCHIPELAGO,
    ISLAND,
    ISLET,
    SQUARE,
    LOCALITY;







    /**
     * @param name Name of enum-value (ignores case).
     * @return Category, or NULL if none with that name.
     */
    public static Category find(String name) {
        return find_(Category.class, name);
    }
    public/***/ static <T extends Enum<T>> T find_(Class<T> c, String name) {
        for (T enumValue : c.getEnumConstants()) {
            if (enumValue.name().equalsIgnoreCase(name)) {
                return enumValue;
            }
        }
        return null;
    }

    /**
     * Converts the color of a label in a box-image to a category.
     *
     * @param lay Label-layout.
     * @param bimg Box-image containing lay.
     */
    public static Category decipher(LabelLayout lay, TiledImage bimg) throws IOException {
        Color c = getAvgColor(lay, bimg);
        return convert(c);
    }

    /**
     * @return Avg color of pixels in lay.
     */
    public/***/ static Color getAvgColor(LabelLayout lay, TiledImage bimg) throws IOException {
        int[] bs = Math2.getInsideBounds (Math2.toIntBounds(lay.getBounds()), bimg.getWidth(), bimg.getHeight());

        BasicImage sub = bimg.getSubImage(bs);
        lay = lay.addOffset(-bs[0], -bs[1]);
        LinkedList<int[]> ps = LabelLayoutIterator.getLabelPoints(lay, sub);
        return sub.getAverageColor(ps);
    }

    /**
     * Turn a color into a category.
     */
    public static Category convert(Color c) {
        int index = c.getRed();

        return BAY;
    }
}
