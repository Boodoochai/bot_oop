package backend.useCases.providers;

import backend.useCases.UseCase;
import backend.useCases.handlers.*;

public class BaseUseCaseProvider implements IUseCaseProvider {
    @Override
    public IUseCaseHandler getUseCaseHandler(UseCase useCase) {
        return switch (useCase) {
            case CREATE_MEETING -> new CreateMeetingHandler();
            case DELETE_MEETING -> new DeleteMeetingHandler();
            case UPDATE_MEETING -> new UpdateMeetingHandler();
            case VIEW_MEETINGS -> new ViewMeetingsHandler();
        };
    }
}
