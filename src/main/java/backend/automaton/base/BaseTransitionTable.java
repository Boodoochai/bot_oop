package backend.automaton.base;

import backend.automaton.IState;
import backend.automaton.ITransitionTable;
import backend.useCases.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Базовая реализация таблицы переходов для конечного автомата.
 * Хранит переходы между состояниями и текстовые описания состояний.
 */
public class BaseTransitionTable implements ITransitionTable {
    private static final Logger logger = LoggerFactory.getLogger(BaseTransitionTable.class);

    private final Map<IState, Map<String, IState>> transitionTable;
    private final Map<IState, String> stateText;
    private final Map<IState, String[][]> stateTransitions;
    private final Map<IState, UseCase> stateUseCase;
    private final Map<IState, IState> transitionFromUseCase;
    private final IState initialState;

    public BaseTransitionTable(Map<IState, Map<String, IState>> transitionTable, Map<IState, String> stateText,
                               Map<IState, String[][]> stateTransitions, Map<IState, UseCase> stateUseCase,
                               Map<IState, IState> transitionFromUseCase, IState initialState) {
        this.transitionTable = transitionTable;
        this.stateText = stateText;
        this.stateTransitions = stateTransitions;
        this.initialState = initialState;
        this.stateUseCase = stateUseCase;
        this.transitionFromUseCase = transitionFromUseCase;
        logger.debug("Создана таблица переходов с начальным состоянием: {}", initialState);
        if (logger.isTraceEnabled()) {
            logger.trace("Полная таблица переходов: {}", transitionTable);
        }
    }

    @Override
    public String[][] getTransitions(IState state) {
        if (!transitionTable.containsKey(state)) {
            logger.warn("Состояние {} отсутствует в таблице переходов", state);
            return null;
        }
        String[][] symbols = stateTransitions.get(state);
        logger.debug("Для состояния {} доступны символы: {}", state, symbols);
        return symbols;
    }

    @Override
    public IState getNextState(IState state, String symbol) {
        if (!transitionTable.containsKey(state)) {
            logger.warn("Состояние {} не найдено в таблице переходов. Остаемся в текущем состоянии.", state);
            return state;
        }

        Map<String, IState> transitions = transitionTable.get(state);
        IState nextState = transitions.get(symbol);

        if (nextState != null) {
            logger.debug("Переход: {} + '{}' → {}", state, symbol, nextState);
        } else {
            logger.debug("Переход для состояния {} и символа '{}' не определён", state, symbol);
        }

        return nextState;
    }

    @Override
    public String getStateText(IState state) {
        String text = stateText.get(state);
        if (text == null) {
            logger.warn("Текстовое описание для состояния {} не найдено", state);
            return "Неизвестное состояние";
        }
        logger.trace("Получен текст для состояния {}: '{}'", state, text);
        return text;
    }

    @Override
    public IState getInitialState() {
        logger.debug("Запрос начального состояния: {}", initialState);
        return initialState;
    }

    @Override
    public UseCase getUseCase(IState state) {
        return stateUseCase.get(state);
    }

    @Override
    public IState getTransitionFromUseCase(IState state) {
        return transitionFromUseCase.get(state);
    }
}