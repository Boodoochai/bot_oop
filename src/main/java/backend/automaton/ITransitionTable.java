package backend.automaton;

public interface ITransitionTable {
    String[][] getTransitions(IState state);

    IState getNextState(IState state, String symbol);

    String getStateText(IState state);

    IState getInitialState();
}
