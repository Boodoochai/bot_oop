import org.checkerframework.checker.nullness.qual.NonNull;

final public class DemoRequestHandler implements IRequestHandler{
    public @NonNull Response handleRequest(@NonNull final Request request) {
        return new Response("heh");
    }
}
