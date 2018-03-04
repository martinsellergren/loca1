package map;

/**
 * Categories of map-objects, mainly map-labels.
 */
public enum Category {
    CITY,
    COUNTRY,
    LAKE,
    OCEAN,
    ROAD;

    /**
     * @param name Name of enum-value (ignores case).
     * @return Category, or NULL if none with that name.
     */
    public static Category find(String name) {
        return find_(Category.class, name);
    }
    private static <T extends Enum<T>> T find_(Class<T> c, String name) {
        for (T enumValue : c.getEnumConstants()) {
            if (enumValue.name().equalsIgnoreCase(name)) {
                return enumValue;
            }
        }
        return null;
    }

}
