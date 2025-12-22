package backend.automaton;

import backend.useCases.UseCase;

import java.util.List;

public interface IAutomaton {
    String[][] getOptions();

    void next(String input);

    String getStateText();

    UseCase getUseCase();

    void useCaseDone();
}
