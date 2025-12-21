package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import backend.automaton.IAutomaton;
import backend.automaton.IAutomatonFactory;
import model.Client;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.IDataStorage;

public final class AutomatonRequestHandler extends IRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(AutomatonRequestHandler.class);
    private final IAutomatonFactory automatonFactory;

    public AutomatonRequestHandler(IDataStorage dataStorage, ClientIdentificationHandler clientIdentificationHandler, IAutomatonFactory automatonFactory) {
        super(dataStorage, clientIdentificationHandler);
        this.automatonFactory = automatonFactory;
        logger.debug("Инициализирован AutomatonRequestHandler");
    }

    @Override
    public Response handleRequest(final Request request) {
        Client owner = request.requestOwner();
        logger.info("Обработка запроса для клиента '{}': '{}'", owner.name(), request.text());

        if (!dataStorage.isExistAutomation(owner.clientId())) {
            logger.debug("Автомат для клиента '{}' не найден, создаем новый", owner.name());
            dataStorage.setAutomation(owner.clientId(), automatonFactory.createAutomaton());
        }

        IAutomaton automaton = dataStorage.getAutomation(owner.clientId());

        automaton.next(request.text());

        var automatonOptions = automaton.getOptions();

        String[][] options = new String[1][automatonOptions.size()];

        for (int i = 0; i < automatonOptions.size(); i++) {
            options[0][i] = automatonOptions.get(i);
        }

        Response response = new Response(automaton.getStateText(), options);
        logger.debug("Получен ответ от автомата для '{}': '{}'", owner.name(), response.text());


        return response;
    }
}