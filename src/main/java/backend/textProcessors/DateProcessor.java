package backend.textProcessors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class DateProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DateProcessor.class);

    private static final DateTimeFormatter FULL_DATE =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final DateTimeFormatter DAY_MONTH =
            DateTimeFormatter.ofPattern("dd.MM");

    private static final DateTimeFormatter DAY =
            new DateTimeFormatterBuilder()
                    .appendPattern("dd")
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, LocalDate.now().getMonthValue())
                    .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                    .toFormatter();

    private DateProcessor() {
    }

    public static LocalDate getDate(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        text = text.trim().toLowerCase();
        logger.debug("Попытка распознавания даты из строки: '{}'", text);

        LocalDate wordDate = parseWords(text);
        if (wordDate != null) {
            return wordDate;
        }

        LocalDate fullDate = parseFullDate(text);
        if (fullDate != null) {
            return fullDate;
        }

        LocalDate dayMonth = parseDayMonth(text);
        if (dayMonth != null) {
            return dayMonth;
        }

        LocalDate dayOnly = parseDayOnly(text);
        if (dayOnly != null) {
            return dayOnly;
        }

        logger.debug("Не удалось распознать дату из '{}'", text);
        return null;
    }

    public static boolean isDate(String text) {
        return getDate(text) != null;
    }


    private static LocalDate parseWords(String text) {
        LocalDate today = LocalDate.now();
        return switch (text) {
            case "сегодня" -> today;
            case "завтра" -> today.plusDays(1);
            case "послезавтра" -> today.plusDays(2);
            case "вчера" -> today.minusDays(1);
            case "позавчера" -> today.minusDays(2);
            default -> null;
        };
    }

    private static LocalDate parseFullDate(String text) {
        try {
            return LocalDate.parse(text, FULL_DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static LocalDate parseDayMonth(String text) {
        try {
            MonthDay md = MonthDay.parse(text, DAY_MONTH);
            LocalDate today = LocalDate.now();

            LocalDate candidate = md.atYear(today.getYear());
            if (candidate.isBefore(today)) {
                candidate = candidate.plusYears(1);
            }
            return candidate;

        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static LocalDate parseDayOnly(String text) {
        try {
            LocalDate date = LocalDate.parse(text, DAY);
            LocalDate today = LocalDate.now();

            if (date.isBefore(today)) {
                date = date.plusMonths(1);
            }
            return date;

        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
