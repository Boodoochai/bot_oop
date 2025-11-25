import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsoleTalkerTest {

    private IDataStorage dataStorage;
    private IRequestHandler requestHandler;

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        dataStorage = mock(IDataStorage.class);
        requestHandler = mock(IRequestHandler.class);

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);
        System.setIn(originalIn);
        outContent.close();
    }

    @Test
    void runProcessesSingleRequestAndQuits() {
        String userInput = "Alice\nHello\nquit\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        Client mockClient = new Client(UUID.randomUUID());
        when(dataStorage.isExistClientByName("Alice")).thenReturn(true);
        when(dataStorage.clientByName("Alice")).thenReturn(mockClient);

        Response mockResponse = new Response("OK");
        when(requestHandler.handleRequest(any(Request.class))).thenReturn(mockResponse);

        ConsoleTalker talker = new ConsoleTalker(dataStorage, requestHandler);

        talker.run();

        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(requestHandler).handleRequest(captor.capture());

        Request capturedRequest = captor.getValue();
        assertEquals("Hello", capturedRequest.getText());
        assertEquals(mockClient, capturedRequest.getRequestOwner());

        String output = outContent.toString();
        assertTrue(output.contains("Enter your name or \"quit\" to quit:"));
        assertTrue(output.contains("Enter your request:"));
        assertTrue(output.contains("Generating response..."));
        assertTrue(output.contains("Response:"));
        assertTrue(output.contains("OK"));
    }

    @Test
    void runQuitsImmediately() {
        String userInput = "quit\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        ConsoleTalker talker = new ConsoleTalker(dataStorage, requestHandler);
        talker.run();

        verify(requestHandler, never()).handleRequest(any());

        String output = outContent.toString();
        assertTrue(output.contains("Enter your name or \"quit\" to quit:"));
    }
}
