import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

class ClientIdentificationHandlerTest {
    // TEST: CREATE NEW BY NAME
    @Test
    @DisplayName("Создание нового клиента по имени, если его нет")
    void getClientByName_createsNewClientIfNotExist() {
        IDataStorage storage = mock(IDataStorage.class);

        Map<String, Client> nameMap = new HashMap<>();

        when(storage.isExistClientByName(anyString()))
                .thenAnswer(inv -> nameMap.containsKey(inv.getArgument(0)));

        when(storage.clientByName(anyString()))
                .thenAnswer(inv -> nameMap.get(inv.getArgument(0)));

        doAnswer(inv -> {
            String name = inv.getArgument(0);
            Client client = inv.getArgument(1);
            nameMap.put(name, client);
            return null;
        }).when(storage).putClientByName(anyString(), any(Client.class));

        Client cl = ClientIdentificationHandler.getClient(storage, "alice");

        assertNotNull(cl);
        assertTrue(nameMap.containsKey("alice"));
    }

    // TEST: EXISTING CLIENT BY NAME
    @Test
    @DisplayName("Если клиент уже существует по имени — не создаётся новый")
    void getClientByName_returnsExistingIfExist() {
        IDataStorage storage = mock(IDataStorage.class);

        Map<String, Client> nameMap = new HashMap<>();

        Client existing = new Client(UUID.randomUUID());
        nameMap.put("bob", existing);

        when(storage.isExistClientByName("bob")).thenReturn(true);
        when(storage.clientByName("bob")).thenReturn(existing);

        Client cl = ClientIdentificationHandler.getClient(storage, "bob");

        assertSame(existing, cl);
        verify(storage, never()).putClientByName(anyString(), any());
    }

    // TEST: SAME CLIENT ON REPEATED CALL
    @Test
    @DisplayName("Повторный вызов getClient(name) возвращает того же клиента")
    void getClientByName_returnsSameOnSecondCall() {
        IDataStorage storage = mock(IDataStorage.class);

        Map<String, Client> nameMap = new HashMap<>();

        when(storage.isExistClientByName(anyString()))
                .thenAnswer(inv -> nameMap.containsKey(inv.getArgument(0)));
        when(storage.clientByName(anyString()))
                .thenAnswer(inv -> nameMap.get(inv.getArgument(0)));

        doAnswer(inv -> {
            nameMap.put(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(storage).putClientByName(anyString(), any(Client.class));

        Client c1 = ClientIdentificationHandler.getClient(storage, "kek");
        Client c2 = ClientIdentificationHandler.getClient(storage, "kek");

        assertSame(c1, c2);
    }

    // TEST: CREATE NEW BY UUID
    @Test
    @DisplayName("Создание нового клиента по UUID, если его нет")
    void getClientByUUID_createsNewClientIfNotExist() {
        IDataStorage storage = mock(IDataStorage.class);

        Map<UUID, Client> idMap = new HashMap<>();

        when(storage.isExistClientById(any(UUID.class)))
                .thenAnswer(inv -> idMap.containsKey(inv.getArgument(0)));
        when(storage.clientById(any(UUID.class)))
                .thenAnswer(inv -> idMap.get(inv.getArgument(0)));

        doAnswer(inv -> {
            idMap.put(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(storage).putClientById(any(UUID.class), any(Client.class));

        UUID id = UUID.randomUUID();

        Client c = ClientIdentificationHandler.getClient(storage, id);

        assertNotNull(c);
        assertTrue(idMap.containsKey(id));
    }

    // TEST: EXISTING CLIENT BY UUID
    @Test
    @DisplayName("Если клиент уже существует по UUID — возвращается существующий")
    void getClientByUUID_returnsExistingIfExist() {
        IDataStorage storage = mock(IDataStorage.class);

        Map<UUID, Client> idMap = new HashMap<>();

        UUID id = UUID.randomUUID();
        Client existing = new Client(id);
        idMap.put(id, existing);

        when(storage.isExistClientById(id)).thenReturn(true);
        when(storage.clientById(id)).thenReturn(existing);

        Client c = ClientIdentificationHandler.getClient(storage, id);

        assertSame(existing, c);
        verify(storage, never()).putClientById(any(UUID.class), any());
    }

    // TEST: SAME CLIENT ON REPEATED CALL (UUID)
    @Test
    @DisplayName("Повторный вызов getClient(uuid) возвращает того же клиента")
    void getClientByUUID_returnsSameOnSecondCall() {
        IDataStorage storage = mock(IDataStorage.class);

        Map<UUID, Client> idMap = new HashMap<>();

        when(storage.isExistClientById(any(UUID.class)))
                .thenAnswer(inv -> idMap.containsKey(inv.getArgument(0)));
        when(storage.clientById(any(UUID.class)))
                .thenAnswer(inv -> idMap.get(inv.getArgument(0)));
        doAnswer(inv -> {
            idMap.put(inv.getArgument(0), inv.getArgument(1));
            return null;
        }).when(storage).putClientById(any(UUID.class), any(Client.class));

        UUID id = UUID.randomUUID();

        Client c1 = ClientIdentificationHandler.getClient(storage, id);
        Client c2 = ClientIdentificationHandler.getClient(storage, id);

        assertSame(c1, c2);
    }
}
