package frontend.talker;

import Identification.ClientIdentificationHandler;
import backend.requestHandler.IRequestHandler;
import model.Client;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

final public class ConsoleTalker extends AbstractTalker {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleTalker.class);

    public ConsoleTalker(final ClientIdentificationHandler clientIdentificationHandler, final IRequestHandler requestHandler) {
        super(clientIdentificationHandler, requestHandler);
        logger.info("Инициализирован ConsoleTalker");
    }

    private Request genRequest(final String requestOwnerName, final String requestString) {
        final Client requestOwner = clientIdentificationHandler.getClient(requestOwnerName);
        logger.debug("Создан запрос: клиент='{}', текст='{}'", requestOwnerName, requestString);
        return new Request(requestOwner, requestString);
    }

    @Override
    public void run() {
        logger.info("Запущен консольный интерфейс. Ожидание ввода...");
        final Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your name or \"quit\" to quit:");
            String input = in.nextLine();
            if (input.equals("quit")) {
                logger.info("Получена команда выхода от пользователя");
                break;
            }

            System.out.println("Enter your request:");
            String requestString = in.nextLine();

            logger.debug("Обработка запроса для пользователя: '{}'", input);
            System.out.println("Generating response...");
            Request request = genRequest(input, requestString);
            var response = requestHandler.handleRequest(request);

            for (Response r : response) {
                logger.debug("Получен ответ: '{}'", r.text());
                System.out.println("Response:");
                System.out.println(r.text());
            }
        }

        in.close();
        logger.info("Консольный интерфейс остановлен");
    }
}