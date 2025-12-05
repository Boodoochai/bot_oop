import org.checkerframework.checker.nullness.qual.NonNull;

final public class DemoRequestHandler implements IRequestHandler{

    private final @NonNull IDataStorage dataStorage;

    public DemoRequestHandler(@NonNull final IDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }
    public @NonNull Response handleRequest(final @NonNull Request request) {
        @NonNull final String res = request.getRequestOwner().getUUID().toString();
        return new Response(res);
    }
}