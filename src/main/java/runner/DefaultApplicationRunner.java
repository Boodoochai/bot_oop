package runner;

import frontend.talker.AbstractTalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Реализация runner.ApplicationRunner.
 */
public final class DefaultApplicationRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DefaultApplicationRunner.class);

    private final AbstractTalker talker;

    public DefaultApplicationRunner(AbstractTalker talker) {
        this.talker = talker;
        logger.debug("Создан DefaultApplicationRunner для talker: {}", talker.getClass().getSimpleName());
    }

    @Override
    public void run() {
        logger.info("Запуск talker: {}", talker.getClass().getSimpleName());
        try {
            talker.run();
            logger.info("Работа talker завершена: {}", talker.getClass().getSimpleName());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка в процессе выполнения talker: {}", talker.getClass().getSimpleName(), e);
            throw e;
        }
    }
}