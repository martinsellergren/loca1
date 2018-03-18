package map;

/**
 * Exception thrown when comming across bad place-data.
*/
public class UnknownPlaceException extends Exception {
    public UnknownPlaceException(String msg) { super(msg); }
}
