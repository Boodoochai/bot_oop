package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import backend.automaton.CalendarAutomaton;
import model.Client;
import model.Request;
import model.Response;
import storage.IDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BaseRequestHandler extends IRequestHandler {
    private final Map<UUID, CalendarAutomaton> machines = new HashMap<>();

    public BaseRequestHandler(IDataStorage dataStorage,
                              ClientIdentificationHandler clientIdentificationHandler) {
        super(dataStorage, clientIdentificationHandler);
    }

    @Override
    public Response handleRequest(final Request request) {
        Client owner = request.requestOwner();

        CalendarAutomaton machine = machines.computeIfAbsent(
                owner.clientId(),
                id -> new CalendarAutomaton(dataStorage, clientIdentificationHandler, owner)
        );

        return machine.feed(request.text());
    }
}
