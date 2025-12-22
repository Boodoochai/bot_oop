package backend.automaton.base;

import backend.automaton.IAutomaton;
import backend.automaton.IAutomatonFactory;
import backend.automaton.ITransitionTable;
import backend.automaton.ITransitionTableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseAutomatonFactory implements IAutomatonFactory {
    private static final Logger logger = LoggerFactory.getLogger(BaseAutomatonFactory.class);

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