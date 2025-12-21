package frontend.talker;

import Identification.ClientIdentificationHandler;
import backend.requestHandler.IRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Базовый класс для всех способов взаимодействия с пользователем.
 * Инкапсулирует обработчик запросов и логику идентификации клиента.
 */
public abstract class AbstractTalker {
    protected final ClientIdentificationHandler clientIdentificationHandler;
    protected final IRequestHandler requestHandler;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

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