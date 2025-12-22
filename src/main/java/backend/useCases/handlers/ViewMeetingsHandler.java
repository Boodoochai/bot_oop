package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
import backend.textProcessors.DateProcessor;
import model.Client;
import model.Meeting;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.IDataStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewMeetingsHandler implements IUseCaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ViewMeetingsHandler.class);

    private int state = 0;
    private boolean isDone = false;
    private ViewMode mode;
    private LocalDate date;

    private enum ViewMode {
        DAY, WEEK, MONTH, ALL
    }

    @Override
    public Response handleRequest(Request request, IDataStorage dataStorage, ClientIdentificationHandler clientIdentificationHandler) {
        Client owner = request.requestOwner();
        logger.debug("Обработка запроса просмотра встреч от клиента: '{}' на шаге {}", owner.name(), state);

        if (state == 0) {
            state++;
            logger.debug("Запрос типа просмотра встреч у клиента: {}", owner.name());
            return new Response("Выберите режим просмотра:", new String[][]{
                    {"На день"},
                    {"На неделю"},
                    {"На месяц"},
                    {"Все встречи"}
            });
        }

        if (state == 1) {
            String choice = request.text().trim().toLowerCase();
            if (choice.contains("день")) {
                mode = ViewMode.DAY;
            } else if (choice.contains("недел") || choice.contains("нед")) {
                mode = ViewMode.WEEK;
            } else if (choice.contains("месяц") || choice.contains("мес")) {
                mode = ViewMode.MONTH;
            } else if (choice.contains("все") || choice.contains("всё")) {
                mode = ViewMode.ALL;
            } else {
                logger.warn("Некорректный выбор режима просмотра: '{}' от клиента '{}'", choice, owner.name());
                return new Response("Пожалуйста, выберите один из вариантов:", new String[][]{
                        {"На день"},
                        {"На неделю"},
                        {"На месяц"},
                        {"Все встречи"}
                });
            }

            if (mode == ViewMode.ALL) {
                state = 4; // пропускаем ввод даты
            } else {
                state++;
                return new Response("Введите дату (формат ДД.ММ)",
                        new String[][]{{"Сегодня"}, {"Завтра"}, {"Послезавтра"}});
            }
        }

        if (state == 2) {
            String text = request.text().trim();
            if (!DateProcessor.isDate(text)) {
                logger.warn("Неверный формат даты при просмотре встреч: '{}' от клиента '{}'", text, owner.name());
                return new Response("""
                        Неверный формат даты. Пожалуйста, введите дату в формате ДД.ММ
                        Например: 24.12, 01.02 или используйте: Сегодня, Завтра""",
                        new String[][]{{"Сегодня"}, {"Завтра"}, {"Послезавтра"}});
            }

            date = DateProcessor.getDate(text);
            state++;
        }

        if (state == 3 || state == 4) {
            List<Meeting> meetings;
            String periodText;

            if (mode == ViewMode.ALL) {
                meetings = dataStorage.getMeetingsWithClient(owner);
                periodText = "все ваши встречи";
            } else if (mode == ViewMode.DAY) {
                LocalDateTime from = date.atStartOfDay();
                LocalDateTime to = date.atTime(23, 59, 59);
                meetings = dataStorage.getMeetingsForClientBetween(owner, from, to);
                periodText = "встречи на " + date.format(DateTimeFormatter.ofPattern("dd.MM.yy"));
            } else if (mode == ViewMode.WEEK) {
                LocalDate weekStart = date.minusDays(date.getDayOfWeek().getValue() - 1); // понедельник
                LocalDate weekEnd = weekStart.plusDays(6);
                LocalDateTime from = weekStart.atStartOfDay();
                LocalDateTime to = weekEnd.atTime(23, 59, 59);
                meetings = dataStorage.getMeetingsForClientBetween(owner, from, to);
                periodText = "встречи на неделю с " + weekStart.format(DateTimeFormatter.ofPattern("dd.MM"));
            } else { // MONTH
                LocalDate monthDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
                LocalDateTime from = monthDate.atStartOfDay();
                LocalDateTime to = from.plusMonths(1);
                meetings = dataStorage.getMeetingsForClientBetween(owner, from, to);
                periodText = "встречи на " + date.format(DateTimeFormatter.ofPattern("MM.yy"));
            }

            logger.debug("Найдено {} встреч(и) для клиента '{}' на период '{}'",
                    meetings.size(), owner.name(), periodText);

            if (meetings.isEmpty()) {
                isDone = true;
                return new Response("На выбранный период у вас нет встреч.");
            }

            StringBuilder sb = new StringBuilder("📚 ").append(periodText).append(":\n\n");
            meetings.stream()
                    .sorted((a, b) -> a.start().compareTo(b.start()))
                    .forEach(meeting -> {
                        String startTime = meeting.start().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                        String endTime = meeting.end().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                        String title = meeting.title().isEmpty() ? "Без названия" : meeting.title();
                        String participants = formatParticipants(meeting.participants().stream().toList(), owner);
                        sb.append("• ").append(startTime).append("-").append(endTime)
                                .append(" — <b>").append(title).append("</b>")
                                .append("\n  Участники: ").append(participants).append("\n\n");
                    });

            isDone = true;
            return new Response(sb.toString().trim());
        }

        logger.warn("Неожиданный запрос к ViewMeetingsHandler после завершения для клиента '{}'", owner.name());
        return new Response("Операция уже завершена.");
    }

    private String formatParticipants(List<Client> participants, Client owner) {
        return participants.stream()
                .map(client -> {
                    String name = client.name();
                    if (name.isEmpty()) {
                        return client.clientId().toString().substring(0, 8);
                    }
                    return name.equals(owner.name()) ? "<i>" + name + " (вы)</i>" : name;
                })
                .reduce((a, b) -> a + ", " + b)
                .orElse("—");
    }

    @Override
    public boolean isDone() {
        return isDone;
    }
}