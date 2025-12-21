package backend.automaton;

import java.util.List;

public interface ITransitionTable {
    List<String> getAcceptableSymbols(IState state);

    IState getNextState(IState state, String symbol);

    String getStateText(IState state);

    IState getInitialState();
}
