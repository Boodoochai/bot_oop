package Logger;

/**
 * It is a factory interface for creating ILogger objects.
 */
public interface ILoggerFactory {
    public ILogger createLogger(Class<?> clazz);

    public ILogger createLogger(String name);
}
