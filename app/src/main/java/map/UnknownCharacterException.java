package map;

/**
 * Exception thrown when conversion from code-img to index
 * [0, mappings-max-index] fails (decoded index outside bounds).
 *
 * General cause: Clipped box in boxImage (Mapbox-bug).
 */
public class UnknownCharacterException extends Exception {
    public UnknownCharacterException(String msg) { super(msg); }
    public UnknownCharacterException() {}
}
