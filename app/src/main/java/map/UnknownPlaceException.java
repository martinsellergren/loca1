package map;

/**
 * Exception throws when comming across bad place-data.
*/
public class UnknownPlaceException extends Exception {
    public UnknownPlaceException(String msg) { super(msg); }
}
