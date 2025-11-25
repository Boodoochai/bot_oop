import java.util.UUID;

public class Client {
    private final UUID clientId;

    Client(UUID uuid) {
        clientId = uuid;
    }

    public UUID getUUID() {
        return clientId;
    }
}
