import org.checkerframework.checker.nullness.qual.NonNull;

abstract class AbstractTalker {
    protected @NonNull final IDataStorage dataStorage;
    protected @NonNull final IRequestHandler requestHandler;

    AbstractTalker(@NonNull final IDataStorage dataStorage, @NonNull final IRequestHandler requestHandler) {
        this.dataStorage = dataStorage;
        this.requestHandler = requestHandler;
    }

    public abstract void run();
}