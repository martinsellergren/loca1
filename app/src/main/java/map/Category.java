package map;

import java.util.LinkedList;
import java.io.IOException;
import java.awt.Color;

/**
 * Categories of map-objects (map-labels).
 */
public enum Category {
    COUNTRY,
    TERRITORY,

    WATERBODY,

    SUBDIVISION,

    CITY,
    TOWN,
    VILLAGE,
    DISTRICT,
    ISLAND,
    ISLET,
    ARCHIPELAGO,
    REGION,

    AIRFIELD,
    ALCOHOL_SHOP,
    AMUSEMENT_PARK,
    AQUARIUM,
    ART_GALLERY,
    ATTRACTION,
    BAKERY,
    BANK,
    BAR,
    BICYCLE_SERVICE,
    BICYCLE_SHARING_SERVICE,
    BUS_STOP,
    CAFE,
    CAMPSITE,
    CAR_SERVICE,
    CASTLE,
    CEMETERY,
    CINEMA,
    CLOTHING_STORE,
    COLLEGE,
    DENTAL_OFFICE,
    HEALTH_CLINIC,
    DOG_PARK,
    DRINKING_WATER_SERVICE,
    EMBASSY,
    ENTRANCE,
    FAST_FOOD_RESTAURANT,
    FERRY_SERVICE,
    FIRE_STATION,
    FILLING_STATION,
    GARDEN,
    GOLF_COURSE,
    GROCERY_STORE,
    HARBOR,
    HELIPORT,
    HOSPITAL,
    ICE_CREAM_PLACE,
    INFORMATION_SERVICE,
    LAUNDRY_SERVICE,
    LIBRARY,
    LODGING_SERVICE,
    PLACE,
    MONUMENT,
    MUSEUM,
    MUSIC_STORE,
    PARK,
    PHARMACY,
    PICNIC_SITE,
    PLACE_OF_WORSHIP,
    PLAYGROUND,
    POLICE_STATION,
    POST_OFFICE,
    PRISON,
    CHRISTIAN_CHURCH,
    JEWISH_CHURCH,
    MUSLIM_CHURCH,
    RESTAURANT,
    ROCKET_STATION,
    SCHOOL,
    SHOP,
    STADIUM,
    PUBLIC_SWIMMING_POOL,
    OFFICE,
    THEATRE,
    PUBLIC_TOILET,
    TOWN_HALL,
    VETERINARY_CLINIC,
    ZOO,

    MOTORWAY,
    HIGHWAY,
    STREET,
    PEDESTRIAN_STREET,
    UNFINISHED_ROAD,
    SMALL_ROAD,
    PATH,
    GOLF_ROAD,

    RIVER,
    CANAL,
    STREAM,

    AIRPORT,
    HEILPORT,

    RAILWAY_STATION,
    METRO_STATION,
    RAILWAY_STATION_ENTRANCE,

    MOUNTAIN,
    VOLCANO;


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
