package backend.automaton;

import backend.automaton.base.BaseAutomatonFactory;
import backend.automaton.base.BaseTransitionTableFactory;
import backend.useCases.UseCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseAutomatonFlowTest {

    @Test
    void helpToCreateMeetingTriggersUseCase() {
        IAutomaton automaton = new BaseAutomatonFactory(new BaseTransitionTableFactory()).createAutomaton();

        assertNull(automaton.getUseCase());

        automaton.next("Помощь");
        assertNull(automaton.getUseCase());

        automaton.next("Создать встречу");
        assertEquals(UseCase.CREATE_MEETING, automaton.getUseCase());
    }

    @Test
    void useCaseDoneReturnsToHelpState() {
        IAutomaton automaton = new BaseAutomatonFactory(new BaseTransitionTableFactory()).createAutomaton();

        automaton.next("Помощь");
        automaton.next("Удалить встречу");
        assertEquals(UseCase.DELETE_MEETING, automaton.getUseCase());

        automaton.useCaseDone();

        assertNull(automaton.getUseCase());
        assertTrue(flatten(automaton.getOptions()).contains("Создать встречу"));
    }

    private static List<String> flatten(String[][] options) {
        if (options == null) {
            return List.of();
        }
        return Stream.of(options).flatMap(Arrays::stream).toList();
    }
}
