import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

public class AppTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // -----------------------
    // Test App.run() method
    // -----------------------
    @Test
    void testRunCallsTalker() {
        AbstractTalker talker = mock(AbstractTalker.class);
        App app = new App(talker);

        app.run();

        verify(talker, times(1)).run();
    }

    // -----------------------
    // Test main(): wrong args
    // -----------------------
    @Test
    void testMainNotEnoughArgs() {
        String[] args = new String[] {};

        App.main(args);

        Assertions.assertTrue(outContent.toString().contains("Not enough arguments 2 expected"));
    }

    // -----------------------------------------
    // Test main(): wrong first argument
    // -----------------------------------------
    @Test
    void testMainWrongFirstArg() {
        String[] args = new String[] { "unknown", "console" };

        App.main(args);

        Assertions.assertTrue(outContent.toString().contains("Wrong first argument use \"demo\" or \"base\""));
    }

    // -----------------------------------------
    // Test main(): wrong second argument
    // -----------------------------------------
    @Test
    void testMainWrongSecondArg() {
        String[] args = new String[] { "demo", "unknown" };

        App.main(args);

        Assertions.assertTrue(outContent.toString().contains("Wrong second argument use \"console\" or \"telegram\""));
    }

    // ---------------------------------------------------------
    // Test main(): correct arguments → ConsoleTalker is used
    // ---------------------------------------------------------
    @Test
    void testMainUsesConsoleTalker() {
        AbstractTalker consoleTalker = mock(ConsoleTalker.class);

        try (var mocked = Mockito.mockConstruction(ConsoleTalker.class,
                (mock, context) -> {
                    // Копируем нужное поведение в мок
                }
        )) {
            String[] args = new String[] { "demo", "console" };

            App.main(args);

            // Mockito вызывает mock.run(), поэтому проверяем его
            var constructed = mocked.constructed();
            Assertions.assertEquals(1, constructed.size());

            verify(constructed.get(0), times(1)).run();
        }
    }


    // ---------------------------------------------------------
    // Test main(): correct arguments → TelegramTalker is used
    // ---------------------------------------------------------
    @Test
    void testMainUsesTelegramTalker() {
        try (var mocked = Mockito.mockConstruction(TelegramTalker.class)) {

            String[] args = new String[] { "base", "telegram" };

            App.main(args);

            var constructed = mocked.constructed();
            Assertions.assertEquals(1, constructed.size());

            verify(constructed.get(0), times(1)).run();
        }
    }

}
