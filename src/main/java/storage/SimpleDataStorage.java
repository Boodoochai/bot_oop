package storage;

import backend.automaton.IAutomaton;
import model.Client;
import model.Meeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

final public class SimpleDataStorage implements IDataStorage {
    private static final Logger logger = LoggerFactory.getLogger(SimpleDataStorage.class);

    private final Map<UUID, Client> clientByUUID;
    private final Map<String, Client> clientByName;
    private final List<Meeting> meetings;
    private final Map<Client, IAutomaton> automatons;

    public SimpleDataStorage() {
        clientByUUID = new HashMap<>();
        clientByName = new HashMap<>();
        meetings = new ArrayList<>();
        automatons = new HashMap<>();
        logger.debug("Инициализировано хранилище: SimpleDataStorage");
    }

    @Override
    public Client clientById(final UUID clientUUID) {
        Client client = clientByUUID.get(clientUUID);
        logger.debug("Запрос клиента по UUID: {} → найден: {}", clientUUID, client != null);
        return client;
    }

    @Override
    public Client clientByName(final String clientName) {
        Client client = clientByName.get(clientName);
        logger.debug("Запрос клиента по имени: '{}' → найден: {}", clientName, client != null);
        return client;
    }

    @Override
    public boolean isExistClientById(final UUID clientUUID) {
        boolean exists = clientByUUID.containsKey(clientUUID);
        logger.debug("Проверка существования клиента по UUID: {} → {}", clientUUID, exists);
        return exists;
    }

    @Override
    public boolean isExistClientByName(final String clientName) {
        boolean exists = clientByName.containsKey(clientName);
        logger.debug("Проверка существования клиента по имени: '{}' → {}", clientName, exists);
        return exists;
    }

    @Override
    public void putClientById(final UUID uuid, final Client client) {
        clientByUUID.put(uuid, client);
        logger.debug("Клиент добавлен по UUID: {} → {}", uuid, client.name());
    }

    @Override
    public void putClientByName(final String name, final Client client) {
        clientByName.put(name, client);
        logger.debug("Клиент добавлен по имени: '{}' → {}", name, client.name());
    }

    @Override
    public void addMeeting(final Meeting meeting) {
        meetings.add(meeting);
        logger.debug("Встреча добавлена: {} участников, время: {} - {}",
                meeting.participants().size(), meeting.start(), meeting.end());
    }

    @Override
    public List<Meeting> getMeetingsBetween(final LocalDateTime from, final LocalDateTime to) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (!m.end().isBefore(from) && !m.start().isAfter(to)) {
                result.add(m);
            }
        }
        logger.debug("Запрос встреч между {} и {}: найдено {} встреч", from, to, result.size());
        return result;
    }

    @Override
    public List<Meeting> getMeetingsForClientBetween(
            final Client client,
            final LocalDateTime from,
            final LocalDateTime to
    ) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.participants().contains(client)
                    && !m.end().isBefore(from)
                    && !m.start().isAfter(to)) {
                result.add(m);
            }
        }
        logger.debug("Запрос встреч для клиента '{}' между {} и {}: найдено {} встреч",
                client.name(), from, to, result.size());
        return result;
    }

    @Override
    public List<Meeting> getMeetingsWithClient(final Client client) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.participants().contains(client)) {
                result.add(m);
            }
        }
        logger.debug("Запрос всех встреч для клиента '{}': найдено {} встреч", client.name(), result.size());
        return result;
    }

    @Override
    public boolean isExistAutomation(UUID uuid) {
        return automatons.containsKey(clientById(uuid));
    }

    @Override
    public IAutomaton getAutomation(UUID uuid) {
        return automatons.get(clientById(uuid));
    }

    @Override
    public void setAutomation(UUID uuid, IAutomaton automaton) {
        automatons.put(clientById(uuid), automaton);
    }
}