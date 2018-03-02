package map;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

/**
 * Fetches data about a place from internet.
 */
public class PlaceQuery {
    private JsonObject data;

    /**
     * Queries for text and bounds, and looks for a result with a
     * category defined in Category-enum.
     */
    class NoRelevantResultException extends Exception {}
    public PlaceQuery(String text, double[] wnes) throws NoRelevantResultException, IOException {
        URL url = getURL(text, wnes);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        this.data = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
    }

    /**
     * @param text Query text.
     * @param wnes Query bounds.
     * @return URL for fetching json-data.
     */
    private URL getURL(String text, double[] wnes) {
        try {
            return new URL("https://nominatim.openstreetmap.org/search?q=135+pilkington+avenue,+birmingham&format=xml&polygon=1&addressdetails=1");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * @return Category of fetched place.
     */
    public Category getCategory() {
        //zipcode = rootobj.get("zip_code").getAsString();
        return Category.OCEAN;
    }
}
