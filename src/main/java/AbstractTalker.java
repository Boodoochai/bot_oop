abstract class AbstractTalker {
    protected final IDataStorage dataStorage;
    protected final IRequestHandler requestHandler;

    AbstractTalker(final IDataStorage dataStorage, final IRequestHandler requestHandler) {
        this.dataStorage = dataStorage;
        this.requestHandler = requestHandler;
    }

    public abstract void run();
}