package backend.textProcessors;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeProcessorTest {

    @Test
    void parsesStandardTime() {
        assertEquals(LocalTime.of(10, 30), TimeProcessor.getTime("10:30"));
    }

    @Test
    void parsesDotSeparatedTime() {
        assertEquals(LocalTime.of(9, 15), TimeProcessor.getTime("9.15"));
    }

    @Test
    void parsesKeywordTime() {
        assertEquals(LocalTime.of(9, 0), TimeProcessor.getTime("утро"));
    }
}
