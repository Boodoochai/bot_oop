package backend.automaton;

import java.util.List;

public interface IAutomaton {
    String[][] getOptions();

    void next(String input);

    String getStateText();
}
