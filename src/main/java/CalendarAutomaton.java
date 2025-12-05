import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class CalendarAutomaton {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final DateTimeFormatter YEAR_MONTH_FORMAT =
            DateTimeFormatter.ofPattern("MM.yy");

    private final @NonNull IDataStorage dataStorage;
    private final @NonNull ClientIdentificationHandler clientIdentificationHandler;
    private final @NonNull Client owner;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    // Состояние автомата (пока одно, можно расширять)
    private enum State {
        IDLE
    }

    private State state = State.IDLE;

    public CalendarAutomaton(@NonNull IDataStorage dataStorage,
                             @NonNull ClientIdentificationHandler clientIdentificationHandler,
                             @NonNull Client owner) {
        this.dataStorage = dataStorage;
        this.clientIdentificationHandler = clientIdentificationHandler;
        this.owner = owner;
    }

    public @NonNull Response feed(@NonNull String text) {
        text = text.trim();
        if (text.isEmpty()) {
            return new Response("Пустой запрос. Напишите '/help' для списка команд.");
        }

        String[] parts = text.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        try {
            switch (cmd) {
                case "/start":
                    return handleHelp();
                case "/help":
                    return handleHelp();
                case "help":
                    return handleHelp();
                case "add":
                    return handleAdd(args);
                case "day":
                case "on":
                    return handleDay(args);
                case "week":
                    return handleWeek(args);
                case "month":
                    return handleMonth(args);
                case "with":
                    return handleWith(args);
                default:
                    return new Response("Неизвестная команда: " + cmd +
                            "\nНапишите '/help' для списка команд.");
            }
        } catch (Exception e) {
            return new Response("Ошибка обработки запроса: " + e.getMessage());
        }
    }

    // Какие переходы вообще доступны в этом состоянии автомат
    public @NonNull Set<String> getAvailableTransitions() {
        return Set.of("help", "add", "day", "on", "week", "month", "with");
    }

    // Copy(bara) BaseRequestHandler

    private @NonNull Response handleHelp() {
        String text =
                "Доступные команды:\n" +
                        "1) /help\n" +
                        "   Информация про возможности (Это сообщение).\n" +
                        "\n" +
                        "2) add ДД.ММ.ГГ HH:MM DUR_MIN [Заголовок; Описание; Участник1,Участник2,...]\n" +
                        "   Год можно не указывать: ДД.ММ\n" +
                        "   Заголовок и участники — необязательны.\n" +
                        "   Пример:\n" +
                        "   add 20.01.25 14:00 60 Встреча; Урок; Иван,Петя\n" +
                        "\n" +
                        "3) day ДД.ММ.ГГ\n" +
                        "   Показать встречи на день (год можно не указывать: ДД.ММ).\n" +
                        "\n" +
                        "4) week ДД.ММ.ГГ\n" +
                        "   Показать встречи на неделю (с этой даты, год можно не указывать).\n" +
                        "\n" +
                        "5) month ДД.ММ.ГГ\n" +
                        "   Показать встречи на месяц (любая дата месяца, год можно не указывать).\n" +
                        "\n" +
                        "6) with Имя\n" +
                        "   Показать все встречи с этим человеком.\n";
        return new Response(text);
    }

    private @NonNull Response handleAdd(@NonNull final String args) {
        // Ожидаем:
        // ДД.ММ[.ГГ] HH:MM DUR_MIN [Заголовок; Описание; Участник1,Участник2,...]
        String[] tokens = args.split("\\s+", 4);
        if (tokens.length < 3) {
            return new Response("Неверный формат команды add. Напишите '/help' для примера.");
        }

        String dateStr = tokens[0]; // 20.01.25
        String timeStr = tokens[1]; // 14:00
        String durStr  = tokens[2]; // 60
        String rest    = tokens.length >= 4 ? tokens[3] : ""; // "Заголовок; Описание; Участник1,Участник2,..."

        LocalDate date = parseDate(dateStr);
        LocalTime time = LocalTime.parse(timeStr);
        int durationMinutes = Integer.parseInt(durStr);
        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end   = start.plusMinutes(durationMinutes);

        String title = "";
        String description = "";
        String participantsStr = "";

        if (!rest.isEmpty()) {
            String[] parts = rest.split(";", 3);
            if (parts.length > 0) title = parts[0].trim();
            if (parts.length > 1) description = parts[1].trim();
            if (parts.length > 2) participantsStr = parts[2].trim();
        }

        Set<Client> participants = new HashSet<>();
        participants.add(owner);

        if (!participantsStr.isEmpty()) {
            String[] names = participantsStr.split(",");
            for (String rawName : names) {
                String name = rawName.trim();
                if (name.isEmpty()) continue;
                Client c = clientIdentificationHandler.getClient(name);
                participants.add(c);
            }
        }

        Meeting meeting = new Meeting(title, start, end, participants, description);
        dataStorage.addMeeting(meeting);

        return new Response("Встреча добавлена:\n" + formatMeeting(meeting));
    }

    private @NonNull Response handleDay(@NonNull final String args) {
        LocalDate date = parseDate(args.trim());
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59, 59);

        List<Meeting> list = dataStorage.getMeetingsForClientBetween(owner, from, to);
        String dateStr = date.format(DATE_FORMAT);
        if (list.isEmpty()) {
            return new Response("На " + dateStr + " встреч не найдено.");
        }

        StringBuilder sb = new StringBuilder("Встречи на ").append(dateStr).append(":\n");
        for (Meeting m : list) {
            sb.append("- ").append(formatMeetingShort(m)).append("\n");
        }
        return new Response(sb.toString());
    }

    private @NonNull Response handleWeek(@NonNull final String args) {
        LocalDate startDate = parseDate(args.trim());
        LocalDateTime from = startDate.atStartOfDay();
        LocalDateTime to = startDate.plusDays(6).atTime(23, 59, 59);

        List<Meeting> list = dataStorage.getMeetingsForClientBetween(owner, from, to);
        String startStr = startDate.format(DATE_FORMAT);
        if (list.isEmpty()) {
            return new Response("На неделю с " + startStr + " встреч не найдено.");
        }

        StringBuilder sb = new StringBuilder("Встречи на неделю с ").append(startStr).append(":\n");
        for (Meeting m : list) {
            sb.append("- ").append(formatMeetingShort(m)).append("\n");
        }
        return new Response(sb.toString());
    }

    private @NonNull Response handleMonth(@NonNull final String args) {
        // args: ДД.ММ[.ГГ] — берём только месяц и год
        LocalDate anyDate = parseDate(args.trim());
        YearMonth ym = YearMonth.of(anyDate.getYear(), anyDate.getMonthValue());

        LocalDateTime from = ym.atDay(1).atStartOfDay();
        LocalDateTime to = ym.atEndOfMonth().atTime(23, 59, 59);

        List<Meeting> list = dataStorage.getMeetingsForClientBetween(owner, from, to);
        String ymStr = ym.format(YEAR_MONTH_FORMAT);
        if (list.isEmpty()) {
            return new Response("На месяц " + ymStr + " встреч не найдено.");
        }

        StringBuilder sb = new StringBuilder("Встречи на ").append(ymStr).append(":\n");
        for (Meeting m : list) {
            sb.append("- ").append(formatMeetingShort(m)).append("\n");
        }
        return new Response(sb.toString());
    }

    private @NonNull Response handleWith(@NonNull final String args) {
        String name = args.trim();
        if (name.isEmpty()) {
            return new Response("Нужно указать имя: with Имя");
        }

        Client other = clientIdentificationHandler.getClient(name);
        List<Meeting> list = dataStorage.getMeetingsWithClient(other);
        if (list.isEmpty()) {
            return new Response("Встреч с " + name + " не найдено.");
        }

        StringBuilder sb = new StringBuilder("Встречи с ").append(name).append(":\n");
        for (Meeting m : list) {
            sb.append("- ").append(formatMeetingShort(m)).append("\n");
        }
        return new Response(sb.toString());
    }

    private String formatMeeting(@NonNull final Meeting m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getTitle())
                .append(" (")
                .append(m.getStart().format(DATE_TIME_FORMAT))
                .append(" - ")
                .append(m.getEnd().format(DATE_TIME_FORMAT))
                .append(")\n")
                .append("Описание: ").append(m.getDescription()).append("\n")
                .append("Участники: ");

        boolean first = true;
        for (Client c : m.getParticipants()) {
            if (!first) sb.append(", ");
            first = false;

            String name = c.getName();
            if (name.isBlank()) {
                sb.append(c.getUUID());
            } else {
                sb.append(name);
            }
        }
        return sb.toString();
    }

    private String formatMeetingShort(@NonNull final Meeting m) {
        return m.getTitle() + " [" +
                m.getStart().format(DATE_TIME_FORMAT) + " - " +
                m.getEnd().format(DATE_TIME_FORMAT) + "]";
    }


    private LocalDate parseDate(@NonNull String s) {
        String[] parts = s.trim().split("\\.");
        if (parts.length < 2 || parts.length > 3) {
            throw new IllegalArgumentException("Ожидается дата в формате ДД.ММ[.ГГ]");
        }
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        int year;
        if (parts.length == 3) {
            String ys = parts[2];
            if (ys.length() == 2) {
                year = 2000 + Integer.parseInt(ys);
            } else if (ys.length() == 4) {
                year = Integer.parseInt(ys);
            } else {
                throw new IllegalArgumentException("Год должен быть в формате ГГ или ГГГГ");
            }
        } else {
            year = LocalDate.now().getYear();
        }

        return LocalDate.of(year, month, day);
    }
}
