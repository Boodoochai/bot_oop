package bootstrapper;

import config.ApplicationConfig;
import frontend.talker.AbstractTalker;
import runner.ApplicationRunner;
import runner.DefaultApplicationRunner;

import java.util.function.Supplier;

/**
 * Собирает все компоненты приложения.
 * Это — Composition Root.
 */
public final class ApplicationBootstrapper {
    public Supplier<ApplicationRunner> createRunnerSupplier(String[] args) {
        return () -> {
            ApplicationConfig config = parseArgs(args);
            ComponentFactory factory = new ComponentFactory(config);
            AbstractTalker talker = factory.createTalker();
            return new DefaultApplicationRunner(talker);
        };
    }

    private ApplicationConfig parseArgs(String[] args) {
        ApplicationConfig.Builder builder = ApplicationConfig.builder();
        String botToken = System.getenv("BOT_TOKEN");

        for (String arg : args) {
            switch (arg) {
                case "demo" -> builder.mode(ApplicationConfig.Mode.DEMO);
                case "console" -> builder.interfaceType(ApplicationConfig.Interface.CONSOLE);
            }
        }

        builder.botToken(botToken);
        return builder.build();
    }
}