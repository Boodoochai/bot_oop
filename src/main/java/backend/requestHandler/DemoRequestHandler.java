package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.IDataStorage;

final public class DemoRequestHandler extends IRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(DemoRequestHandler.class);

    public DemoRequestHandler(final IDataStorage dataStorage, final ClientIdentificationHandler clientIdentificationHandler) {
        super(dataStorage, clientIdentificationHandler);
        logger.debug("Инициализирован DemoRequestHandler");
    }

    @Override
    public Response handleRequest(final Request request) {
        logger.info("Обработка запроса в режиме демо: клиент='{}', текст='{}'",
                request.requestOwner().name(), request.text());

        final String res = request.requestOwner().clientId().toString();

        logger.debug("Сформирован демо-ответ: {}", res);
        return new Response(res);
    }
}