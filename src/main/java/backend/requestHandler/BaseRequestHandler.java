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

    @Override
    public Response handleRequest(final Request request) {
        Client owner = request.requestOwner();
        logger.info("Обработка запроса для клиента '{}': '{}'", owner.name(), request.text());

        if (!dataStorage.isExistAutomation(owner.clientId())) {
            logger.debug("Автомат для клиента '{}' не найден, создаем новый", owner.name());
            IAutomaton newAutomaton = automatonFactory.createAutomaton();
            dataStorage.setAutomation(owner.clientId(), newAutomaton);
            logger.info("Автомат {} установлен для клиента '{}'", newAutomaton.getClass().getSimpleName(), owner.name());
        } else {
            logger.debug("Используется существующий автомат для клиента '{}'", owner.name());
        }

        IAutomaton automaton = dataStorage.getAutomation(owner.clientId());

        logger.debug("Текущее состояние автомата: {}", automaton.getClass().getSimpleName());

        if (automaton.getUseCase() == null) {
            automaton.next(request.text());
            logger.debug("Выполнен переход в автомате для клиента '{}'", owner.name());
        }

        Response response = null;

        if (automaton.getUseCase() == null) {
            var options = automaton.getOptions();
            logger.debug("Автомат не связан с use case, формируем ответ с опциями: {}", options);
            response = new Response(automaton.getStateText(), options);
            logger.debug("Получен ответ от автомата для '{}': '{}'", owner.name(), response.text());
        } else {
            logger.info("Обнаружен сценарий использования: {}", automaton.getUseCase().name());
            if (!dataStorage.isExistUseCaseHandler(owner.clientId())) {
                logger.debug("Обработчик сценария для клиента '{}' не найден, получаем из провайдера", owner.name());
                IUseCaseHandler useCaseHandler = useCaseProvider.getUseCaseHandler(automaton.getUseCase());
                if (useCaseHandler == null) {
                    logger.error("Провайдер не вернул обработчик для use case: {}", automaton.getUseCase().name());
                    return new Response("Ошибка обработки запроса. Повторите попытку.");
                }
                dataStorage.setUseCaseHandler(owner.clientId(), useCaseHandler);
                logger.info("Обработчик {} установлен для клиента '{}'", useCaseHandler.getClass().getSimpleName(), owner.name());
            }

            IUseCaseHandler useCaseHandler = dataStorage.getUseCaseHandler(owner.clientId());
            logger.debug("Выполнение обработчика сценария: {}", useCaseHandler.getClass().getSimpleName());
            response = useCaseHandler.handleRequest(request, dataStorage);

            if (useCaseHandler.isDone()) {
                logger.info("Обработчик сценария использования завершил работу для клиента '{}'", owner.name());
                automaton.useCaseDone();
                logger.debug("Состояние автомата сброшено после завершения сценария");
            } else {
                logger.debug("Сценарий использования ещё не завершён, состояние сохранено");
            }
        }

        logger.debug("Формирование окончательного ответа для клиента '{}'", owner.name());
        return response;
    }
}