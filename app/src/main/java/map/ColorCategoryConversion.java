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
 * Class for converting between color and category.
 * Uses a json-table of Mapbox Vector-tile label-types where the
 * indexing determines the color of this label-type and the
 * corresponding category.
 */
public class ColorCategoryConversion {
    public/***/ String tablePath = "labelTypeTable.json";

    /**
     * Derived tabel where every row is a label-type, and the row-index
     * determines Color/Category. A label-type is fully defined by
     * source-layer and type.
     * Each row a string: "source-layer : type"
     * If no types:       "source-layer : -"
     */
    public/***/ LinkedList<String> lookupTable = new LinkedList<String>();

    /**
     * Creates the lookupTable by loading json-table from file and
     * parse it.
     */
    public ColorCategoryConversion() throws IOException {
        JsonArray elems = new JsonParser().parse(new FileReader(tablePath)).getAsJsonArray();

        for (int i = 0; i < elems.size(); i++) {
            JsonObject elem = elems.get(i).getAsJsonObject();
            String sourceLayer = elem.get("source-layer").getAsString();
            String property = elem.get("property").getAsString();

            if (property.equals("-")) {
                this.lookupTable.add(sourceLayer + " : -");
                continue;
            }

            JsonArray types = elem.getAsJsonArray("values");

            for (int j = 0; j < types.size(); j++) {
                String type = types.get(j).getAsString();
                this.lookupTable.add(sourceLayer + " : " + type);
            }
        }
    }

    /**
     * @return Category converted from color c.
     */
    public Category getCategory(Color c) {
        String labelType = getLabelType(c);
        String sourceLayer = labelType.split(" : ")[0];
        String type = labelType.split(" : ")[1];
        return getCategory(sourceLayer, type);
    }

    /**
     * @return LabelType (table-entry) of color c.
     */
    public/***/ String getLabelType(Color c) {
        int index = c.getRed();
        if (index >= this.lookupTable.size())
            index = this.lookupTable.size()-1;

        return this.lookupTable.get(index);
    }

    /**
     * @return Category derived from sourceLayer and type.
     */
    public/***/ Category getCategory(String sourceLayer, String type) {
        return Category.BAY;
    }

}
