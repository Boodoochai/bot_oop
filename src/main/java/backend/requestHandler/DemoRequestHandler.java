package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import Logger.ILogger;
import Logger.LoggerProvider;
import model.Request;
import model.Response;
import storage.IDataStorage;

import java.util.List;

final public class DemoRequestHandler implements IRequestHandler {
    private static final ILogger logger = LoggerProvider.get(DemoRequestHandler.class);
    final IDataStorage dataStorage;
    final ClientIdentificationHandler clientIdentificationHandler;

    public DemoRequestHandler(final IDataStorage dataStorage, final ClientIdentificationHandler clientIdentificationHandler) {
        this.dataStorage = dataStorage;
        this.clientIdentificationHandler = clientIdentificationHandler;
        logger.debug("Инициализирован DemoRequestHandler");
    }

    @Override
    public List<Response> handleRequest(final Request request) {
        logger.info("Обработка запроса в режиме демо: клиент='{}', текст='{}'",
                request.requestOwner().name(), request.text());

        final String res = request.requestOwner().clientId().toString();

        logger.debug("Сформирован демо-ответ: {}", res);
        Response response = new Response(res);
        return List.of(response);
    }
}