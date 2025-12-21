package backend.automaton;

import Identification.ClientIdentificationHandler;
import model.Client;
import model.Meeting;
import model.Response;
import storage.IDataStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CalendarAutomaton {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final DateTimeFormatter YEAR_MONTH_FORMAT =
            DateTimeFormatter.ofPattern("MM.yy");

    private final IDataStorage dataStorage;
    private final ClientIdentificationHandler clientIdentificationHandler;
    private final Client owner;
    private State state = State.IDLE;

    public CalendarAutomaton(IDataStorage dataStorage,
                             ClientIdentificationHandler clientIdentificationHandler,
                             Client owner) {
        this.dataStorage = dataStorage;
        this.clientIdentificationHandler = clientIdentificationHandler;
        this.owner = owner;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Response feed(String text) {
        text = text.trim();
        if (text.isEmpty()) {
            return new Response("Пустой запрос. Напишите '/help' для списка команд.");
        }

        String[] parts = text.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        try {
            return switch (cmd) {
                case "/start", "/help", "help" -> handleHelp();
                case "add" -> handleAdd(args);
                case "day", "on" -> handleDay(args);
                case "week" -> handleWeek(args);
                case "month" -> handleMonth(args);
                case "with" -> handleWith(args);
                default -> new Response("Неизвестная команда: " + cmd +
                        "\nНапишите '/help' для списка команд.");
            };
        } catch (Exception e) {
            return new Response("Ошибка обработки запроса: " + e.getMessage());
        }
    }

    // Какие переходы вообще доступны в этом состоянии автомат
    public Set<String> getAvailableTransitions() {
        return Set.of("help", "add", "day", "on", "week", "month", "with");
    }

    private Response handleHelp() {
        String text =
                """
                        Доступные команды:
                        1) /help
                           Информация про возможности (Это сообщение).
                        
                        2) add ДД.ММ.ГГ HH:MM DUR_MIN [Заголовок; Описание; Участник1,Участник2,...]
                           Год можно не указывать: ДД.ММ
                           Заголовок и участники — необязательны.
                           Пример:
                           add 20.01.25 14:00 60 Встреча; Урок; Иван,Петя
                        
                        3) day ДД.ММ.ГГ
                           Показать встречи на день (год можно не указывать: ДД.ММ).
                        
                        4) week ДД.ММ.ГГ
                           Показать встречи на неделю (с этой даты, год можно не указывать).
                        
                        5) month ДД.ММ.ГГ
                           Показать встречи на месяц (любая дата месяца, год можно не указывать).
                        
                        6) with Имя
                           Показать все встречи с этим человеком.
                        """;
        return new Response(text);
    }

    // Copy(bara)

    private Response handleAdd(final String args) {
        // Ожидаем:
        // ДД.ММ[.ГГ] HH:MM DUR_MIN [Заголовок; Описание; Участник1,Участник2,...]
        String[] tokens = args.split("\\s+", 4);
        if (tokens.length < 3) {
            return new Response("Неверный формат команды add. Напишите '/help' для примера.");
        }

        String dateStr = tokens[0]; // 20.01.25
        String timeStr = tokens[1]; // 14:00
        String durStr = tokens[2]; // 60
        String rest = tokens.length >= 4 ? tokens[3] : ""; // "Заголовок; Описание; Участник1,Участник2,..."

        LocalDate date = parseDate(dateStr);
        LocalTime time = LocalTime.parse(timeStr);
        int durationMinutes = Integer.parseInt(durStr);
        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end = start.plusMinutes(durationMinutes);

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

    private Response handleDay(final String args) {
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

    private Response handleWeek(final String args) {
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

    private Response handleMonth(final String args) {
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

    private Response handleWith(final String args) {
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

    private String formatMeeting(final Meeting m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.title())
                .append(" (")
                .append(m.start().format(DATE_TIME_FORMAT))
                .append(" - ")
                .append(m.end().format(DATE_TIME_FORMAT))
                .append(")\n")
                .append("Описание: ").append(m.description()).append("\n")
                .append("Участники: ");

        boolean first = true;
        for (Client c : m.participants()) {
            if (!first) sb.append(", ");
            first = false;

            String name = c.name();
            if (name.isBlank()) {
                sb.append(c.clientId());
            } else {
                sb.append(name);
            }
        }
        return sb.toString();
    }

    private String formatMeetingShort(final Meeting m) {
        return m.title() + " [" +
                m.start().format(DATE_TIME_FORMAT) + " - " +
                m.end().format(DATE_TIME_FORMAT) + "]";
    }

    private LocalDate parseDate(String s) {
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


    // Состояние автомата (пока одно, можно расширять)
    private enum State {
        IDLE
    }
}
