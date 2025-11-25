import java.util.UUID;

public interface IDataStorage {

    Client clientById(final UUID clientUUID);

    Client clientByName(final String clientName);

    boolean isExistClientById(final UUID clientUUID);

    boolean isExistClientByName(final String clientName);

    void putClientById(final UUID uuid, final Client client);

    void putClientByName(final String name, final Client client);
}
