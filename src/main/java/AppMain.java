import bootstrapper.ApplicationBootstrapper;
import Logger.ILogger;
import Logger.LoggerProvider;
import runner.ApplicationRunner;

public final class AppMain {
    private static final ILogger logger = LoggerProvider.get(AppMain.class);

    public static void main(String[] args) {
        logger.info("Запуск приложения...");

        try {
            ApplicationBootstrapper bootstrapper = new ApplicationBootstrapper();
            logger.debug("Создан ApplicationBootstrapper");

            ApplicationRunner runner = bootstrapper.createRunner(args);
            logger.debug("Создан ApplicationRunner");

            new App(runner).run();
        } catch (Exception e) {
            logger.error("Критическая ошибка при запуске приложения", e);
            System.err.println("❌ Ошибка запуска: " + e.getMessage());
            System.exit(1);
        }
    }
}