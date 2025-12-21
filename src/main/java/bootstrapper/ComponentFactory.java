package bootstrapper;

import Identification.ClientIdentificationHandler;
import backend.automaton.BaseAutomatonFactory;
import backend.automaton.BaseTransitionTableFactory;
import backend.automaton.IAutomatonFactory;
import backend.automaton.ITransitionTableFactory;
import backend.requestHandler.AutomatonRequestHandler;
import backend.requestHandler.DemoRequestHandler;
import backend.requestHandler.IRequestHandler;
import config.ApplicationConfig;
import frontend.talker.AbstractTalker;
import frontend.talker.ConsoleTalker;
import frontend.talker.TelegramTalker;
import storage.IDataStorage;
import storage.SimpleDataStorage;

public final class ComponentFactory {
    private final ApplicationConfig config;
    private final IDataStorage dataStorage;
    private final ClientIdentificationHandler clientIdentificationHandler;

    public ComponentFactory(ApplicationConfig config) {
        this.config = config;
        this.dataStorage = new SimpleDataStorage();
        this.clientIdentificationHandler = new ClientIdentificationHandler(dataStorage);
    }

    private ITransitionTableFactory createTransitionTableFactory() {
//        if (config.getTransitionTableFactory() == ApplicationConfig.TransitionTableFactory.BASE) {
//            return new BaseTransitionTableFactory();
//        }
        return new BaseTransitionTableFactory();
    }

    private IAutomatonFactory createAutomatonFactory() {
        ITransitionTableFactory transitionTableFactory = createTransitionTableFactory();
        return new BaseAutomatonFactory(transitionTableFactory);
    }

    public IRequestHandler createRequestHandler() {
        ClientIdentificationHandler clientHandler = clientIdentificationHandler;
        IAutomatonFactory automatonFactory = createAutomatonFactory();
        return config.getMode() == ApplicationConfig.Mode.DEMO ? new DemoRequestHandler(dataStorage, clientHandler) : new AutomatonRequestHandler(dataStorage, clientHandler, automatonFactory);
    }

    public AbstractTalker createTalker() {
        ClientIdentificationHandler clientHandler = clientIdentificationHandler;
        IRequestHandler requestHandler = createRequestHandler();

        if (config.getInterface() == ApplicationConfig.Interface.CONSOLE) {
            return new ConsoleTalker(clientHandler, requestHandler);
        } else {
            String botToken = getBotToken();
            return new TelegramTalker(clientHandler, requestHandler, botToken);
        }
    }

    private String getBotToken() {
        String token = config.getBotToken();
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalStateException("BOT_TOKEN обязателен для Telegram");
        }
        return token;
    }
}