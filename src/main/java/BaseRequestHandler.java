import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.*;
        import java.time.format.DateTimeFormatter;
import java.util.*;

final public class BaseRequestHandler implements IRequestHandler {
    private final @NonNull IDataStorage dataStorage;

    public BaseRequestHandler(@NonNull final IDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public @NonNull Response handleRequest(@NonNull final Request request) {
        String text = request.getText().trim();
        if (text.isEmpty()) {
            return new Response("Пустой запрос. Напишите 'help' для списка команд.");
        }

        String[] parts = text.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        try {
            switch (cmd) {
                case "help":
                    return handleHelp();
                case "add":
                    return handleAdd(request.getRequestOwner(), args);
                case "day":
                case "on":
                    return handleDay(request.getRequestOwner(), args);
                case "week":
                    return handleWeek(request.getRequestOwner(), args);
                case "month":
                    return handleMonth(request.getRequestOwner(), args);
                case "with":
                    return handleWith(args);
                default:
                    return new Response("Неизвестная команда: " + cmd +
                            "\nНапишите 'help' для списка команд.");
            }
        } catch (Exception e) {
            return new Response("Ошибка обработки запроса: " + e.getMessage());
        }

    }

    private @NonNull Response handleHelp() {
        String text =
                "Доступные команды:\n" +
                        "1) help\n" +
                        "   Информация про возможности (Это сообщение).\n" +
                        "\n" +
                        "2) add YYYY-MM-DD HH:MM DUR_MIN Заголовок; Описание; Участник1,Участник2,...\n" +
                        "   Пример:\n" +
                        "   add 2025-01-20 14:00 60 Встреча; Урок; Иван,Петя\n" +
                        "\n" +
                        "3) day YYYY-MM-DD\n" +
                        "   Показать встречи на день.\n" +
                        "\n" +
                        "4) week YYYY-MM-DD\n" +
                        "   Показать встречи на неделю (с этой даты).\n" +
                        "\n" +
                        "5) month YYYY-MM\n" +
                        "   Показать встречи на месяц.\n" +
                        "\n" +
                        "6) with Имя\n" +
                        "   Показать все встречи с этим человеком.\n";
        return new Response(text);
    }


    private @NonNull Response handleAdd(
            @NonNull final Client owner,
            @NonNull final String args
    ) {
        // Ожидаем:
        // YYYY-MM-DD HH:MM DUR_MIN Заголовок; Описание; Участник1,Участник2,...
        // первые три токена
        String[] tokens = args.split("\\s+", 4);
        if (tokens.length < 4) {
            return new Response("Неверный формат команды add. Напишите 'help' для примера.");
        }

        String dateStr = tokens[0]; // 2025-01-20
        String timeStr = tokens[1]; // 14:00
        String durStr  = tokens[2]; // 60
        String rest    = tokens[3]; // "Заголовок; Описание; Участник1,Участник2,..."

        LocalDate date = LocalDate.parse(dateStr);
        LocalTime time = LocalTime.parse(timeStr);
        int durationMinutes = Integer.parseInt(durStr);
        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end   = start.plusMinutes(durationMinutes);

        String[] parts = rest.split(";", 3);
        if (parts.length < 3) {
            return new Response("Нужно указать: Заголовок; Описание; Участник1,Участник2,...");
        }

        String title = parts[0].trim();
        String description = parts[1].trim();
        String participantsStr = parts[2].trim();

        Set<Client> participants = new HashSet<>();
        // добавим владельца как участника по умолчанию
        participants.add(owner);

        if (!participantsStr.isEmpty()) {
            String[] names = participantsStr.split(",");
            for (String rawName : names) {
                String name = rawName.trim();
                if (name.isEmpty()) continue;
                Client c = ClientIdentificationHandler.getClient(dataStorage, name);
                participants.add(c);
            }
        }

        Meeting meeting = new Meeting(title, start, end, participants, description);
        dataStorage.addMeeting(meeting);

        return new Response("Встреча добавлена:\n" + formatMeeting(meeting));
    }

    private @NonNull Response handleDay(
            @NonNull final Client owner,
            @NonNull final String args
    ) {
        LocalDate date = LocalDate.parse(args.trim());
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.atTime(23, 59, 59);

        List<Meeting> list = dataStorage.getMeetingsForClientBetween(owner, from, to);
        if (list.isEmpty()) {
            return new Response("На " + date + " встреч не найдено.");
        }

        StringBuilder sb = new StringBuilder("Встречи на ").append(date).append(":\n");
        for (Meeting m : list) {
            sb.append("- ").append(formatMeetingShort(m)).append("\n");
        }
        return new Response(sb.toString());
    }

    private @NonNull Response handleWeek(
            @NonNull final Client owner,
            @NonNull final String args
    ) {
        LocalDate startDate = LocalDate.parse(args.trim());
        LocalDateTime from = startDate.atStartOfDay();
        LocalDateTime to = startDate.plusDays(6).atTime(23, 59, 59);

        List<Meeting> list = dataStorage.getMeetingsForClientBetween(owner, from, to);
        if (list.isEmpty()) {
            return new Response("На неделю с " + startDate + " встреч не найдено.");
        }

        StringBuilder sb = new StringBuilder("Встречи на неделю с ").append(startDate).append(":\n");
        for (Meeting m : list) {
            sb.append("- ").append(formatMeetingShort(m)).append("\n");
        }
        return new Response(sb.toString());
    }

    private @NonNull Response handleMonth(
            @NonNull final Client owner,
            @NonNull final String args
    ) {
        // args: YYYY-MM
        YearMonth ym = YearMonth.parse(args.trim());
        LocalDateTime from = ym.atDay(1).atStartOfDay();
        LocalDateTime to = ym.atEndOfMonth().atTime(23, 59, 59);

        List<Meeting> list = dataStorage.getMeetingsForClientBetween(owner, from, to);
        if (list.isEmpty()) {
            return new Response("На месяц " + ym + " встреч не найдено.");
        }

        StringBuilder sb = new StringBuilder("Встречи на ").append(ym).append(":\n");
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

        Client other = ClientIdentificationHandler.getClient(dataStorage, name);
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

    // Форматироватирование

    private String formatMeeting(@NonNull final Meeting m) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append(m.getTitle())
                .append(" (")
                .append(m.getStart().format(dtf))
                .append(" - ")
                .append(m.getEnd().format(dtf))
                .append(")\n")
                .append("Описание: ").append(m.getDescription()).append("\n")
                .append("Участники: ");

        boolean first = true;
        for (Client c : m.getParticipants()) {
            if (!first) sb.append(", ");
            first = false;
            sb.append(c.getUUID());
        }
        return sb.toString();
    }

    private String formatMeetingShort(@NonNull final Meeting m) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return m.getTitle() + " [" +
                m.getStart().format(dtf) + " - " +
                m.getEnd().format(dtf) + "]";
    }
}
