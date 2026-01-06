package bootstrapper;

import Logger.ILogger;
import Logger.LoggerProvider;
import config.ApplicationConfig;
import frontend.talker.AbstractTalker;
import runner.ApplicationRunner;
import runner.DefaultApplicationRunner;

import java.util.function.Supplier;

/**
 * Собирает все компоненты приложения.
 */
public final class ApplicationBootstrapper {
    private static final ILogger logger = LoggerProvider.get(ApplicationBootstrapper.class);

    public Supplier<ApplicationRunner> createRunnerSupplier(String[] args) {
        logger.info("Создание supplier'а для ApplicationRunner");
        logArgs(args);

        return () -> {
            try {
                ApplicationConfig config = parseArgs(args);
                logger.debug("Конфигурация успешно распаршена: mode={}, interface={}",
                        config.getMode(), config.getInterface());

                ComponentFactory factory = new ComponentFactory(config);
                logger.debug("Создана фабрика компонентов");

                AbstractTalker talker = factory.createTalker();
                logger.info("Создан talker: {}", talker.getClass().getSimpleName());

                return new DefaultApplicationRunner(talker);
            } catch (Exception e) {
                logger.error("Ошибка при создании ApplicationRunner", e);
                throw e;
            }
        };
    }

    private void logArgs(String[] args) {
        if (args.length == 0) {
            logger.info("Аргументы командной строки не переданы");
        } else {
            StringBuilder sb = new StringBuilder("Аргументы командной строки: ");
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
            logger.info(sb.toString().trim());
        }
    }

    private ApplicationConfig parseArgs(String[] args) {
        ApplicationConfig.Builder builder = ApplicationConfig.builder();
        String botToken = System.getenv("BOT_TOKEN");

        logger.debug("Чтение переменной окружения BOT_TOKEN: {}", botToken != null ? "***" : "не задана");

        for (String arg : args) {
            switch (arg) {
                case "demo" -> {
                    builder.mode(ApplicationConfig.Mode.DEMO);
                    logger.info("Активирован режим DEMO");
                }
                case "console" -> {
                    builder.interfaceType(ApplicationConfig.Interface.CONSOLE);
                    logger.info("Активирован консольный интерфейс");
                }
                default -> logger.warn("Неизвестный аргумент: {}", arg);
            }
        }

        builder.botToken(botToken);
        return builder.build();
    }
}