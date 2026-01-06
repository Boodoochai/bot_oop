package backend.textProcessors;


import Logger.ILogger;
import Logger.LoggerProvider;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class TimeProcessor {

    private static final ILogger logger = LoggerProvider.get(TimeProcessor.class);

    private static final DateTimeFormatter HH_MM =
            DateTimeFormatter.ofPattern("H:mm");

    private static final DateTimeFormatter HH_MM_SS =
            DateTimeFormatter.ofPattern("H:mm:ss");

    private static final DateTimeFormatter H_ONLY =
            new DateTimeFormatterBuilder()
                    .appendValue(ChronoField.HOUR_OF_DAY)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

    private static final DateTimeFormatter H_MM_DOT =
            DateTimeFormatter.ofPattern("H.mm");

    private static final DateTimeFormatter H_MM_SS_DOT =
            DateTimeFormatter.ofPattern("H.mm.ss");

    private TimeProcessor() {
    }

    public static LocalTime getTime(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        text = normalize(text);
        logger.debug("Попытка распознавания времени из строки: '{}'", text);

        LocalTime wordTime = parseWords(text);
        if (wordTime != null) {
            return wordTime;
        }

        LocalTime t = parse(text, HH_MM_SS);
        if (t != null) return t;

        t = parse(text, HH_MM);
        if (t != null) return t;

        t = parse(text, H_MM_SS_DOT);
        if (t != null) return t;

        t = parse(text, H_MM_DOT);
        if (t != null) return t;

        t = parse(text, H_ONLY);
        if (t != null) return t;

        logger.debug("Не удалось распознать время из '{}'", text);
        return null;
    }

    public static boolean isTime(String text) {
        return getTime(text) != null;
    }


    private static LocalTime parse(String text, DateTimeFormatter formatter) {
        try {
            LocalTime time = LocalTime.parse(text, formatter);
            logger.debug("Строка '{}' распознана как время: {}", text, time);
            return time;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static LocalTime parseWords(String text) {
        return switch (text) {
            case "сейчас" -> LocalTime.now();
            case "утро" -> LocalTime.of(9, 0);
            case "день", "полдень" -> LocalTime.of(12, 0);
            case "вечер" -> LocalTime.of(18, 0);
            case "ночь" -> LocalTime.of(23, 0);
            case "полночь" -> LocalTime.MIDNIGHT;
            default -> null;
        };
    }

    private static String normalize(String text) {
        return text.trim()
                .toLowerCase()
                .replace(',', ':')
                .replace("::", ":");
    }
}
