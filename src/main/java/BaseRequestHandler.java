import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BaseRequestHandler implements IRequestHandler {

    private final @NonNull IDataStorage dataStorage;
    private final @NonNull ClientIdentificationHandler clientIdentificationHandler;

    // По одному автомату на каждого клиента
    private final Map<UUID, CalendarAutomaton> machines = new HashMap<>();

    public BaseRequestHandler(@NonNull IDataStorage dataStorage,
                              @NonNull ClientIdentificationHandler clientIdentificationHandler) {
        this.dataStorage = dataStorage;
        this.clientIdentificationHandler = clientIdentificationHandler;
    }

    @Override
    public @NonNull Response handleRequest(@NonNull final Request request) {
        Client owner = request.getRequestOwner();

        // Берём автомат клиента, либо создаём новый
        CalendarAutomaton machine = machines.computeIfAbsent(
                owner.getUUID(),
                id -> new CalendarAutomaton(dataStorage, clientIdentificationHandler, owner)
        );

        // Кормим(вкусно) автомат входной строкой — получаем ответ
        return machine.feed(request.getText());
    }
}
