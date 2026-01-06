package backend.automaton.base;

import Logger.ILogger;
import Logger.LoggerProvider;
import backend.automaton.IState;
import backend.automaton.ITransitionTable;
import backend.automaton.ITransitionTableFactory;
import backend.useCases.UseCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика для создания таблицы переходов конечного автомата.
 * Определяет доступные команды и переходы между состояниями.
 */
public class BaseTransitionTableFactory implements ITransitionTableFactory {
    private static final ILogger logger = LoggerProvider.get(BaseTransitionTableFactory.class);

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
        table.get(State.HELP).put("Создать встречу", State.CREATE_MEETING);
        table.get(State.HELP).put("Просмотреть встречи", State.VIEW_MEETINGS);
        table.get(State.HELP).put("Редактировать встречу", State.UPDATE_MEETING);
        table.get(State.HELP).put("Удалить встречу", State.DELETE_MEETING);
        table.get(State.CREATE_MEETING).put("Помощь", State.HELP);
        table.get(State.VIEW_MEETINGS).put("Помощь", State.HELP);
        table.get(State.DELETE_MEETING).put("Помощь", State.HELP);
        table.get(State.UPDATE_MEETING).put("Помощь", State.HELP);

        // Настройка текстовых описаний состояний
        Map<IState, String> stateText = new HashMap<>();
        stateText.put(State.START, """
                Привет! Я бот для составления расписания.""");
        stateText.put(State.HELP, """
                Доступные команды:
                Помощь - показать это сообщение.
                """);
        stateText.put(State.CREATE_MEETING, """
                Временное сообщение для состояния CREATE_MEETING.
                """);
        stateText.put(State.VIEW_MEETINGS, """
                Временное сообщение для состояния VIEW_MEETINGS.
                """);
        stateText.put(State.DELETE_MEETING, """
                Временное сообщение для состояния DELETE_MEETING.
                """);
        stateText.put(State.UPDATE_MEETING, """
                Временное сообщение для состояния UPDATE_MEETING.
                """);

        Map<IState, String[][]> stateTransitions = new HashMap<>();

        stateTransitions.put(State.START, new String[][]{{"Помощь"}});
        stateTransitions.put(State.HELP, new String[][]{{"Создать встречу"}, {"Просмотреть встречи"}, {"Редактировать встречу"}, {"Удалить встречу"}, {"Помощь"}});
        stateTransitions.put(State.CREATE_MEETING, new String[][]{{"Помощь"}});
        stateTransitions.put(State.VIEW_MEETINGS, new String[][]{{"Помощь"}});
        stateTransitions.put(State.DELETE_MEETING, new String[][]{{"Помощь"}});
        stateTransitions.put(State.UPDATE_MEETING, new String[][]{{"Помощь"}});

        Map<IState, UseCase> stateUseCase = new HashMap<>();

        stateUseCase.put(State.CREATE_MEETING, UseCase.CREATE_MEETING);
        stateUseCase.put(State.VIEW_MEETINGS, UseCase.VIEW_MEETINGS);
        stateUseCase.put(State.DELETE_MEETING, UseCase.DELETE_MEETING);
        stateUseCase.put(State.UPDATE_MEETING, UseCase.UPDATE_MEETING);

        Map<IState, IState> transitionFromUseCase = new HashMap<>();
        transitionFromUseCase.put(State.CREATE_MEETING, State.HELP);
        transitionFromUseCase.put(State.DELETE_MEETING, State.HELP);
        transitionFromUseCase.put(State.UPDATE_MEETING, State.HELP);
        transitionFromUseCase.put(State.VIEW_MEETINGS, State.HELP);

        logger.info("Таблица переходов успешно создана");
        return new BaseTransitionTable(table, stateText, stateTransitions, stateUseCase, transitionFromUseCase, State.START);
    }

    private enum State implements IState {
        START, HELP, CREATE_MEETING, DELETE_MEETING, VIEW_MEETINGS, UPDATE_MEETING,
    }
}