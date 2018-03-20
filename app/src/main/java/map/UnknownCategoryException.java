package map;

/**
 * Exception thrown when can't determine category from color-coded
 * box-label.
 *
 * General cause: Color of box-label is the default color given
 * when it's label-type isn't defined in labelTypeTable.json.
*/
public class UnknownCategoryException extends Exception {
    public UnknownCategoryException(String msg) { super(msg); }
    public UnknownCategoryException() {}
}
