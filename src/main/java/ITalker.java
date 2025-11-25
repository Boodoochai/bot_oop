abstract class ITalker {
    protected final IDataStorage dataStorage;
    protected final IRequestHandler requestHandler;

    ITalker(final IDataStorage dataStorage, final IRequestHandler requestHandler) {
        this.dataStorage = dataStorage;
        this.requestHandler = requestHandler;
    }

    public abstract void run();
}