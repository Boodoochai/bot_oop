import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

final public class Client {
    private @NonNull final UUID clientId;
    private @NonNull final String name;


    Client(@NonNull final UUID uuid, @NonNull final String name) {
        this.clientId = uuid;
        this.name = name;
    }

    public @NonNull UUID getUUID() {
        return clientId;
    }
    public @NonNull String getName() { return name; }
}
