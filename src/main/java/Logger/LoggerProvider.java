package Logger;

import java.util.Objects;

public final class LoggerProvider {

    private LoggerProvider() {}

    private static volatile ILoggerFactory factory =
            new JulLoggerFactory(); // default

    public static ILogger get(Class<?> clazz) {
        return factory.createLogger(clazz);
    }

    public static ILogger get(String name) {
        return factory.createLogger(name);
    }

    public static void setFactory(ILoggerFactory newFactory) {
        factory = Objects.requireNonNull(newFactory);
    }
}
