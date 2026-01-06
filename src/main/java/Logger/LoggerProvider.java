package Logger;

import java.util.Objects;

/**
 * Provides a Logger instance to the outer world.
 * Used LoggerFactory can be replaced by a custom implementation, but only before any get calls.
 */
public final class LoggerProvider {

    private LoggerProvider() {}

    private static volatile ILoggerFactory factory =
            new JulLoggerFactory();

    private static volatile boolean frozen = false;

    public static ILogger get(Class<?> clazz) {
        frozen = true;
        return factory.createLogger(clazz);
    }

    public static ILogger get(String name) {
        frozen = true;
        return factory.createLogger(name);
    }

    public static void setFactory(ILoggerFactory newFactory) {
        if (frozen) {
            throw new IllegalStateException(
                    "LoggerFactory already in use"
            );
        }
        factory = Objects.requireNonNull(newFactory);
    }
}
