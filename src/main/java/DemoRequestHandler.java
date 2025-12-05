import org.checkerframework.checker.nullness.qual.NonNull;

final public class DemoRequestHandler implements IRequestHandler{

    private final @NonNull IDataStorage dataStorage;

    public DemoRequestHandler(@NonNull final IDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }
    public @NonNull Response handleRequest(final @NonNull Request request) {
        return new Response("heh");
    }
}