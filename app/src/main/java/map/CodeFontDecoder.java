package map;

import java.io.IOException;
import java.io.FileReader;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

/**
 * Class of turning letter-code-images into unicode letters.
 *
 * 1) Code-image turned to integer = index in mappings-array.
 * 2) Mapping-array maps index to unicode-point.
 *
 * Code-image is a box-grid. Lay it out row by row and you'll
 * get a binary code where transparent block means 0, opaque 1.
 * Least significant bit first.
 */
public class CodeFontDecoder {

    /**
     * Mappings-array where index map to unicode-point. */
    private final String MAPPINGS_PATH = "codeFontMappings.json";
    private int[] mappings;

    /**
     * Layout of code-image. */
    private final int CODE_BOX_ROWS = 4;
    private final int CODE_BOX_COLS = 3;

    /**
     * If alpha[0,255] in code-image of block-mid >= this: binary 1.
     *                                               else: binary 0. */
    private final int ALPHA_THRESHOLD = 1;


    /**
     * Loads code-font-mappings-table used for converting decoded
     * number into unicode character.
     */
    public CodeFontDecoder() throws IOException {
        JsonArray json = new JsonParser().parse(new FileReader(MAPPINGS_PATH)).getAsJsonArray();

        this.mappings = new int[json.size()];
        for (int i = 0; i < mappings.length; i++)
            mappings[i] = json.get(i).getAsInt();
    }

    /**
     * @param codeImg Encoded-integer-image.
     * @return Decoded integer.
     */
    private int decodeInteger(BasicImage codeImg) {
        double[] bs = new double[]{0, 0, codeImg.getWidth(), codeImg.getHeight()};
        double[][] grid = Math2.split(bs, CODE_BOX_ROWS, CODE_BOX_COLS);
        String binary = "";

        for (double[] blockBs : grid) {
            int[] mid = Math2.toInt(Math2.getBoundsMid(blockBs));

            if (codeImg.getColor(mid).getAlpha() < ALPHA_THRESHOLD)
                binary += "0";
            else
                binary += "1";
        }
        return Integer.parseInt(binary, 2);
    }

    /**
     * @param codeImg Encoded-integer-image.
     * @return The character(s) mapped by decoded integer. Most likely
     * one character.
     */
    public String decode(BasicImage codeImg) {
        int index = decodeInteger(codeImg);
        int codePoint = this.mappings[index];
        return new String(Character.toChars(codePoint));
    }

}
