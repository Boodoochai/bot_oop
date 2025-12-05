import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

final public class ClientIdentificationHandler {
    private @NonNull final IDataStorage dataStorage;
    public ClientIdentificationHandler(@NonNull final IDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }
    private void registerNewClient(@NonNull final String name) {
        @NonNull final UUID newClientUUID = UUID.randomUUID();
        @NonNull final Client newClient = new Client(newClientUUID, name);
        dataStorage.putClientByName(name, newClient);
    }

    private void registerNewClient(@NonNull final UUID uuid) {
        @NonNull final Client newClient = new Client(uuid, "");
        dataStorage.putClientById(uuid, newClient);
    }

    public @NonNull Client getClient(@NonNull final String name) {
        if (!dataStorage.isExistClientByName(name)) {
            registerNewClient(name);
        }
        return dataStorage.clientByName(name);
    }

    public @NonNull Client getClient(@NonNull final UUID uuid) {
        if (!dataStorage.isExistClientById(uuid)) {
            registerNewClient(uuid);
        }
        return dataStorage.clientById(uuid);
    }
}