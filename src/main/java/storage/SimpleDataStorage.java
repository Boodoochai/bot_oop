package storage;

import model.Client;
import model.Meeting;

import java.time.LocalDateTime;
import java.util.*;

final public class SimpleDataStorage implements IDataStorage {
    private final Map<UUID, Client> clientByUUID;
    private final Map<String, Client> clientByName;
    private final List<Meeting> meetings;

    public SimpleDataStorage() {
        clientByUUID = new HashMap<>();
        clientByName = new HashMap<>();
        meetings = new ArrayList<>();
    }

    @Override
    public Client clientById(final UUID clientUUID) {
        return clientByUUID.get(clientUUID);
    }

    @Override
    public Client clientByName(final String clientName) {
        return clientByName.get(clientName);
    }

    @Override
    public boolean isExistClientById(final UUID clientUUID) {
        return clientByUUID.containsKey(clientUUID);
    }

    @Override
    public boolean isExistClientByName(final String clientName) {
        return clientByName.containsKey(clientName);
    }

    @Override
    public void putClientById(final UUID uuid, final Client client) {
        clientByUUID.put(uuid, client);
    }

    @Override
    public void putClientByName(final String name, final Client client) {
        clientByName.put(name, client);
    }

    @Override
    public void addMeeting(final Meeting meeting) {
        meetings.add(meeting);
    }

    @Override
    public List<Meeting> getMeetingsBetween(
            final LocalDateTime from,
            final LocalDateTime to
    ) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (!m.end().isBefore(from) && !m.start().isAfter(to)) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public List<Meeting> getMeetingsForClientBetween(
            final Client client,
            final LocalDateTime from,
            final LocalDateTime to
    ) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.participants().contains(client)
                    && !m.end().isBefore(from)
                    && !m.start().isAfter(to)) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public List<Meeting> getMeetingsWithClient(final Client client) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.participants().contains(client)) {
                result.add(m);
            }
        }
        return result;
    }
}
