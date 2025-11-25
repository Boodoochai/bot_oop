import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DemoRequestHandlerTest {

    @Test
    void handleRequestShouldReturnResponseWithHeh() {
        DemoRequestHandler handler = new DemoRequestHandler();
        Request request = mock(Request.class);

        Response response = handler.handleRequest(request);

        assertNotNull(response, "Response не должен быть null");
    }

    @Test
    void handleRequestShouldReturnNewInstanceEveryTime() {
        DemoRequestHandler handler = new DemoRequestHandler();
        Request request = mock(Request.class);

        Response r1 = handler.handleRequest(request);
        Response r2 = handler.handleRequest(request);

        assertNotSame(r1, r2, "Каждый вызов должен возвращать новый объект Response");
    }

    @Test
    void handleRequestWorksWithNullRequest() {
        DemoRequestHandler handler = new DemoRequestHandler();

        Response response = handler.handleRequest(null);

        assertNotNull(response);
    }
}
