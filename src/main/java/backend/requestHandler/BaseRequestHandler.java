package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import backend.automaton.CalendarAutomaton;
import model.Client;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.IDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BaseRequestHandler extends IRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseRequestHandler.class);
    private final Map<UUID, CalendarAutomaton> machines = new HashMap<>();

    public BaseRequestHandler(IDataStorage dataStorage,
                              ClientIdentificationHandler clientIdentificationHandler) {
        super(dataStorage, clientIdentificationHandler);
        logger.debug("Инициализирован BaseRequestHandler");
    }

    @Override
    public Response handleRequest(final Request request) {
        Client owner = request.requestOwner();
        logger.info("Обработка запроса для клиента '{}': '{}'", owner.name(), request.text());

        CalendarAutomaton machine = machines.computeIfAbsent(
                owner.clientId(),
                id -> {
                    logger.debug("Создан новый автомат для клиента: {}", owner.name());
                    return new CalendarAutomaton(dataStorage, clientIdentificationHandler, owner);
                }
        );

        Response response = machine.feed(request.text());
        logger.debug("Получен ответ от автомата для '{}': '{}'", owner.name(), response.text());

        return response;
    }
}