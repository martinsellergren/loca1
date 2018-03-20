package map;

import java.io.IOException;
import java.io.FileReader;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

/**
 * Static class for decoding images of encoded label-text.
 *
 * 1) Letter-code-image turned to integer = index in mappings-array.
 * 2) Mapping-array maps index to unicode-point.
 *
 * Code-image is a box-grid. Lay it out row by row and you'll
 * get a binary code where transparent block means 0, opaque 1.
 * Least significant bit first.
 */
public class LabelTextDecoder {

    /**
     * Mappings-array where index map to unicode-point. */
    public/***/ static final String MAPPINGS_PATH = "codeFontMappings.json";
    public/***/ static int[] mappings = null;

    /**
     * Layout of code-image. */
    public static final int CODE_BOX_ROWS = 4;
    public static final int CODE_BOX_COLS = 3;

    /**
     * If alpha[0,255] in code-image of block-mid < this: binary 0.
     *                                              else: binary 1. */
    public/***/ static final int ALPHA_THRESHOLD = 1;


    /**
     * Loads code-font-mappings-table used for converting decoded
     * number into unicode character. Call before all else.
     */
    public static void init() throws IOException {
        JsonArray json = new JsonParser().parse(new FileReader(MAPPINGS_PATH)).getAsJsonArray();

        mappings = new int[json.size()];
        for (int i = 0; i < mappings.length; i++)
            mappings[i] = json.get(i).getAsInt();
    }

    /**
     * @param b Specifies area of a letter.
     * @param codeImg Image containing letter specified by b.
     * @return The character mapped by decoded integer.
     */
    public static char decode(Box b, TiledImage codeImg) throws UnknownCharacterException, IOException {
        if (mappings == null)
            throw new RuntimeException("Call init() !");

        Box[] bs = b.split(CODE_BOX_ROWS, CODE_BOX_COLS);
        String binary = "";

        for (Box block : bs) {
            int[] mid = Math2.toInt(block.getMid());

            if (codeImg.getColor(mid).getAlpha() < ALPHA_THRESHOLD)
                binary += "0";
            else
                binary += "1";
        }

        binary = new StringBuilder(binary).reverse().toString();
        int index = Integer.parseInt(binary, 2);
        if (index >= mappings.length)
            throw new UnknownCharacterException("Bad code: " + index + ", caused by box: " + b);

        int codePoint = mappings[index];
        return new String(Character.toChars(codePoint)).charAt(0);
    }

    /**
     * @param lay Label-layout.
     * @param codeImg Code-image containing label described by lay.
     * @return Label-text forlabel described by lay. One char
     * per box in the layout.
     */
    public static String decode(LabelLayout lay, TiledImage codeImg) throws UnknownCharacterException, IOException {
        String txt = "";
        for (Box b : lay.getBoxesWithNewlines())
            if (b == null) txt += " ";
            else txt += decode(b, codeImg);
        return txt;
    }
}
