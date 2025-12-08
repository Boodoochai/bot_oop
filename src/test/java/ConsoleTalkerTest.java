import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.*;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ConsoleTalkerTest {

    private ClientIdentificationHandler clientHandler;
    private IRequestHandler requestHandler;
    private ConsoleTalker talker;

    @BeforeEach
    void setUp() {
        clientHandler = mock(ClientIdentificationHandler.class);
        requestHandler = mock(IRequestHandler.class);

        talker = new ConsoleTalker(clientHandler, requestHandler);
    }

    @Test
    void testRunHandlesSingleRequestAndQuit() {
        ByteArrayInputStream testIn = new ByteArrayInputStream(
                ("Alice\nHello\nquit\n").getBytes()
        );
        System.setIn(testIn);

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        Client fakeClient = new Client(UUID.randomUUID(), "Alice");
        when(clientHandler.getClient("Alice")).thenReturn(fakeClient);

        Response fakeResponse = new Response("World!");
        when(requestHandler.handleRequest(any())).thenReturn(fakeResponse);

        talker.run();

        verify(clientHandler).getClient("Alice");
        verify(requestHandler, times(1)).handleRequest(any());

        String console = testOut.toString();

        Assertions.assertTrue(console.contains("Enter your name"));
        Assertions.assertTrue(console.contains("Enter your request"));
        Assertions.assertTrue(console.contains("Generating response"));
        Assertions.assertTrue(console.contains("Response:"));
        Assertions.assertTrue(console.contains("World!"));
    }


    @Test
    void testRunExitsImmediatelyOnQuit() {
        ByteArrayInputStream testIn = new ByteArrayInputStream(
                ("quit\n").getBytes()
        );
        System.setIn(testIn);

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        talker.run();

        verify(requestHandler, never()).handleRequest(any());

        String out = testOut.toString();
        Assertions.assertTrue(out.contains("Enter your name"));
    }
}
