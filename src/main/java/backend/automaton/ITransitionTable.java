package backend.automaton;

import backend.useCases.UseCase;

public interface ITransitionTable {
    String[][] getTransitions(IState state);

    IState getNextState(IState state, String symbol);

    String getStateText(IState state);

    IState getInitialState();

    UseCase getUseCase(IState state);

    IState getTransitionFromUseCase(IState state);
}
