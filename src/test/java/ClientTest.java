import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void constructorShouldStoreUUID() {
        UUID id = UUID.randomUUID();
        Client client = new Client(id);

        assertEquals(id, client.getUUID());
    }

    @Test
    void getUUIDShouldReturnSameInstance() {
        UUID id = UUID.randomUUID();
        Client client = new Client(id);

        assertSame(id, client.getUUID(), "getUUID должен возвращать ту же ссылку на объект UUID");
    }

    @Test
    void multipleClientsHaveIndependentUUIDs() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Client c1 = new Client(id1);
        Client c2 = new Client(id2);

        assertEquals(id1, c1.getUUID());
        assertEquals(id2, c2.getUUID());

        assertNotEquals(c1.getUUID(), c2.getUUID());
    }

    @Test
    void constructorAllowsNullUUID() {
        Client client = new Client(null);
        assertNull(client.getUUID(), "Если UUID передан как null, getUUID должен вернуть null");
    }
}
