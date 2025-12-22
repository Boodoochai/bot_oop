package backend.automaton.base;

import backend.automaton.IAutomaton;
import backend.automaton.IState;
import backend.automaton.ITransitionTable;
import backend.useCases.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseAutomaton implements IAutomaton {
    private static final Logger logger = LoggerFactory.getLogger(BaseAutomaton.class);

    private final ITransitionTable transitionTable;
    private IState currentState;

    BaseAutomaton(ITransitionTable transitionTable) {
        this.transitionTable = transitionTable;
        this.currentState = transitionTable.getInitialState();
        logger.info("Создан автомат с начальным состоянием: {}", currentState);
    }

    @Override
    public String[][] getOptions() {
        String[][] options = transitionTable.getTransitions(currentState);
        logger.debug("Доступные опции в состоянии {}: {}", currentState, options);
        return options;
    }

    @Override
    public void next(String input) {
        logger.info("Получен ввод: '{}'", input);
        IState nextState = transitionTable.getNextState(currentState, input);
        if (nextState != null) {
            logger.debug("Переход: {} → {}", currentState, nextState);
            currentState = nextState;
        } else {
            logger.warn("Недопустимый ввод '{}' в состоянии {}, остаёмся в {}", input, currentState, currentState);
        }
    }

    @Override
    public String getStateText() {
        String text = transitionTable.getStateText(currentState);
        logger.debug("Текст состояния {}: '{}'", currentState, text);
        return text;
    }

    @Override
    public UseCase getUseCase() {
        return transitionTable.getUseCase(currentState);
    }

    @Override
    public void useCaseDone() {
        currentState = transitionTable.getTransitionFromUseCase(currentState);
    }
}