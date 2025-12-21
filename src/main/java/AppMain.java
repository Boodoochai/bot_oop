import bootstrapper.ApplicationBootstrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import runner.ApplicationRunner;

import java.util.function.Supplier;

public final class AppMain {
    private static final Logger logger = LoggerFactory.getLogger(AppMain.class);

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