import org.checkerframework.checker.nullness.qual.NonNull;

public interface IRequestHandler {
    Response handleRequest(@NonNull final Request request);
}
