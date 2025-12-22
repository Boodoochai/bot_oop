package frontend.talker;

import Identification.ClientIdentificationHandler;
import backend.requestHandler.IRequestHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class TelegramTalker extends AbstractTalker {
    private static final Logger logger = LoggerFactory.getLogger(TelegramTalker.class);

    private final TelegramBot bot;
    private int offset = 0;

    public TelegramTalker(ClientIdentificationHandler clientIdentificationHandler,
                          IRequestHandler requestHandler,
                          String botToken) {
        super(clientIdentificationHandler, requestHandler);
        this.bot = new TelegramBot(botToken);
        logger.info("Инициализирован TelegramTalker для бота");
    }

    @Override
    public void run() {
        logger.info("Запущен Telegram бот. Ожидание обновлений...");
        while (true) {
            try {
                GetUpdatesResponse updatesResponse = bot.execute(
                        new GetUpdates().limit(100).offset(offset)
                );

                if (updatesResponse == null || updatesResponse.updates() == null) {
                    logger.debug("Получен null-ответ от Telegram API, пропуск итерации");
                    Thread.sleep(1000);
                    continue;
                }

                for (Update update : updatesResponse.updates()) {
                    offset = update.updateId() + 1;
                    logger.debug("Получено обновление от Telegram: updateId={}", update.updateId());

                    if (update.message() != null && update.message().text() != null) {
                        String userName = getUserName(update);
                        String requestText = update.message().text();

                        logger.debug("Обработка запроса от пользователя: '{}' → текст: '{}'", userName, requestText);

                        Request request = new Request(
                                clientIdentificationHandler.getClient(userName),
                                requestText
                        );

                        List<Response> response = requestHandler.handleRequest(request);

                        for (Response resp : response) {
                            logger.debug("Сформирован ответ для '{}': '{}'", userName, resp.text());
                            sendResponse(update, resp);
                        }
                    }
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                logger.error("Неожиданная ошибка в polling-цикле Telegram бота", e);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    logger.warn("Поток TelegramTalker был прерван во время ожидания");
                }
            }
        }
    }

    private String getUserName(Update update) {
        var from = update.message().from();
        if (from == null) {
            logger.warn("Поле 'from' отсутствует в сообщении updateId={}. Используем 'unknown_user'", update.updateId());
            return "unknown_user";
        }

        String userName = from.username();
        if (userName != null && !userName.isEmpty()) {
            return userName;
        }

        String firstName = from.firstName();
        if (firstName != null && !firstName.isEmpty()) {
            logger.debug("Используем firstName как имя пользователя: '{}'", firstName);
            return firstName;
        }

        logger.warn("Не удалось определить имя пользователя в updateId={}. Используем 'unknown_user'", update.updateId());
        return "unknown_user";
    }

    private void sendResponse(Update update, Response response) {
        try {
            var options = response.options();
            if (options != null) {
                ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(options).resizeKeyboard(true).oneTimeKeyboard(response.isTempOptions());
                bot.execute(new SendMessage(update.message().chat().id(), response.text()).replyMarkup(keyboard));
                logger.debug("Отправлен ответ с кнопками пользователю: {}", update.message().chat().id());
            } else {
                bot.execute(new SendMessage(update.message().chat().id(), response.text()));
                logger.debug("Отправлен текстовый ответ пользователю: {}", update.message().chat().id());
            }
        } catch (Exception e) {
            logger.error("Ошибка при отправке ответа в Telegram для chatId={}", update.message().chat().id(), e);
        }
    }
}