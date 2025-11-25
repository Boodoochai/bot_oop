import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void constructorShouldStoreText() {
        Response response = new Response("Hello");
        assertEquals("Hello", response.getText());
    }

    @Test
    void constructorShouldAllowEmptyString() {
        Response response = new Response("");
        assertEquals("", response.getText());
    }

    @Test
    void constructorShouldAllowNullText() {
        Response response = new Response(null);
        assertNull(response.getText());
    }

    @Test
    void getTextShouldReturnSameInstance() {
        String source = "data";
        Response response = new Response(source);
        assertSame(source, response.getText());
    }

    @Test
    void multipleInstancesShouldStoreIndependentValues() {
        Response r1 = new Response("A");
        Response r2 = new Response("B");

        assertEquals("A", r1.getText());
        assertEquals("B", r2.getText());
    }
}
