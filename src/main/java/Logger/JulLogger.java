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
        if (!logger.isLoggable(Level.FINE)) {
            return;
        }

        LogCall call = LogCall.of(message, args);
        logger.fine(call.formattedMessage());
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
        if (!logger.isLoggable(Level.INFO)) {
            return;
        }

        LogCall call = LogCall.of(message, args);
        logger.info(call.formattedMessage());
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
        if (!logger.isLoggable(Level.WARNING)) {
            return;
        }

        LogCall call = LogCall.of(message, args);
        logger.warning(call.formattedMessage());
    }

    // ---------- ERROR ----------

    @Override
    public void error(String message) {
        if (logger.isLoggable(Level.SEVERE)) {
            logger.severe(message);
        }
    }

    @Override
    public void error(String message, Object... args) {
        if (!logger.isLoggable(Level.SEVERE)) {
            return;
        }

        LogCall call = LogCall.of(message, args);

        if (call.throwable() != null) {
            logger.log(Level.SEVERE, call.formattedMessage(), call.throwable());
        } else {
            logger.severe(call.formattedMessage());
        }
    }

    // ---------- CRITICAL ----------

    @Override
    public void crit(String message) {
        error("[CRITICAL] " + message);
    }

    @Override
    public void crit(String message, Object... args) {
        error("[CRITICAL] " + message, args);
    }
}
