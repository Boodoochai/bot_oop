import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.*;

final public class SimpleDataStorage implements IDataStorage {
    private @NonNull final Map<UUID, Client> clientByUUID;
    private @NonNull final Map<String, Client> clientByName;
    private @NonNull final List<Meeting> meetings;

    public SimpleDataStorage() {
        clientByUUID = new HashMap<>();
        clientByName = new HashMap<>();
        meetings = new ArrayList<>();
    }

    @Override
    public @NonNull Client clientById(@NonNull final UUID clientUUID) {
        return clientByUUID.get(clientUUID);
    }

    @Override
    public @NonNull Client clientByName(@NonNull final String clientName) {
        return clientByName.get(clientName);
    }

    @Override
    public boolean isExistClientById(@NonNull final UUID clientUUID) {
        return clientByUUID.containsKey(clientUUID);
    }

    @Override
    public boolean isExistClientByName(@NonNull final String clientName) {
        return clientByName.containsKey(clientName);
    }

    @Override
    public void putClientById(@NonNull final UUID uuid, @NonNull final Client client) {
        clientByUUID.put(uuid, client);
    }

    @Override
    public void putClientByName(@NonNull final String name, @NonNull final Client client) {
        clientByName.put(name, client);
    }

    @Override
    public void addMeeting(@NonNull final Meeting meeting) {
        meetings.add(meeting);
    }

    @Override
    public @NonNull List<Meeting> getMeetingsBetween(
            @NonNull final LocalDateTime from,
            @NonNull final LocalDateTime to
    ) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (!m.getEnd().isBefore(from) && !m.getStart().isAfter(to)) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public @NonNull List<Meeting> getMeetingsForClientBetween(
            @NonNull final Client client,
            @NonNull final LocalDateTime from,
            @NonNull final LocalDateTime to
    ) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.getParticipants().contains(client)
                    && !m.getEnd().isBefore(from)
                    && !m.getStart().isAfter(to)) {
                result.add(m);
            }
        }
        return result;
    }

    @Override
    public @NonNull List<Meeting> getMeetingsWithClient(@NonNull final Client client) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.getParticipants().contains(client)) {
                result.add(m);
            }
        }
        return result;
    }
}
