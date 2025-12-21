package frontend.talker;

import Identification.ClientIdentificationHandler;
import backend.requestHandler.IRequestHandler;

/**
 * Базовый класс для всех способов взаимодействия с пользователем.
 * Инкапсулирует обработчик запросов и логику идентификации клиента.
 */
public abstract class AbstractTalker {
    protected final ClientIdentificationHandler clientIdentificationHandler;
    protected final IRequestHandler requestHandler;

    public AbstractTalker(ClientIdentificationHandler clientIdentificationHandler,
                          IRequestHandler requestHandler) {
        this.clientIdentificationHandler = clientIdentificationHandler;
        this.requestHandler = requestHandler;
    }

    public abstract void run();
}