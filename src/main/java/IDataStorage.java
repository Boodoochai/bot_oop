import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IDataStorage {
    @NonNull Client clientById(@NonNull final UUID clientUUID);

    @NonNull Client clientByName(@NonNull final String clientName);

    boolean isExistClientById(@NonNull final UUID clientUUID);

    boolean isExistClientByName(@NonNull final String clientName);

    void putClientById(@NonNull final UUID uuid, @NonNull final Client client);

    void putClientByName(@NonNull final String name, @NonNull final Client client);

    void addMeeting(@NonNull final Meeting meeting);

    @NonNull List<Meeting> getMeetingsBetween(
            @NonNull final LocalDateTime from,
            @NonNull final LocalDateTime to
    );

    @NonNull List<Meeting> getMeetingsForClientBetween(
            @NonNull final Client client,
            @NonNull final LocalDateTime from,
            @NonNull final LocalDateTime to
    );

    @NonNull List<Meeting> getMeetingsWithClient(
            @NonNull final Client client
    );

}
