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
    private static final int RESULT_LIMIT = 10;

    /**
     * Queries for text and bounds, and looks for a result with a
     * category defined in Category-enum.
     * @trows UnknownPlaceException if no appropriate category.
     */
    public static JsonObject fetch(String text, double[] wsen) throws IOException, UnknownPlaceException {
        URL url = getURL(text, wsen);
        JsonArray places = getPlaces(url);
        JsonObject place = selectPlace(places);

        if (place == null)
            throw new UnknownPlaceException(url.toString());

        return place;
    }

    /**
     * @param text Query text.
     * @param wsen Query bounds.
     * @return URL for fetching json-data.
     */
    private static URL getURL(String text, double[] wsen) {
        String str = "https://nominatim.openstreetmap.org/search?format=json";
        str += "&accept-language=eng";
        str += "&addressdetails=0";
        str += "&limit=" + RESULT_LIMIT;
        str += "&extratags=1";
        str += "&namedetails=0";
        str += "&bounded=1";

        str += "&q=" + text.replace(' ', '+');
        str += String.format("&viewbox=%s,%s,%s,%s", wsen[0], wsen[1], wsen[2], wsen[3]);

        try {
            return new URL(str);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * @param URL A url that holds a json-array of places-objects.
     * @return This array.
     */
    private static JsonArray getPlaces(URL url) throws IOException {
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        return jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonArray();
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
        if (place.has("extratags")
            && place.getAsJsonObject("extratags").has("place")) {
            String placeStr = place.getAsJsonObject("extratags").get("place").getAsString();
            Category c = Category.find(placeStr);
            if (c != null) return c;
        }

        String type = place.get("type").getAsString();
        return Category.find(type);
    }

    /**
     * @return Json-object.
     */
    public static String toString(JsonObject place) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(place);
    }
}
