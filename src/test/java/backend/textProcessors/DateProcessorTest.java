package backend.textProcessors;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DateProcessorTest {

    @Test
    void parsesFullDate() {
        LocalDate date = DateProcessor.getDate("31.12.2099");
        assertEquals(LocalDate.of(2099, 12, 31), date);
    }

    @Test
    void parsesTodayKeyword() {
        LocalDate date = DateProcessor.getDate("сегодня");
        assertEquals(LocalDate.now(), date);
    }

    @Test
    void rejectsInvalidDate() {
        assertFalse(DateProcessor.isDate("99.99.9999"));
    }
}
