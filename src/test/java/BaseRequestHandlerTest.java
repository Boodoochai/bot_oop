import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaseRequestHandlerTest {

    @Test
    void handleRequestShouldReturnNonNullResponse() {
        BaseRequestHandler handler = new BaseRequestHandler();
        Request request = new Request(null, "any text");

        Response response = handler.handleRequest(request);

        assertNotNull(response, "Response не должен быть null");
    }

    @Test
    void handleRequestShouldReturnResponseWithHeh() {
        BaseRequestHandler handler = new BaseRequestHandler();
        Request request = new Request(null, "something");

        Response response = handler.handleRequest(request);

        assertEquals("heh", response.getText(), "Response должен содержать текст 'heh'");
    }

    @Test
    void handleRequestReturnsNewInstanceEveryTime() {
        BaseRequestHandler handler = new BaseRequestHandler();
        Request request = new Request(null, "test");

        Response r1 = handler.handleRequest(request);
        Response r2 = handler.handleRequest(request);

        assertNotSame(r1, r2, "Каждый вызов должен возвращать новый объект Response");
    }

    @Test
    void handleRequestWorksWithNullRequest() {
        BaseRequestHandler handler = new BaseRequestHandler();

        Response response = handler.handleRequest(null);

        assertNotNull(response, "Даже при null Request должен возвращаться Response");
    }
}
