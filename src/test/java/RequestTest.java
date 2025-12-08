import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestTest {

    @Test
    void constructorShouldStoreOwnerAndText() {
        Client client = mock(Client.class);
        Request request = new Request(client, "Hello");

        assertEquals(client, request.getRequestOwner());
        assertEquals("Hello", request.getText());
    }

    @Test
    void constructorAllowsNullClient() {
        Request request = new Request(null, "Text");
        assertNull(request.getRequestOwner());
        assertEquals("Text", request.getText());
    }

    @Test
    void constructorAllowsNullText() {
        Client client = mock(Client.class);
        Request request = new Request(client, null);

        assertEquals(client, request.getRequestOwner());
        assertNull(request.getText());
    }

    @Test
    void getRequestOwnerShouldReturnSameInstance() {
        Client client = mock(Client.class);
        Request request = new Request(client, "Hi");

        assertSame(client, request.getRequestOwner());
    }

    @Test
    void multipleRequestsShouldStoreIndependentValues() {
        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);

        Request r1 = new Request(client1, "A");
        Request r2 = new Request(client2, "B");

        assertEquals(client1, r1.getRequestOwner());
        assertEquals("A", r1.getText());

        assertEquals(client2, r2.getRequestOwner());
        assertEquals("B", r2.getText());
    }
}
