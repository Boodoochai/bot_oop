import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

final public class Client {
    private @NonNull final UUID clientId;

    Client(@NonNull final UUID uuid) {
        clientId = uuid;
    }

    public @NonNull UUID getUUID() {
        return clientId;
    }
}
