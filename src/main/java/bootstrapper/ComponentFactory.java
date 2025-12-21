package bootstrapper;

import Identification.ClientIdentificationHandler;
import backend.requestHandler.BaseRequestHandler;
import backend.requestHandler.DemoRequestHandler;
import backend.requestHandler.IRequestHandler;
import config.ApplicationConfig;
import frontend.talker.AbstractTalker;
import frontend.talker.ConsoleTalker;
import frontend.talker.TelegramTalker;
import storage.IDataStorage;
import storage.SimpleDataStorage;

/**
 * Фабрика компонентов.
 */
public final class ComponentFactory {
    private final ApplicationConfig config;
    private final IDataStorage dataStorage;

    public ComponentFactory(ApplicationConfig config) {
        this.config = config;
        this.dataStorage = new SimpleDataStorage(); // Можно передать как параметр
    }

    public IRequestHandler createRequestHandler() {
        return config.getMode() == ApplicationConfig.Mode.DEMO
                ? new DemoRequestHandler(dataStorage, createClientHandler())
                : new BaseRequestHandler(dataStorage, createClientHandler());
    }

    public ClientIdentificationHandler createClientHandler() {
        return new ClientIdentificationHandler(dataStorage);
    }

    public AbstractTalker createTalker() {
        var handler = createClientHandler();
        var requestHandler = createRequestHandler();

        return config.getInterface() == ApplicationConfig.Interface.CONSOLE
                ? new ConsoleTalker(handler, requestHandler)
                : new TelegramTalker(handler, requestHandler, getBotToken());
    }

    private String getBotToken() {
        String token = config.getBotToken();
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalStateException("BOT_TOKEN обязателен для Telegram");
        }
        return token;
    }
}