import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleDataStorage implements IDataStorage {
    private final Map<UUID, Client> clientByUUID;
    private final Map<String, Client> clientByName;

    public SimpleDataStorage() {
        clientByUUID = new HashMap<>();
        clientByName = new HashMap<>();
    }

    @Override
    public Client clientById(UUID clientUUID) {
        return clientByUUID.get(clientUUID);
    }

    @Override
    public Client clientByName(String clientName) {
        return clientByName.get(clientName);
    }

    @Override
    public boolean isExistClientById(UUID clientUUID) {
        return clientByUUID.containsKey(clientUUID);
    }

    @Override
    public boolean isExistClientByName(String clientName) {
        return clientByName.containsKey(clientName);
    }

    @Override
    public void putClientById(UUID uuid, Client client) {
        clientByUUID.put(uuid, client);
    }

    @Override
    public void putClientByName(String name, Client client) {
        clientByName.put(name, client);
    }
}
