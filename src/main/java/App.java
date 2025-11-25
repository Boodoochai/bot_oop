public class App {
    private final AbstractTalker talker;

    App(AbstractTalker talker) {
        this.talker = talker;
    }

    public static void main(String[] args) {
        IDataStorage dataStorage = new SimpleDataStorage();
        IRequestHandler requestHandler = new BaseRequestHandler();
        AbstractTalker talker = new ConsoleTalker(dataStorage, requestHandler);
        new App(talker).run();
    }

    void run() {
        talker.run();
    }
}
