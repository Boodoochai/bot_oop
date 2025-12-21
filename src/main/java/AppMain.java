import bootstrapper.ApplicationBootstrapper;
import runner.ApplicationRunner;

import java.util.function.Supplier;

public final class AppMain {
    public static void main(String[] args) {
        try {
            Supplier<ApplicationRunner> supplier = new ApplicationBootstrapper().createRunnerSupplier(args);
            new App(supplier).run();
        } catch (Exception e) {
            System.err.println("Ошибка запуска: " + e.getMessage());
            System.exit(1);
        }
    }
}