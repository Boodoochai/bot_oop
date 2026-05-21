package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
import model.Client;
import model.Meeting;
import model.Request;
import org.junit.jupiter.api.Test;
import storage.SimpleDataStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateMeetingHandlerTest {

    @Test
    void createsMeetingThroughSteps() {
        SimpleDataStorage storage = new SimpleDataStorage();
        ClientIdentificationHandler idHandler = new ClientIdentificationHandler(storage);
        Client owner = idHandler.getClient("alice");
        CreateMeetingHandler handler = new CreateMeetingHandler();

        handler.handleRequest(new Request(owner, ""), storage, idHandler);
        handler.handleRequest(new Request(owner, "Weekly sync"), storage, idHandler);
        handler.handleRequest(new Request(owner, "31.12.2099"), storage, idHandler);
        handler.handleRequest(new Request(owner, "10:30"), storage, idHandler);
        handler.handleRequest(new Request(owner, "01:00"), storage, idHandler);
        handler.handleRequest(new Request(owner, "bob"), storage, idHandler);

        assertTrue(handler.isDone());
        List<Meeting> meetings = storage.getMeetingsWithClient(owner);
        assertEquals(1, meetings.size());

        Meeting meeting = meetings.get(0);
        assertEquals("Weekly sync", meeting.title());
        assertEquals(LocalDateTime.of(2099, 12, 31, 10, 30), meeting.start());
        assertEquals(LocalDateTime.of(2099, 12, 31, 11, 30), meeting.end());

        Client bob = idHandler.getClient("bob");
        assertTrue(meeting.participants().contains(owner));
        assertTrue(meeting.participants().contains(bob));
    }
}
