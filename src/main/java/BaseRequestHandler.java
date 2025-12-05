import org.checkerframework.checker.nullness.qual.NonNull;

final public class BaseRequestHandler implements IRequestHandler{
    public @NonNull Response handleRequest(final @NonNull Request request) {
        return new Response("heh");
    }
}
