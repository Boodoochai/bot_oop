package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
import Logger.ILogger;
import Logger.LoggerProvider;
import model.Client;
import model.Meeting;
import model.Request;
import model.Response;
import storage.IDataStorage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeleteMeetingHandler implements IUseCaseHandler {
    private static final ILogger logger = LoggerProvider.get(DeleteMeetingHandler.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private int state = 0;
    private boolean isDone = false;
    private List<Meeting> meetings = List.of();
    private String[][] meetingOptions;
    private Meeting selectedMeeting;

    @Override
    public Response handleRequest(Request request, IDataStorage dataStorage, ClientIdentificationHandler clientIdentificationHandler) {
        Client owner = request.requestOwner();

        if (state == 0) {
            meetings = getMeetingsForClient(dataStorage, owner);
            if (meetings.isEmpty()) {
                isDone = true;
                return new Response("У вас нет встреч для удаления.");
            }

            meetingOptions = buildIndexOptions(meetings.size(), true);
            state = 1;
            return new Response(buildMeetingsListText(meetings, "Выберите встречу для удаления:"), meetingOptions);
        }

        if (state == 1) {
            String text = request.text().trim();
            if (isCancel(text)) {
                isDone = true;
                return new Response("Удаление отменено.");
            }

            Integer index = parseIndex(text, meetings.size());
            if (index == null) {
                return new Response("Пожалуйста, выберите номер из списка:", meetingOptions);
            }

            selectedMeeting = meetings.get(index);
            state = 2;
            return new Response(buildDeleteConfirmText(selectedMeeting), new String[][]{{"Да"}, {"Нет"}});
        }

        if (state == 2) {
            String text = request.text().trim().toLowerCase();
            if (text.startsWith("д")) {
                boolean removed = dataStorage.deleteMeeting(selectedMeeting.uuid());
                isDone = true;
                return new Response(removed ? "Встреча удалена." : "Не удалось удалить встречу.");
            }

            if (text.startsWith("н")) {
                isDone = true;
                return new Response("Удаление отменено.");
            }

            return new Response("Пожалуйста, подтвердите удаление:", new String[][]{{"Да"}, {"Нет"}});
        }

        logger.warn("Повторный запрос после завершения DeleteMeetingHandler");
        return new Response("Операция уже завершена.");
    }

    @Override
    public boolean isDone() {
        return isDone;
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
        String title = meeting.title() == null || meeting.title().isBlank() ? "Без названия" : meeting.title();
        String date = meeting.start().format(DATE_FORMAT);
        String startTime = meeting.start().toLocalTime().format(TIME_FORMAT);
        String endTime = meeting.end().toLocalTime().format(TIME_FORMAT);
        return index + ") " + date + " " + startTime + "-" + endTime + " — " + title;
    }

    private String buildDeleteConfirmText(Meeting meeting) {
        String title = meeting.title() == null || meeting.title().isBlank() ? "Без названия" : meeting.title();
        String date = meeting.start().format(DATE_FORMAT);
        String startTime = meeting.start().toLocalTime().format(TIME_FORMAT);
        String endTime = meeting.end().toLocalTime().format(TIME_FORMAT);
        return "Удалить встречу '" + title + "' (" + date + " " + startTime + "-" + endTime + ")?";
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

    private boolean isCancel(String text) {
        String normalized = text.trim().toLowerCase();
        return normalized.equals("отмена") || normalized.equals("cancel");
    }
}
