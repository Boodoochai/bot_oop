import org.checkerframework.checker.nullness.qual.NonNull;

final public class App {
    private @NonNull final AbstractTalker talker;

    App(@NonNull final AbstractTalker talker) {
        this.talker = talker;
    }

    public static void main(@NonNull String[] args) {
        @NonNull final IDataStorage dataStorage = new SimpleDataStorage();

        @NonNull final ClientIdentificationHandler clientIdentificationHandler = new ClientIdentificationHandler(dataStorage);

        @NonNull final IRequestHandler requestHandler;
        if (args.length > 0 && args[0].equals("demo")) {
            requestHandler = new DemoRequestHandler(dataStorage, clientIdentificationHandler);
        } else {
            requestHandler = new BaseRequestHandler(dataStorage, clientIdentificationHandler);
        }

        @NonNull final AbstractTalker talker;
        if (args.length > 1 && args[1].equals("console")) {
            talker = new ConsoleTalker(clientIdentificationHandler, requestHandler);
        } else {
            talker = new TelegramTalker(clientIdentificationHandler, requestHandler, "8052483079:AAHFu7aZD_aHn0AL-Qq8SXSxrYQA9E2QtKQ");
        }

        @NonNull final App app = new App(talker);
        app.run();
    }

    void run()
    {
        talker.run();
    }
}
