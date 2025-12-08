import org.checkerframework.checker.nullness.qual.NonNull;

final public class DemoRequestHandler extends IRequestHandler{

    public DemoRequestHandler(@NonNull final IDataStorage dataStorage, @NonNull final ClientIdentificationHandler clientIdentificationHandler) {
        super(dataStorage, clientIdentificationHandler);
    }

    public @NonNull Response handleRequest(final @NonNull Request request) {
        @NonNull final String res = request.getRequestOwner().getUUID().toString();
        return new Response(res);
    }
}