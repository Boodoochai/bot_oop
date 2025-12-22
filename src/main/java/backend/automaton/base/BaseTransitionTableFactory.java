package backend.automaton.base;

import backend.automaton.IState;
import backend.automaton.ITransitionTable;
import backend.automaton.ITransitionTableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика для создания таблицы переходов конечного автомата.
 * Определяет доступные команды и переходы между состояниями.
 */
public class BaseTransitionTableFactory implements ITransitionTableFactory {
    private static final Logger logger = LoggerFactory.getLogger(BaseTransitionTableFactory.class);

    @Override
    public ITransitionTable createTransitionTable() {
        logger.info("Создание таблицы переходов для автомата");

        Map<IState, Map<String, IState>> table = new HashMap<>();

        // Инициализация состояний
        for (State state : State.values()) {
            table.put(state, new HashMap<>());
        }

        // Настройка переходов
        table.get(State.START).put("Помощь", State.HELP);
        table.get(State.HELP).put("Помощь", State.HELP);

        // Настройка текстовых описаний состояний
        Map<IState, String> stateText = new HashMap<>();
        stateText.put(State.START, """
                Привет! Я бот для составления расписания.""");
        stateText.put(State.HELP, """
                Доступные команды:
                Помощь - показать это сообщение.
                """);

        Map<IState, String[][]> stateTransitions = new HashMap<>();

        stateTransitions.put(State.START, new String[][] {{"Помощь"}});
        stateTransitions.put(State.HELP, new String[][] {{"Помощь"}});

        logger.info("Таблица переходов успешно создана");
        return new BaseTransitionTable(table, stateText, stateTransitions, State.START);
    }

    private enum State implements IState {
        START,
        HELP,
    }
}