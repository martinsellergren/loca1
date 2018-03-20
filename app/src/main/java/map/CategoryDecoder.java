package map;

import java.io.IOException;
import java.io.FileReader;
import java.util.LinkedList;
import java.awt.Color;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Static class for converting between color and category.
 * Uses a json-table of Mapbox Vector-tile label-types where the
 * indexing determines the color of this label-type and the
 * corresponding category.
 */
public class CategoryDecoder {

    /**
     * Path to json-table. An array of label-types (source-layer,
     * and property-value defined by mapbox) and the corresponding
     * categories (defined in Category-enum). The labels in a box-image
     * are color-coded based on index in this array. */
    public/***/ static final String TABLE_PATH = "labelTypeTable.json";

    /**
     * Parsed json-table: a list if Categories where the index of
     * a category is encoded in the label-color in a box-image. */
    private static LinkedList<Category> colorCategoryMappings = null;

    /**
     * Creates the mappings by loading json-table from file and
     * parsing it. Call before all else.
     */
    public static void init() throws IOException {
        colorCategoryMappings = new LinkedList<Category>();
        JsonArray elems = new JsonParser().parse(new FileReader(TABLE_PATH)).getAsJsonArray();

        for (int i = 0; i < elems.size(); i++) {
            JsonObject elem = elems.get(i).getAsJsonObject();
            JsonArray values = elem.getAsJsonArray("values");

            for (int j = 0; j < values.size(); j++) {
                JsonArray type_cat = values.get(j).getAsJsonArray();
                String catStr = type_cat.get(1).getAsString();
                Category cat = Category.find(catStr);
                if (cat == null) {
                    throw new RuntimeException("Category in json-table but not in Category-enum: " + catStr);
                }

                colorCategoryMappings.add(cat);
            }
        }
    }

    /**
     * @param lay Label-layout.
     * @param boxImg Box-image containing label described by lay.
     * @return Category of label described by lay.
     */
    public static Category decode(LabelLayout lay, TiledImage boxImg) throws UnknownCategoryException, IOException {
        Color avg = lay.getAverageColor(boxImg);
        int index = colorToIndex(avg);
        if (index == colorCategoryMappings.size())
            throw new UnknownCategoryException();

        return colorCategoryMappings.get(index);
    }

    /**
     * Decodes color to index [0, len(mappings)].
     * Decoded index = len(mappings) means unknown label-type
     * (not defined in labelTypeTable.json so color fell to
     * default color-value).
     *
     * Conversion:
     * - r,g,b [0-255] scaled to d1,d2,d3 [0,4].
     * - d1 d2 d3 is a number n in base 5.
     * - n transformed to base 10 [0, 124].
     * - n scaled to [0, mappings.length] = index.
     *
     * @return index [0, len(mappings)]
     */
    public/***/ static int colorToIndex(Color c) throws IOException {
        if (colorCategoryMappings == null)
            throw new RuntimeException("Call init() !");

        double f = 256 / 5d;
        int d1 = (int)(c.getRed() / f);
        int d2 = (int)(c.getGreen() / f);
        int d3 = (int)(c.getBlue() / f);
        int n = d1*5*5 + d2*5 + d3;
        f = colorCategoryMappings.size() / 124f;
        int index = Math2.toInt(n * f);
        return index;
    }
}
