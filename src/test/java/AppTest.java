import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class AppTest {

    @Test
    void runShouldCallTalkerRun() {
        AbstractTalker talkerMock = mock(AbstractTalker.class);
        App app = new App(talkerMock);

        app.run();

        verify(talkerMock, times(1)).run();
    }
}
