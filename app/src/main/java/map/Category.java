package map;

/**
 * Categories of map-objects, mainly map-labels.
 */
public enum Category {
    BAY,
    LAKE,
    NATURE_RESERVE,
    SEA,
    STREET,
    OCEAN,

    WATERWAY,
    RIVER,
    STREAM,
    CANAL,
    DRAIN,
    DITCH,

    SHOP,
    CAFE,

    HIGHWAY,

    //PLACE-TAG
    //Administratively declared places
    COUNTRY,
    STATE,
    REGION,
    PROVINCE,
    DISTRICT,
    COUNTY,
    MUNICIPALITY,

    //Populated settlements, urban
    CITY,
    BOROUGH,
    SUBURB,
    QUARTER,
    NEIGHBOURHOOD,
    CITY_BLOCK,
    PLOT,

    //Populated settlements, urban and rural
    TOWN,
    VILLAGE,
    HAMLET,
    ISOLATED_DWELLING,
    FARM,
    ALLOTMENTS,

    //Other places
    CONTINENT,
    ARCHIPELAGO,
    ISLAND,
    ISLET,
    SQUARE,
    LOCALITY;







    /**
     * @param name Name of enum-value (ignores case).
     * @return Category, or NULL if none with that name.
     */
    public static Category find(String name) {
        return find_(Category.class, name);
    }
    public/***/ static <T extends Enum<T>> T find_(Class<T> c, String name) {
        for (T enumValue : c.getEnumConstants()) {
            if (enumValue.name().equalsIgnoreCase(name)) {
                return enumValue;
            }
        }
        return null;
    }

}
