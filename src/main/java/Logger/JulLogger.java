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

    @Override
    public void trace(String message) {
        logger.fine(message);
    }

    @Override
    public void debug(String message) {
        logger.fine(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warning(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message) {
        logger.severe(message);
    }

    @Override
    public void error(String message, Exception e) {
        logger.log(Level.SEVERE, message, e);
    }

    @Override
    public void critical(String message) {
        logger.severe("[CRITICAL] " + message);
    }

    @Override
    public void critical(String message, Exception e) {
        logger.log(Level.SEVERE, "[CRITICAL] " + message, e);
    }
}
