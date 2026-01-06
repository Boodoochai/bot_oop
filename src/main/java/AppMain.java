import bootstrapper.ApplicationBootstrapper;
import Logger.ILogger;
import Logger.LoggerProvider;
import runner.ApplicationRunner;

import java.util.function.Supplier;

public final class AppMain {
    private static final ILogger logger = LoggerProvider.get(AppMain.class);

    public static void main(String[] args) {
        logger.info("Запуск приложения...");

        try {
            Supplier<ApplicationRunner> supplier = new ApplicationBootstrapper().createRunnerSupplier(args);
            logger.debug("Создан supplier для ApplicationRunner");
            new App(supplier).run();
        } catch (Exception e) {
            logger.error("Критическая ошибка при запуске приложения", e);
            System.err.println("❌ Ошибка запуска: " + e.getMessage());
            System.exit(1);
        }
    }
}