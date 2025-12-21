package Identification;

import model.Client;
import storage.IDataStorage;

import java.util.UUID;

final public class ClientIdentificationHandler {
    private
    final IDataStorage dataStorage;

    public ClientIdentificationHandler(final IDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    private void registerNewClient(final String name) {
        final UUID newClientUUID = UUID.randomUUID();
        final Client newClient = new Client(newClientUUID, name);
        dataStorage.putClientByName(name, newClient);
    }

    private void registerNewClient(final UUID uuid) {
        final Client newClient = new Client(uuid, "");
        dataStorage.putClientById(uuid, newClient);
    }

    public Client getClient(final String name) {
        if (!dataStorage.isExistClientByName(name)) {
            registerNewClient(name);
        }
        return dataStorage.clientByName(name);
    }

    public Client getClient(final UUID uuid) {
        if (!dataStorage.isExistClientById(uuid)) {
            registerNewClient(uuid);
        }
        return dataStorage.clientById(uuid);
    }
}