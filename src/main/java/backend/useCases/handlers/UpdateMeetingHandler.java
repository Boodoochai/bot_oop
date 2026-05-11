package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
import Logger.ILogger;
import Logger.LoggerProvider;
import backend.textProcessors.DateProcessor;
import backend.textProcessors.TimeProcessor;
import model.Client;
import model.Meeting;
import model.Request;
import model.Response;
import storage.IDataStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpdateMeetingHandler implements IUseCaseHandler {
    private static final ILogger logger = LoggerProvider.get(UpdateMeetingHandler.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private int state = 0;
    private boolean isDone = false;
    private List<Meeting> meetings = List.of();
    private String[][] meetingOptions;
    private Meeting selectedMeeting;

    private String title;
    private LocalDate date;
    private LocalTime time;
    private Duration duration;
    private Set<Client> participants;

    @Override
    public Response handleRequest(Request request, IDataStorage dataStorage, ClientIdentificationHandler clientIdentificationHandler) {
        Client owner = request.requestOwner();

        if (state == 0) {
            meetings = getMeetingsForClient(dataStorage, owner);
            if (meetings.isEmpty()) {
                isDone = true;
                return new Response("У вас нет встреч для обновления.");
            }

            meetingOptions = buildIndexOptions(meetings.size(), true);
            state = 1;
            return new Response(buildMeetingsListText(meetings, "Выберите встречу для обновления:"), meetingOptions);
        }

        if (state == 1) {
            String text = request.text().trim();
            if (isCancel(text)) {
                isDone = true;
                return new Response("Обновление отменено.");
            }

            Integer index = parseIndex(text, meetings.size());
            if (index == null) {
                return new Response("Пожалуйста, выберите номер из списка:", meetingOptions);
            }

            selectedMeeting = meetings.get(index);
            initDefaultsFromMeeting(selectedMeeting);
            state = 2;
            return new Response(buildTitlePrompt(), new String[][]{{"Без изменений"}});
        }

        if (state == 2) {
            String text = request.text().trim();
            if (!isSkipChoice(text)) {
                if (text.isEmpty()) {
                    return new Response("Название не может быть пустым. Введите новое название или выберите 'Без изменений'.",
                            new String[][]{{"Без изменений"}});
                }
                title = text;
            }

            state = 3;
            return new Response(buildDatePrompt(), new String[][]{{"Сегодня"}, {"Завтра"}, {"Послезавтра"}, {"Без изменений"}});
        }

        if (state == 3) {
            String text = request.text().trim();
            if (!isSkipChoice(text)) {
                if (!DateProcessor.isDate(text)) {
                    return new Response("Неверный формат даты. Введите дату в формате ДД.ММ или выберите 'Без изменений'.",
                            new String[][]{{"Сегодня"}, {"Завтра"}, {"Послезавтра"}, {"Без изменений"}});
                }
                date = DateProcessor.getDate(text);
            }

            state = 4;
            return new Response(buildTimePrompt(), new String[][]{{"6:00", "7:00", "8:00", "9:00"}, {"10:00", "11:00", "12:00", "13:00"}, {"14:00", "15:00", "16:00", "17:00"}, {"18:00", "19:00", "20:00", "21:00"}, {"Без изменений"}});
        }

        if (state == 4) {
            String text = request.text().trim();
            if (!isSkipChoice(text)) {
                if (!TimeProcessor.isTime(text)) {
                    return new Response("Неверный формат времени. Введите время в формате ЧЧ:ММ или выберите 'Без изменений'.",
                            new String[][]{{"6:00", "7:00", "8:00", "9:00"}, {"10:00", "11:00", "12:00", "13:00"}, {"14:00", "15:00", "16:00", "17:00"}, {"18:00", "19:00", "20:00", "21:00"}, {"Без изменений"}});
                }
                time = TimeProcessor.getTime(text);
            }

            state = 5;
            return new Response(buildDurationPrompt(), new String[][]{{"00:30"}, {"1:00"}, {"1:30"}, {"2:00"}, {"Без изменений"}});
        }

        if (state == 5) {
            String text = request.text().trim();
            if (!isSkipChoice(text)) {
                if (!TimeProcessor.isTime(text)) {
                    return new Response("Неверный формат длительности. Введите длительность в формате ЧЧ:ММ или выберите 'Без изменений'.",
                            new String[][]{{"00:30"}, {"1:00"}, {"1:30"}, {"2:00"}, {"Без изменений"}});
                }
                duration = Duration.between(LocalTime.of(0, 0), TimeProcessor.getTime(text));
            }

            state = 6;
            return new Response(buildParticipantsPrompt(owner), new String[][]{{"Без изменений"}});
        }

        if (state == 6) {
            String text = request.text().trim();
            if (!isSkipChoice(text)) {
                participants = new HashSet<>();
                participants.add(owner);
                participants.add(clientIdentificationHandler.getClient(text));
            }

            LocalDateTime start = LocalDateTime.of(date, time);
            LocalDateTime end = start.plusSeconds(duration.getSeconds());
            Meeting updated = new Meeting(selectedMeeting.uuid(), title, start, end, participants, selectedMeeting.description());
            boolean updatedOk = dataStorage.updateMeeting(updated);
            isDone = true;
            return new Response(updatedOk ? "Встреча обновлена." : "Не удалось обновить встречу.");
        }

        logger.warn("Повторный запрос после завершения UpdateMeetingHandler");
        return new Response("Операция уже завершена.");
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    private void initDefaultsFromMeeting(Meeting meeting) {
        title = meeting.title();
        date = meeting.start().toLocalDate();
        time = meeting.start().toLocalTime();
        duration = Duration.between(meeting.start(), meeting.end());
        participants = new HashSet<>(meeting.participants());
    }

    private List<Meeting> getMeetingsForClient(IDataStorage dataStorage, Client owner) {
        List<Meeting> result = new ArrayList<>(dataStorage.getMeetingsWithClient(owner));
        result.sort(Comparator.comparing(Meeting::start));
        return result;
    }

    private String buildMeetingsListText(List<Meeting> meetings, String header) {
        StringBuilder sb = new StringBuilder(header).append("\n\n");
        for (int i = 0; i < meetings.size(); i++) {
            sb.append(formatMeetingLine(i + 1, meetings.get(i))).append("\n");
        }
        return sb.toString().trim();
    }

    private String formatMeetingLine(int index, Meeting meeting) {
        String meetingTitle = meeting.title() == null || meeting.title().isBlank() ? "Без названия" : meeting.title();
        String dateText = meeting.start().format(DATE_FORMAT);
        String startTime = meeting.start().toLocalTime().format(TIME_FORMAT);
        String endTime = meeting.end().toLocalTime().format(TIME_FORMAT);
        return index + ") " + dateText + " " + startTime + "-" + endTime + " — " + meetingTitle;
    }

    private String buildTitlePrompt() {
        String currentTitle = title == null || title.isBlank() ? "Без названия" : title;
        return "Введите новое название встречи (текущее: '" + currentTitle + "') или выберите 'Без изменений'.";
    }

    private String buildDatePrompt() {
        return "Введите новую дату (ДД.ММ). Текущая дата: " + date.format(DATE_FORMAT) + ".";
    }

    private String buildTimePrompt() {
        return "Введите новое время (ЧЧ:ММ). Текущее время: " + time.format(TIME_FORMAT) + ".";
    }

    private String buildDurationPrompt() {
        long minutes = duration.toMinutes();
        long hoursPart = minutes / 60;
        long minutesPart = minutes % 60;
        String currentDuration = String.format("%02d:%02d", hoursPart, minutesPart);
        return "Введите новую длительность (ЧЧ:ММ). Текущая длительность: " + currentDuration + ".";
    }

    private String buildParticipantsPrompt(Client owner) {
        String other = participants.stream()
                .filter(client -> !client.equals(owner))
                .map(client -> client.name().isEmpty() ? client.clientId().toString().substring(0, 8) : client.name())
                .reduce((a, b) -> a + ", " + b)
                .orElse("—");
        return "Введите имя участника (текущий: " + other + ") или выберите 'Без изменений'.";
    }

    private String[][] buildIndexOptions(int size, boolean includeCancel) {
        List<String[]> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            row.add(String.valueOf(i));
            if (row.size() == 4) {
                rows.add(row.toArray(new String[0]));
                row.clear();
            }
        }

        if (!row.isEmpty()) {
            rows.add(row.toArray(new String[0]));
        }

        if (includeCancel) {
            rows.add(new String[]{"Отмена"});
        }

        return rows.toArray(new String[0][]);
    }

    private Integer parseIndex(String text, int size) {
        try {
            int index = Integer.parseInt(text.trim());
            if (index >= 1 && index <= size) {
                return index - 1;
            }
        } catch (NumberFormatException ignored) {
            return null;
        }
        return null;
    }

    private boolean isSkipChoice(String text) {
        String normalized = text.trim().toLowerCase();
        return normalized.equals("без изменений")
                || normalized.equals("без изменения")
                || normalized.equals("оставить")
                || normalized.equals("не менять")
                || normalized.equals("пропустить");
    }

    private boolean isCancel(String text) {
        String normalized = text.trim().toLowerCase();
        return normalized.equals("отмена") || normalized.equals("cancel");
    }
}
