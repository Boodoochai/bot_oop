package backend.automaton;

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
        table.get(State.START).put("help", State.HELP);
        table.get(State.HELP).put("help", State.HELP);

        // Настройка текстовых описаний состояний
        Map<IState, String> stateText = new HashMap<>();
        stateText.put(State.START, """
                Привет! Я бот для составления расписания.""");
        stateText.put(State.HELP, """
                Доступные команды:
                help - показать это сообщение.
                """);

        logger.info("Таблица переходов успешно создана");
        return new BaseTransitionTable(table, stateText, State.START);
    }

    private enum State implements IState {
        START,
        HELP,
    }
}