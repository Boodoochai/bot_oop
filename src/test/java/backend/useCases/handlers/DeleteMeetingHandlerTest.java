package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
import model.Client;
import model.Meeting;
import model.Request;
import org.junit.jupiter.api.Test;
import storage.SimpleDataStorage;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteMeetingHandlerTest {

    @Test
    void deletesSelectedMeeting() {
        SimpleDataStorage storage = new SimpleDataStorage();
        ClientIdentificationHandler idHandler = new ClientIdentificationHandler(storage);
        Client owner = idHandler.getClient("alice");
        Client bob = idHandler.getClient("bob");

        LocalDateTime start = LocalDateTime.of(2099, 12, 31, 10, 0);
        Meeting meeting = new Meeting("Demo", start, start.plusHours(1), Set.of(owner, bob), "");
        storage.addMeeting(meeting);

        DeleteMeetingHandler handler = new DeleteMeetingHandler();
        handler.handleRequest(new Request(owner, ""), storage, idHandler);
        handler.handleRequest(new Request(owner, "1"), storage, idHandler);
        handler.handleRequest(new Request(owner, "Да"), storage, idHandler);

        assertTrue(handler.isDone());
        assertEquals(0, storage.getMeetingsWithClient(owner).size());
    }
}
