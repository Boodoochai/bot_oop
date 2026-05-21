package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
import model.Client;
import model.Meeting;
import model.Request;
import org.junit.jupiter.api.Test;
import storage.SimpleDataStorage;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateMeetingHandlerTest {

    @Test
    void updatesMeetingWithNoChanges() {
        SimpleDataStorage storage = new SimpleDataStorage();
        ClientIdentificationHandler idHandler = new ClientIdentificationHandler(storage);
        Client owner = idHandler.getClient("alice");
        Client bob = idHandler.getClient("bob");

        LocalDateTime start = LocalDateTime.of(2099, 12, 31, 10, 0);
        Meeting meeting = new Meeting(UUID.randomUUID(), "Demo", start, start.plusHours(1), Set.of(owner, bob), "desc");
        storage.addMeeting(meeting);

        UpdateMeetingHandler handler = new UpdateMeetingHandler();
        handler.handleRequest(new Request(owner, ""), storage, idHandler);
        handler.handleRequest(new Request(owner, "1"), storage, idHandler);
        handler.handleRequest(new Request(owner, "Без изменений"), storage, idHandler);
        handler.handleRequest(new Request(owner, "Без изменений"), storage, idHandler);
        handler.handleRequest(new Request(owner, "Без изменений"), storage, idHandler);
        handler.handleRequest(new Request(owner, "Без изменений"), storage, idHandler);
        handler.handleRequest(new Request(owner, "Без изменений"), storage, idHandler);

        assertTrue(handler.isDone());
        Meeting updated = storage.getMeetingsWithClient(owner).get(0);
        assertEquals(meeting.uuid(), updated.uuid());
        assertEquals(meeting.title(), updated.title());
        assertEquals(meeting.start(), updated.start());
        assertEquals(meeting.end(), updated.end());
        assertEquals(meeting.participants(), updated.participants());
    }
}
