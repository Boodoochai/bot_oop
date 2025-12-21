package frontend.talker;

import Identification.ClientIdentificationHandler;
import backend.requestHandler.IRequestHandler;
import model.Client;
import model.Request;
import model.Response;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

final public class ConsoleTalker extends AbstractTalker {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleTalker.class);

    public ConsoleTalker(@NonNull final ClientIdentificationHandler clientIdentificationHandler, @NonNull final IRequestHandler requestHandler) {
        super(clientIdentificationHandler, requestHandler);
        logger.info("Инициализирован ConsoleTalker");
    }

    private @NonNull Request genRequest(@NonNull final String requestOwnerName, @NonNull final String requestString) {
        @NonNull final Client requestOwner = clientIdentificationHandler.getClient(requestOwnerName);
        logger.debug("Создан запрос: клиент='{}', текст='{}'", requestOwnerName, requestString);
        return new Request(requestOwner, requestString);
    }

    @Override
    public void run() {
        logger.info("Запущен консольный интерфейс. Ожидание ввода...");
        @NonNull final Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your name or \"quit\" to quit:");
            @NonNull final String input = in.nextLine();
            if (input.equals("quit")) {
                logger.info("Получена команда выхода от пользователя");
                break;
            }

            System.out.println("Enter your request:");
            @NonNull final String requestString = in.nextLine();

            logger.debug("Обработка запроса для пользователя: '{}'", input);
            System.out.println("Generating response...");
            @NonNull final Request request = genRequest(input, requestString);
            @NonNull final Response response = requestHandler.handleRequest(request);

            logger.debug("Получен ответ: '{}'", response.text());
            System.out.println("Response:");
            System.out.println(response.text());
        }

        in.close();
        logger.info("Консольный интерфейс остановлен");
    }
}