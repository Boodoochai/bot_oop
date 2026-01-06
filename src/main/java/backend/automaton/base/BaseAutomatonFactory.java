package backend.automaton.base;

import Logger.ILogger;
import Logger.LoggerProvider;
import backend.automaton.IAutomaton;
import backend.automaton.IAutomatonFactory;
import backend.automaton.ITransitionTable;
import backend.automaton.ITransitionTableFactory;

public class BaseAutomatonFactory implements IAutomatonFactory {
    private static final ILogger logger = LoggerProvider.get(BaseAutomatonFactory.class);

    private final ITransitionTable transitionTable;

    public BaseAutomatonFactory(ITransitionTableFactory transitionTableFactory) {
        this.transitionTable = transitionTableFactory.createTransitionTable();
        if (this.transitionTable == null) {
            logger.error("Создана пустая таблица переходов — возможна ошибка в инициализации");
            throw new IllegalStateException("transitionTableFactory вернул null");
        }
        logger.info("Фабрика автомата инициализирована с таблицей переходов");
    }

    @Override
    public IAutomaton createAutomaton() {
        logger.debug("Создаётся новый экземпляр автомата");
        return new BaseAutomaton(transitionTable);
    }
}