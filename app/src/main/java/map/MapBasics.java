package map;

/**
 * Basic data about a map.
 *
 * @author Martin Sellergren
 */
public class MapBasics {
    int x;
    int y;
    int width;
    int height;
    int zoom;

    /**
     * Creates a MapBasics object.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     * @param z zoom
     * @inv {@code w,h>0}
     * @inv {@code 0<z<16}
     */
    public MapBasics(int x, int y, int w, int h, int z) {
    }
}
