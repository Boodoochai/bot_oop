import Logger.ILogger;
import Logger.LoggerProvider;
import runner.ApplicationRunner;

import java.util.function.Supplier;

/**
 * Основной класс приложения. Полностью зависит от внедрённых зависимостей.
 * Не содержит логики запуска.
 */
public final class App {
    private static final ILogger logger = LoggerProvider.get(App.class);

    private final Supplier<ApplicationRunner> runnerSupplier;

    public App(Supplier<ApplicationRunner> runnerSupplier) {
        this.runnerSupplier = runnerSupplier;
        logger.debug("Создан экземпляр App с runnerSupplier");
    }

    public void run() {
        logger.info("Запуск приложения...");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> logger.info("Приложение остановлено")));

        try {
            ApplicationRunner runner = runnerSupplier.get();
            logger.debug("Получен runner, запускаем...");
            runner.run();
        } catch (Exception e) {
            logger.error("Неожиданная ошибка во время работы", e);
            throw e;
        }
    }
}