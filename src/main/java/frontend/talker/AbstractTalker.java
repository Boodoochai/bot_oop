package frontend.talker;

import Identification.ClientIdentificationHandler;
import Logger.ILogger;
import Logger.LoggerProvider;
import backend.requestHandler.IRequestHandler;

/**
 * Базовый класс для всех способов взаимодействия с пользователем.
 * Инкапсулирует обработчик запросов и логику идентификации клиента.
 */
public abstract class AbstractTalker {
    protected final ClientIdentificationHandler clientIdentificationHandler;
    protected final IRequestHandler requestHandler;
    protected final ILogger logger = LoggerProvider.get(this.getClass());

    public AbstractTalker(ClientIdentificationHandler clientIdentificationHandler,
                          IRequestHandler requestHandler) {
        this.clientIdentificationHandler = clientIdentificationHandler;
        this.requestHandler = requestHandler;
        logger.debug("Инициализирован AbstractTalker с обработчиком запросов: {} и идентификатором клиентов: {}",
                requestHandler.getClass().getSimpleName(),
                clientIdentificationHandler.getClass().getSimpleName());
    }

    public abstract void run();
}