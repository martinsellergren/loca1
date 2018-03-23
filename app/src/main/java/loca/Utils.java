package loca;

import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

/**
 * Various usefull stuff.
 */
public class Utils {

    /**
     * Usage:
     * LOGGER.log( Level.FINE, "processing {0} entries", list.size() );
     * LOGGER.log( Level.FINER, "processing[{0}]: {1}", new Object[]{ i, list.get(i) } );
     * exception ex:
     * LOGGER.log( Level.SEVERE, ex.toString(), ex );
     *
     *	severe(String msg)
     *  warning(String msg)
     *	info(String msg)
     * 	config(String msg)
     * 	fine(String msg)
     *  finer(String msg)
     * 	finest(String msg) */
    public static final Logger LOGGER;

    /**
     * Level used when logging. Ignores messages with lower level.
     * Severe, warning, info, config, fine, finer, finest. */
    public static final Level LOG_LEVEL = Level.INFO;

    static {
         LOGGER = Logger.getGlobal();
         // ConsoleHandler hand = new ConsoleHandler();
         // SimpleFormatter form = new SimpleFormatter();
         // hand.setFormatter(form);
         // LOGGER.addHandler(hand);
         // hand.setLevel(LOG_LEVEL);
         LOGGER.setLevel(LOG_LEVEL);
    }
}
