package Logger;

public interface ILoggerFactory {
    public ILogger createLogger(Class<?> clazz);

    public ILogger createLogger(String name);
}
