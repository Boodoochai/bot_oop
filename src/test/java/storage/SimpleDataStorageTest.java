package storage;

import backend.automaton.IAutomaton;
import backend.automaton.base.BaseAutomatonFactory;
import backend.automaton.base.BaseTransitionTableFactory;
import backend.useCases.handlers.CreateMeetingHandler;
import backend.useCases.handlers.IUseCaseHandler;
import model.Client;
import model.Meeting;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleDataStorageTest {

    @Test
    void addUpdateDeleteMeetingLifecycle() {
        SimpleDataStorage storage = new SimpleDataStorage();
        Client owner = new Client(UUID.randomUUID(), "alice");
        Client other = new Client(UUID.randomUUID(), "bob");
        storage.putClientById(owner.clientId(), owner);
        storage.putClientById(other.clientId(), other);

        LocalDateTime start = LocalDateTime.of(2099, 12, 31, 10, 0);
        LocalDateTime end = LocalDateTime.of(2099, 12, 31, 11, 0);
        Meeting meeting = new Meeting("Standup", start, end, Set.of(owner, other), "");
        storage.addMeeting(meeting);

        assertEquals(1, storage.getMeetingsWithClient(owner).size());
        assertEquals(1, storage.getMeetingsForClientBetween(owner, start.minusHours(1), end.plusHours(1)).size());

        Meeting updated = new Meeting(meeting.uuid(), "Updated", start.plusHours(1), end.plusHours(1), Set.of(owner, other), "desc");
        assertTrue(storage.updateMeeting(updated));

        Meeting stored = storage.getMeetingsWithClient(owner).get(0);
        assertEquals("Updated", stored.title());
        assertEquals(start.plusHours(1), stored.start());
        assertEquals(end.plusHours(1), stored.end());

        assertTrue(storage.deleteMeeting(updated.uuid()));
        assertEquals(0, storage.getMeetingsWithClient(owner).size());
    }

    @Test
    void storesAutomatonAndUseCaseHandlerPerClient() {
        SimpleDataStorage storage = new SimpleDataStorage();
        Client owner = new Client(UUID.randomUUID(), "alice");
        storage.putClientById(owner.clientId(), owner);

        IAutomaton automaton = new BaseAutomatonFactory(new BaseTransitionTableFactory()).createAutomaton();
        storage.setAutomation(owner.clientId(), automaton);
        assertTrue(storage.isExistAutomation(owner.clientId()));
        assertNotNull(storage.getAutomation(owner.clientId()));

        IUseCaseHandler handler = new CreateMeetingHandler();
        storage.setUseCaseHandler(owner.clientId(), handler);
        assertTrue(storage.isExistUseCaseHandler(owner.clientId()));
        assertNotNull(storage.getUseCaseHandler(owner.clientId()));
        storage.deleteUseCaseHandler(owner.clientId());
        assertFalse(storage.isExistUseCaseHandler(owner.clientId()));
    }
}
