package Logger;

public interface ILogger {
    void trace(String message);

    void debug(String message);

    void info(String message);

    void warning(String message);

    void error(String message);

    void error(String message, Exception e);

    void critical(String message);

    void critical(String message, Exception e);
}
