package backend.useCases.providers;

import backend.useCases.UseCase;
import backend.useCases.handlers.IUseCaseHandler;

public interface IUseCaseProvider {
    IUseCaseHandler getUseCaseHandler(UseCase useCase);
}
