import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

final public class SimpleDataStorage implements IDataStorage {
    private @NonNull final Map<UUID, Client> clientByUUID;
    private @NonNull final Map<String, Client> clientByName;

    public SimpleDataStorage() {
        clientByUUID = new HashMap<>();
        clientByName = new HashMap<>();
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
}
