package runner;

import frontend.talker.AbstractTalker;

/**
 * Реализация runner.ApplicationRunner.
 */
public final class DefaultApplicationRunner implements ApplicationRunner {
    private final AbstractTalker talker;

    public DefaultApplicationRunner(AbstractTalker talker) {
        this.talker = talker;
    }

    @Override
    public void run() {
        talker.run();
    }
}