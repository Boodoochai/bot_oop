import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        if (! clientId.equals(client.clientId)) return false;
        return name.equals(client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, name);
    }
}
