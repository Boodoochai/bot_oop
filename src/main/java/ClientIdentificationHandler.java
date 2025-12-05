import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

final class ClientIdentificationHandler {
    private ClientIdentificationHandler(){}
    private static void registerNewClient(@NonNull final IDataStorage dataStorage, @NonNull final String name) {
        @NonNull final UUID newClientUUID = UUID.randomUUID();
        @NonNull final Client newClient = new Client(newClientUUID);
        dataStorage.putClientByName(name, newClient);
    }

    private static void registerNewClient(@NonNull final IDataStorage dataStorage, @NonNull final UUID uuid) {
        @NonNull final Client newClient = new Client(uuid);
        dataStorage.putClientById(uuid, newClient);
    }

    public static @NonNull Client getClient(@NonNull final IDataStorage dataStorage, @NonNull final String name) {
        if (!dataStorage.isExistClientByName(name)) {
            registerNewClient(dataStorage, name);
        }
        return dataStorage.clientByName(name);
    }

    public static @NonNull Client getClient(@NonNull final IDataStorage dataStorage, @NonNull final UUID uuid) {
        if (!dataStorage.isExistClientById(uuid)) {
            registerNewClient(dataStorage, uuid);
        }
        return dataStorage.clientById(uuid);
    }
}