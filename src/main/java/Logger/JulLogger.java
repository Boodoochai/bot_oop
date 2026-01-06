package Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements ILogger interface using Java Util Logging (JUL).
 */
class JulLogger implements ILogger {

    private final Logger logger;

    JulLogger(Logger logger) {
        this.logger = logger;
    }

    // ---------- TRACE / DEBUG ----------

    @Override
    public void trace(String message) {
        debug(message);
    }

    @Override
    public void trace(String message, Object... args) {
        debug(message, args);
    }

    @Override
    public void debug(String message) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(message);
        }
    }

    @Override
    public void debug(String message, Object... args) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(
                    MessageFormatter.format(message, args)
            );
        }
    }

    // ---------- INFO ----------

    @Override
    public void info(String message) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(message);
        }
    }

    @Override
    public void info(String message, Object... args) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(
                    MessageFormatter.format(message, args)
            );
        }
    }

    // ---------- WARNING ----------

    @Override
    public void warn(String message) {
        if (logger.isLoggable(Level.WARNING)) {
            logger.warning(message);
        }
    }

    @Override
    public void warn(String message, Object... args) {
        if (logger.isLoggable(Level.WARNING)) {
            logger.warning(
                    MessageFormatter.format(message, args)
            );
        }
    }

    // ---------- ERROR ----------

    @Override
    public void error(String message) {
        if (logger.isLoggable(Level.SEVERE)) {
            logger.severe(message);
        }
    }

    @Override
    public void error(String message, Throwable t) {
        if (logger.isLoggable(Level.SEVERE)) {
            logger.log(Level.SEVERE, message, t);
        }
    }

    @Override
    public void error(String message, Throwable t, Object... args) {
        if (logger.isLoggable(Level.SEVERE)) {
            logger.log(
                    Level.SEVERE,
                    MessageFormatter.format(message, args),
                    t
            );
        }
    }

    // ---------- CRITICAL ----------

    @Override
    public void crit(String message) {
        error("[CRITICAL] " + message);
    }

    @Override
    public void crit(String message, Throwable t) {
        error("[CRITICAL] " + message, t);
    }

    @Override
    public void crit(String message, Throwable t, Object... args) {
        error("[CRITICAL] " + message, t, args);
    }
}
