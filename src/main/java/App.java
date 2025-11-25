public class App {
    public static void main(String[] args) {
        IDataStorage dataStorage = new SimpleDataStorage();

        IRequestHandler requestHandler = new DemoRequestHandler();

        AbstractTalker talker = new ConsoleTalker(dataStorage, requestHandler);

        talker.run();
    }
}
