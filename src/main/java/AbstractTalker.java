import org.checkerframework.checker.nullness.qual.NonNull;

abstract class AbstractTalker {
    protected @NonNull final ClientIdentificationHandler clientIdentificationHandler;
    protected @NonNull final IRequestHandler requestHandler;

    AbstractTalker(@NonNull final ClientIdentificationHandler clientIdentificationHandler, @NonNull final IRequestHandler requestHandler) {
        this.clientIdentificationHandler = clientIdentificationHandler;
        this.requestHandler = requestHandler;
    }

    public abstract void run();
}