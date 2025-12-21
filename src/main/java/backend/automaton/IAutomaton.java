package backend.automaton;

import java.util.List;

public interface IAutomaton {
    List<String> getOptions();

    void next(String input);

    String getStateText();
}
