import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

public final class TelegramTalker extends AbstractTalker {

    private final TelegramBot bot;
    private int offset = 0;

    public TelegramTalker(ClientIdentificationHandler clientIdentificationHandler,
                          IRequestHandler requestHandler,
                          String botToken) {
        super(clientIdentificationHandler, requestHandler);
        this.bot = new TelegramBot(botToken);
    }

    @Override
    public void run() {
        System.out.println("Telegram bot started...");

        while (true) {
            try {
                // Получаем обновления
                GetUpdatesResponse updatesResponse = bot.execute(
                        new GetUpdates().limit(100).offset(offset)
                );

                for (Update update : updatesResponse.updates()) {
                    offset = update.updateId() + 1;

                    if (update.message() != null && update.message().text() != null) {
                        String userName = update.message().from().username();
                        if (userName == null || userName.isEmpty()) {
                            userName = update.message().from().firstName();
                        }
                        String requestText = update.message().text();

                        // Создаем Request
                        Request request = new Request(
                                clientIdentificationHandler.getClient(userName),
                                requestText
                        );

                        // Получаем Response
                        Response response = requestHandler.handleRequest(request);

                        var options = response.getOptions();

                        if (options != null) {
                            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(options).resizeKeyboard(true).oneTimeKeyboard(true);
                            bot.execute(new SendMessage(update.message().chat().id(), response.getText()).replyMarkup(keyboard));
                        } else {
                            bot.execute(new SendMessage(update.message().chat().id(), response.getText()));
                        }
                    }
                }

                Thread.sleep(1000); // небольшая пауза между polling
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
