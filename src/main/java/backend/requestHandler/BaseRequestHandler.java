package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import backend.automaton.IAutomaton;
import backend.automaton.IAutomatonFactory;
import backend.useCases.handlers.IUseCaseHandler;
import backend.useCases.providers.IUseCaseProvider;
import model.Client;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.IDataStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class BaseRequestHandler implements IRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseRequestHandler.class);

    final IDataStorage dataStorage;
    final ClientIdentificationHandler clientIdentificationHandler;
    private final IAutomatonFactory automatonFactory;
    private final IUseCaseProvider useCaseProvider;

    public BaseRequestHandler(IDataStorage dataStorage, ClientIdentificationHandler clientIdentificationHandler, IAutomatonFactory automatonFactory, IUseCaseProvider useCaseProvider) {
        this.dataStorage = dataStorage;
        this.clientIdentificationHandler = clientIdentificationHandler;
        this.automatonFactory = automatonFactory;
        this.useCaseProvider = useCaseProvider;
        logger.info("Инициализирован BaseRequestHandler с automatonFactory: {} и useCaseProvider: {}", automatonFactory.getClass().getSimpleName(), useCaseProvider.getClass().getSimpleName());
    }

    private IAutomaton getOrCreateClientAutomaton(final IDataStorage dataStorage, final Client client) {
        if (!dataStorage.isExistAutomation(client.clientId())) {
            logger.debug("Автомат для клиента '{}' не найден, создаем новый", client.name());
            IAutomaton newAutomaton = automatonFactory.createAutomaton();
            dataStorage.setAutomation(client.clientId(), newAutomaton);
            logger.info("Автомат {} установлен для клиента '{}'", newAutomaton.getClass().getSimpleName(), client.name());
        } else {
            logger.debug("Используется существующий автомат для клиента '{}'", client.name());
        }

        return dataStorage.getAutomation(client.clientId());
    }

    private IUseCaseHandler getOrCreateUseCaseHandler(final IDataStorage dataStorage, final Client client, final IAutomaton automaton) {
        if (!dataStorage.isExistUseCaseHandler(client.clientId())) {
            logger.debug("Обработчик сценария для клиента '{}' не найден, получаем из провайдера", client.name());
            IUseCaseHandler useCaseHandler = useCaseProvider.getUseCaseHandler(automaton.getUseCase());
            if (useCaseHandler == null) {
                logger.error("Провайдер не вернул обработчик для use case: {}", automaton.getUseCase().name());
                return null;
            }
            dataStorage.setUseCaseHandler(client.clientId(), useCaseHandler);
            logger.info("Обработчик {} установлен для клиента '{}'", useCaseHandler.getClass().getSimpleName(), client.name());
        }
        return dataStorage.getUseCaseHandler(client.clientId());
    }

    private Response standardResponse(IAutomaton automaton) {
        var options = automaton.getOptions();
        logger.debug("Автомат не связан с use case, формируем ответ с опциями: {}", (Object) options);
        Response response = new Response(automaton.getStateText(), options);
        logger.debug("Получен ответ от автомата: '{}'", response.text());
        return response;
    }

    private List<Response> handleUseCase(final Request request, final Client client, IAutomaton automaton) {
        logger.info("Обнаружен сценарий использования: {}", automaton.getUseCase().name());
        IUseCaseHandler useCaseHandler = getOrCreateUseCaseHandler(dataStorage, client, automaton);
        if (useCaseHandler == null) {
            return List.of(new Response("Ошибка обработки запроса. Повторите попытку."));
        }

        logger.debug("Выполнение обработчика сценария: {}", useCaseHandler.getClass().getSimpleName());
        Response response = useCaseHandler.handleRequest(request, dataStorage, clientIdentificationHandler);

        Response secondResponse = null;
        if (useCaseHandler.isDone()) {
            logger.info("Обработчик сценария использования завершил работу для клиента '{}'", client.name());
            automaton.useCaseDone();
            logger.debug("Состояние автомата сброшено после завершения сценария");
            dataStorage.deleteUseCaseHandler(client.clientId());
            secondResponse = standardResponse(automaton);
        } else {
            logger.debug("Сценарий использования ещё не завершён");
        }

        logger.debug("Формирование окончательного ответа для клиента '{}'", client.name());
        return Stream.of(response, secondResponse).filter(Objects::nonNull).toList();
    }

    @Override
    public List<Response> handleRequest(final Request request) {
        Client owner = request.requestOwner();
        logger.info("Обработка запроса для клиента '{}': '{}'", owner.name(), request.text());

        IAutomaton automaton = getOrCreateClientAutomaton(dataStorage, owner);
        logger.debug("Текущее состояние автомата: {}", automaton.getClass().getSimpleName());

        if (automaton.getUseCase() == null) {
            automaton.next(request.text());
            logger.debug("Выполнен переход в автомате для клиента '{}'", owner.name());
        }

        if (automaton.getUseCase() == null) {
            return List.of(standardResponse(automaton));
        } else {
            return handleUseCase(request, owner, automaton);
        }
    }
}