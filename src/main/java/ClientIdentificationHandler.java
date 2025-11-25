import java.util.UUID;

class ClientIdentificationHandler {
    private ClientIdentificationHandler(){}
    private static void registerNewClient(IDataStorage dataStorage, String name) {
        UUID newClientUUID = UUID.randomUUID();
        Client newClient = new Client(newClientUUID);
        dataStorage.putClientByName(name, newClient);
    }

    private static void registerNewClient(IDataStorage dataStorage, UUID uuid) {
        Client newClient = new Client(uuid);
        dataStorage.putClientById(uuid, newClient);
    }

    public static Client getClient(IDataStorage dataStorage, String name) {
        if (!dataStorage.isExistClientByName(name)) {
            registerNewClient(dataStorage, name);
        }
        return dataStorage.clientByName(name);
    }

    public static Client getClient(IDataStorage dataStorage, UUID uuid) {
        if (!dataStorage.isExistClientById(uuid)) {
            registerNewClient(dataStorage, uuid);
        }
        return dataStorage.clientById(uuid);
    }
}