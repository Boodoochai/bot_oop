import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface IDataStorage {
    @NonNull Client clientById(@NonNull final UUID clientUUID);

    @NonNull Client clientByName(@NonNull final String clientName);

    boolean isExistClientById(@NonNull final UUID clientUUID);

    boolean isExistClientByName(@NonNull final String clientName);

    void putClientById(@NonNull final UUID uuid, @NonNull final Client client);

    void putClientByName(@NonNull final String name, @NonNull final Client client);
}
