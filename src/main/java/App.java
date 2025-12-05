import org.checkerframework.checker.nullness.qual.NonNull;

final public class App {
    private @NonNull final AbstractTalker talker;

    App(@NonNull final AbstractTalker talker) {
        this.talker = talker;
    }

    public static void main(String[] args) {
        @NonNull final IDataStorage dataStorage = new SimpleDataStorage();
        @NonNull final IRequestHandler requestHandler = new DemoRequestHandler(dataStorage);
        @NonNull final ClientIdentificationHandler clientIdentificationHandler = new ClientIdentificationHandler(dataStorage);
        @NonNull final AbstractTalker talker = new ConsoleTalker(clientIdentificationHandler, requestHandler);
        @NonNull final App app = new App(talker);
        app.run();
    }

    void run()
    {
        talker.run();
    }
}
