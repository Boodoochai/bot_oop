import org.checkerframework.checker.nullness.qual.NonNull;

final public class App {
    private @NonNull final AbstractTalker talker;

    App(@NonNull final AbstractTalker talker) {
        this.talker = talker;
    }

    public static void main(@NonNull String[] args) {
        @NonNull final IDataStorage dataStorage = new SimpleDataStorage();

        @NonNull final ClientIdentificationHandler clientIdentificationHandler = new ClientIdentificationHandler(dataStorage);

        if (args.length < 2) {
            System.out.println("Not enough arguments 2 expected");
            return;
        }

        @NonNull final IRequestHandler requestHandler;
        if (args[0].equals("demo")) {
            requestHandler = new DemoRequestHandler(dataStorage, clientIdentificationHandler);
        } else if (args[0].equals("base")) {
            requestHandler = new BaseRequestHandler(dataStorage, clientIdentificationHandler);
        } else {
            System.out.println("Wrong first argument use \"demo\" or \"base\"");
            return;
        }

        @NonNull final AbstractTalker talker;
        if (args[1].equals("console")) {
            talker = new ConsoleTalker(clientIdentificationHandler, requestHandler);
        } else if (args[1].equals("telegram")) {
            talker = new TelegramTalker(clientIdentificationHandler, requestHandler, "8052483079:AAHFu7aZD_aHn0AL-Qq8SXSxrYQA9E2QtKQ");
        } else {
            System.out.println("Wrong second argument use \"console\" or \"telegram\"");
            return;
        }

        @NonNull final App app = new App(talker);
        app.run();
    }

    void run()
    {
        talker.run();
    }
}
