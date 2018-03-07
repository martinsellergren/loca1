package map;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Arrays;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Fetches data about a place from internet.
 */
public class PlaceQuery {

    /**
     * Exception thrown when query result isn't valid. */
    public static class UnknownPlaceException extends Exception {
        private UnknownPlaceException(String msg) { super(msg); }
    }

    /**
     * Max number of returned place candidates. */
    private static final int RESULT_LIMIT = 4;

    /*
     * True: Search only area around label.
     * False: Preder area around label but might go outside. */
    private static final boolean BOUNDED_QUERY = false;

    /**
     * Queries for text and bounds, and looks for a result with a
     * category defined in Category-enum.
     * @trows UnknownPlaceException if no appropriate category.
     */
    public static JsonObject fetch(String text, double[] wsen) throws IOException, UnknownPlaceException {
        URL[] urls = getURLs(text, wsen);
        JsonArray places = getPlaces(urls);
        JsonObject place = selectPlace(places);

        if (place == null)
            throw new UnknownPlaceException(Arrays.toString(urls));

        return place;
    }

    /**
     * @param text Query text.
     * @param wsen Query bounds.
     * @return Array of URLs for fetching json-data. Usually length=1.
     */
    private static URL[] getURLs(String text, double[] wsen) {
        String str = "https://nominatim.openstreetmap.org/search?format=json";
        str += "&accept-language=eng";
        str += "&addressdetails=0";
        str += "&limit=" + RESULT_LIMIT;
        str += "&extratags=1";
        str += "&namedetails=0";
        if (BOUNDED_QUERY) str += "&bounded=1";
        else str += "&bounded=0";

        double w = wsen[0];
        double s = wsen[1];
        double e = wsen[2];
        double n = wsen[3];
        str += "&q=" + text.replace(' ', '+');

        String str0 = str + String.format("&viewbox=%s,%s,%s,%s", w, s, e, n);

        try {
            if (w < -180 || e < -180) {
                String str1 = str + String.format("&viewbox=%s,%s,%s,%s", w+360, s, e+360, n);
                return new URL[]{ new URL(str0), new URL(str1) };
            }
            else if (w > 180 || e > 180) {
                String str1 = str + String.format("&viewbox=%s,%s,%s,%s", w-360, s, e-360, n);
                return new URL[]{ new URL(str0), new URL(str1) };
            }
            else {
                return new URL[]{ new URL(str0) };
            }
        }
        catch (MalformedURLException exc) {
            exc.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * @param URL An array of urls that each holds a json-array of
     * places-objects, or an empty json-array.
     * @return First non-empty json-array from the url-array,
     * or an empty JsonArray of none.
     */
    private static JsonArray getPlaces(URL[] urls) throws IOException {
        for (URL url : urls) {
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonArray arr = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonArray();

            if (arr.size() > 0) return arr;
        }

        return new JsonArray();
    }

    /**
     * Selects and returns first place in array that has a valid
     * category (i.e a category listed in Category-enum).
     *
     * @param arr Array of places, sorted after search-result-relevance.
     * @return First place with valid category, or NULL if none.
     */
    private static JsonObject selectPlace(JsonArray arr) throws UnknownPlaceException {
        for (int i = 0; i < arr.size(); i++) {
            JsonObject place = arr.get(i).getAsJsonObject();
            if (getCategory(place) != null) return place;
        }

        return null;
    }

    /**
     * @return Category of place - searches in tags, or NULL if none
     * appropriate.
     */
    public static Category getCategory(JsonObject place) {
        //place-tag
        if (place.has("extratags")
            && place.getAsJsonObject("extratags").has("place")) {
            String placeStr = place.getAsJsonObject("extratags").get("place").getAsString();
            Category c = Category.find(placeStr);
            if (c != null) return c;
        }
        if (place.has("extratags")
            && place.getAsJsonObject("extratags").has("description")) {
            String placeStr = place.getAsJsonObject("extratags").get("description").getAsString();
            Category c = Category.find(placeStr);
            if (c != null) return c;
        }

        //type-tag
        String type = place.get("type").getAsString();
        Category c = Category.find(type);
        if (c != null) return c;

        //class-tag
        String cls = place.get("class").getAsString();
        return Category.find(cls);
    }

    /**
     * @return Json-object.
     */
    public static String toString(JsonObject place) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(place);
    }
}
