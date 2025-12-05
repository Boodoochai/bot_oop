import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class IRequestHandler {

    @NonNull final IDataStorage dataStorage;
    @NonNull final ClientIdentificationHandler clientIdentificationHandler;

    IRequestHandler(@NonNull final IDataStorage dataStorage, @NonNull final ClientIdentificationHandler clientIdentificationHandler) {
        this.dataStorage = dataStorage;
        this.clientIdentificationHandler = clientIdentificationHandler;
    }

    abstract @NonNull Response handleRequest(@NonNull final Request request);
}
