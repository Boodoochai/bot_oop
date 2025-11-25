import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleDataStorageTest {

    private SimpleDataStorage storage;

    @BeforeEach
    void setUp() {
        storage = new SimpleDataStorage();
    }

    @Test
    void testPutAndGetClientById() {
        UUID uuid = UUID.randomUUID();
        Client client = new Client(uuid);

        storage.putClientById(uuid, client);

        assertSame(client, storage.clientById(uuid));
    }

    @Test
    void testPutAndGetClientByName() {
        String name = "Alice";
        UUID uuid = UUID.randomUUID();
        Client client = new Client(uuid);

        storage.putClientByName(name, client);

        assertSame(client, storage.clientByName(name));
    }

    @Test
    void testIsExistClientById() {
        UUID uuid = UUID.randomUUID();
        Client client = new Client(uuid);

        assertFalse(storage.isExistClientById(uuid));

        storage.putClientById(uuid, client);

        assertTrue(storage.isExistClientById(uuid));
    }

    @Test
    void testIsExistClientByName() {
        String name = "Bob";
        UUID uuid = UUID.randomUUID();
        Client client = new Client(uuid);

        assertFalse(storage.isExistClientByName(name));

        storage.putClientByName(name, client);

        assertTrue(storage.isExistClientByName(name));
    }

    @Test
    void testGetNonExistingClientByIdReturnsNull() {
        UUID uuid = UUID.randomUUID();

        assertNull(storage.clientById(uuid));
    }

    @Test
    void testGetNonExistingClientByNameReturnsNull() {
        assertNull(storage.clientByName("Unknown"));
    }

    @Test
    void testOverwriteClientById() {
        UUID uuid = UUID.randomUUID();
        Client client1 = new Client(uuid);
        Client client2 = new Client(uuid);

        storage.putClientById(uuid, client1);
        storage.putClientById(uuid, client2);

        assertSame(client2, storage.clientById(uuid));
    }

    @Test
    void testOverwriteClientByName() {
        String name = "Charlie";
        Client client1 = new Client(UUID.randomUUID());
        Client client2 = new Client(UUID.randomUUID());

        storage.putClientByName(name, client1);
        storage.putClientByName(name, client2);

        assertSame(client2, storage.clientByName(name));
    }

    @Test
    void testTwoDifferentClientsWithDifferentIds() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Client c1 = new Client(id1);
        Client c2 = new Client(id2);

        storage.putClientById(id1, c1);
        storage.putClientById(id2, c2);

        assertSame(c1, storage.clientById(id1));
        assertSame(c2, storage.clientById(id2));
    }

    @Test
    void testTwoDifferentClientsWithDifferentNames() {
        Client c1 = new Client(UUID.randomUUID());
        Client c2 = new Client(UUID.randomUUID());

        storage.putClientByName("x", c1);
        storage.putClientByName("y", c2);

        assertSame(c1, storage.clientByName("x"));
        assertSame(c2, storage.clientByName("y"));
    }
}
