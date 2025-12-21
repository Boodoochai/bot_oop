import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.ApplicationRunner;

import java.util.function.Supplier;

/**
 * Основной класс приложения. Полностью зависит от внедрённых зависимостей.
 * Не содержит логики запуска.
 */
public final class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private final Supplier<ApplicationRunner> runnerSupplier;

    public App(Supplier<ApplicationRunner> runnerSupplier) {
        this.runnerSupplier = runnerSupplier;
    }

    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> logger.info("Приложение остановлено")));

        try {
            runnerSupplier.get().run();
        } catch (Exception e) {
            logger.error("Неожиданная ошибка во время работы", e);
            throw e;
        }
    }
}