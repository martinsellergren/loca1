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
    private static final String TABLE_PATH = "labelTypeTable.json";

    /**
     * Derived tabel where every row is a label-type, and the row-index
     * determines Color/Category. A label-type is fully defined by
     * source-layer and type.
     * Each row a string: "source-layer : type"
     * If no types:       "source-layer : -"
     */
    private static LinkedList<String> lookupTable = null;

    /**
     * Creates the lookupTable by loading json-table from file and
     * parse it. Call before all else.
     */
    public static void init() throws IOException {
        lookupTable = new LinkedList<String>();
        JsonArray elems = new JsonParser().parse(new FileReader(TABLE_PATH)).getAsJsonArray();

        for (int i = 0; i < elems.size(); i++) {
            JsonObject elem = elems.get(i).getAsJsonObject();
            String sourceLayer = elem.get("source-layer").getAsString();
            String property = elem.get("property").getAsString();

            if (property.equals("-")) {
                lookupTable.add(sourceLayer + " : -");
                continue;
            }

            JsonArray types = elem.getAsJsonArray("values");

            for (int j = 0; j < types.size(); j++) {
                String type = types.get(j).getAsString();
                lookupTable.add(sourceLayer + " : " + type);
            }
        }
    }

    /**
     * Converts color to index and returns table-entry.
     * Conversion:
     * - r,g,b [0-255] scaled to d1,d2,d3 [0,4].
     * - d1 d2 d3 is a number n is base 5.
     * - n transformed to base 10 [0, 124].
     * - n scaled to [0, max-table-index] = index.
     *
     * @return LabelType (table-entry) of color c.
     */
    private static String getLabelType(Color c) throws IOException {
        if (lookupTable == null)
            throw new RuntimeException("Call init() !");

        double f = 256 / 5d;
        int d1 = (int)(c.getRed() / f);
        int d2 = (int)(c.getGreen() / f);
        int d3 = (int)(c.getBlue() / f);
        int n = d1 + d2*5 + d3*5*5;
        f = (lookupTable.size()-1) / 124f;
        int index = Math2.toInt(n * f);
        if (index >= lookupTable.size())
            throw new IOException("Bad code:" + index);

        return lookupTable.get(index);
    }


    /**
     * @return Category derived from sourceLayer and type.
     */
    public static Category getCategory(String sourceLayer, String type) {
        return Category.BAY;
    }

    /**
     * @param lay Label-layout.
     * @param boxImg Box-image containing label described by lay.
     * @return Category of label described by lay.
     */
    public static Category decode(LabelLayout lay, TiledImage boxImg) throws IOException {
        Color avg = lay.getAverageColor(boxImg);
        String labelType = getLabelType(avg);
        String sourceLayer = labelType.split(" : ")[0];
        String type = labelType.split(" : ")[1];
        return getCategory(sourceLayer, type);
    }
}
