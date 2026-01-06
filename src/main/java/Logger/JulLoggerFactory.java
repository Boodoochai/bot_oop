package Logger;

import java.util.logging.Logger;

/**
 * Implements ILoggerFactory interface using Java Util Logging (JUL).
 */
class JulLoggerFactory implements ILoggerFactory {

    @Override
    public ILogger createLogger(Class<?> clazz) {
        return new JulLogger(
                Logger.getLogger(clazz.getName())
        );
    }

    @Override
    public ILogger createLogger(String name) {
        return new JulLogger(
                Logger.getLogger(name)
        );
    }
}
