import org.checkerframework.checker.nullness.qual.NonNull;

final public class App {
    private @NonNull final AbstractTalker talker;

    App(@NonNull final AbstractTalker talker) {
        this.talker = talker;
    }

    public static void main(String[] args) {
        @NonNull final IDataStorage dataStorage = new SimpleDataStorage();

        // Demo
        @NonNull final IRequestHandler requestHandler = new DemoRequestHandler(dataStorage);

        // Prod
//        @NonNull final IRequestHandler requestHandler = new BaseRequestHandler(dataStorage);

        @NonNull final ClientIdentificationHandler clientIdentificationHandler = new ClientIdentificationHandler(dataStorage);

        // Console
        // @NonNull final AbstractTalker talker = new ConsoleTalker(clientIdentificationHandler, requestHandler);

        // Telegram
        @NonNull final AbstractTalker talker = new TelegramTalker(clientIdentificationHandler, requestHandler, "8052483079:AAHFu7aZD_aHn0AL-Qq8SXSxrYQA9E2QtKQ");

        @NonNull final App app = new App(talker);
        app.run();
    }

    void run()
    {
        talker.run();
    }
}
