package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
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
import java.time.temporal.TemporalAmount;
import java.util.HashSet;
import java.util.Set;

public class CreateMeetingHandler implements IUseCaseHandler {
    int state = 0;
    boolean isDone = false;
    LocalDate date = null;
    LocalTime time = null;
    String title = null;
    Set<Client> participants = null;
    Duration duration = null;

    @Override
    public Response handleRequest(Request request, IDataStorage dataStorage, ClientIdentificationHandler clientIdentificationHandler) {
        if (state == 0) {
            state++;
            return new Response("Введите название встречи");
        } else if (state == 1) {
            title = request.text();
            state++;
            return new Response("Введите дату встречи в формате ДД.ММ", new String[][]{{"Сегодня"}, {"Завтра"}, {"Послезавтра"}});
        } else if (state == 2) {
            String text = request.text();
            if (!DateProcessor.isDate(text)) {
                return new Response("""
                        Неверный формат даты, попробуйте еще раз.
                        Введите дату встречи в формате ДД.ММ""", new String[][]{{"Сегодня"}, {"Завтра"}, {"Послезавтра"}});
            }
            date = DateProcessor.getDate(request.text());
            state++;
            return new Response("Введите время встречи в формате ЧЧ:ММ", new String[][]{{"6:00", "7:00", "8:00", "9:00"}, {"10:00", "11:00", "12:00", "13:00"}, {"14:00", "15:00", "16:00", "17:00"}, {"18:00", "19:00", "20:00", "21:00"}});

        } else if (state == 3) {
            String text = request.text();
            if (!TimeProcessor.isTime(text)) {
                return new Response("""
                        Неверный формат времени, попробуйте еще раз.
                        Введите время встречи в формате ЧЧ:ММ""", new String[][]{{"6:00", "7:00", "8:00", "9:00"}, {"10:00", "11:00", "12:00", "13:00"}, {"14:00", "15:00", "16:00", "17:00"}, {"18:00", "19:00", "20:00", "21:00"}});
            }
            time = TimeProcessor.getTime(request.text());
            state++;
            return new Response("Введите продолжительность встречи в формате ЧЧ:ММ", new String[][]{{"00:30"}, {"1:00"}, {"1:30"}, {"2:00"}});
        } else if (state == 4) {
            String text = request.text();
            if (!TimeProcessor.isTime(text)) {
                return new Response("""
                        Неверный формат времени, попробуйте еще раз.
                        Введите продолжительность встречи в формате ЧЧ:ММ""", new String[][]{{"00:30"}, {"1:00"}, {"1:30"}, {"2:00"}});
            }
            duration = Duration.between(LocalTime.of(0, 0), TimeProcessor.getTime(request.text()));
            state++;
            return new Response("Введите с кем хотите встретиться");
        } else if (state == 5) {
            participants = new HashSet<>();
            participants.add(request.requestOwner());
            participants.add(clientIdentificationHandler.getClient(request.text()));

            dataStorage.addMeeting(new Meeting(title, LocalDateTime.of(date, time), LocalDateTime.of(date, time).plusSeconds(duration.getSeconds()), participants, ""));

            state++;
            isDone = true;
        }

        return new Response("Добавление встречи завершено.");
    }

    @Override
    public boolean isDone() {
        return isDone;
    }
}

