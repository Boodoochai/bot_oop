import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaseRequestHandler extends IRequestHandler {
    private final Map<UUID, CalendarAutomaton> machines = new HashMap<>();

    public BaseRequestHandler(@NonNull IDataStorage dataStorage,
                              @NonNull ClientIdentificationHandler clientIdentificationHandler) {
        super(dataStorage, clientIdentificationHandler);
    }

    @Override
    public @NonNull Response handleRequest(@NonNull final Request request) {
        Client owner = request.getRequestOwner();

        CalendarAutomaton machine = machines.computeIfAbsent(
                owner.getUUID(),
                id -> new CalendarAutomaton(dataStorage, clientIdentificationHandler, owner)
        );

        return machine.feed(request.getText());
    }
}
