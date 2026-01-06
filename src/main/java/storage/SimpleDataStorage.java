package storage;

import Logger.ILogger;
import Logger.LoggerProvider;
import backend.automaton.IAutomaton;
import backend.useCases.handlers.IUseCaseHandler;
import model.Client;
import model.Meeting;

import java.time.LocalDateTime;
import java.util.*;

final public class SimpleDataStorage implements IDataStorage {
    private static final ILogger logger = LoggerProvider.get(SimpleDataStorage.class);

    private final Map<UUID, Client> clientByUUID;
    private final Map<String, Client> clientByName;
    private final List<Meeting> meetings;
    private final Map<Client, IAutomaton> automatons;
    private final Map<Client, IUseCaseHandler> useCaseHandlers;

    public SimpleDataStorage() {
        clientByUUID = new HashMap<>();
        clientByName = new HashMap<>();
        meetings = new ArrayList<>();
        automatons = new HashMap<>();
        useCaseHandlers = new HashMap<>();
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
        clientByName.put(client.name(), client);
        logger.debug("Клиент добавлен по UUID: {} → {}", uuid, client.name());
    }

    @Override
    public void putClientByName(final String name, final Client client) {
        clientByName.put(name, client);
        clientByUUID.put(client.clientId(), client);
        logger.debug("Клиент добавлен по имени: '{}' → {}", name, client.name());
    }

    @Override
    public void addMeeting(final Meeting meeting) {
        meetings.add(meeting);
        logger.debug("Встреча добавлена: {} участников, время: {} - {}", meeting.participants().size(), meeting.start(), meeting.end());
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
    public List<Meeting> getMeetingsForClientBetween(final Client client, final LocalDateTime from, final LocalDateTime to) {
        final List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.participants().contains(client) && !m.end().isBefore(from) && !m.start().isAfter(to)) {
                result.add(m);
            }
        }
        logger.debug("Запрос встреч для клиента '{}' между {} и {}: найдено {} встреч", client.name(), from, to, result.size());
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
        boolean exists = automatons.containsKey(clientById(uuid));
        logger.debug("Проверка существования автомата для клиента {}: {}", uuid, exists);
        return exists;
    }

    @Override
    public IAutomaton getAutomation(UUID uuid) {
        Client client = clientById(uuid);
        if (client == null) {
            logger.warn("Клиент с UUID {} не найден при запросе автомата", uuid);
            return null;
        }
        IAutomaton automaton = automatons.get(client);
        logger.debug("Получен автомат для клиента {}: {}", uuid, automaton != null ? automaton.getClass().getSimpleName() : "отсутствует");
        return automaton;
    }

    @Override
    public void setAutomation(UUID uuid, IAutomaton automaton) {
        Client client = clientById(uuid);
        if (client == null) {
            logger.warn("Клиент с UUID {} не найден, автомат не может быть установлен", uuid);
            return;
        }
        logger.info("Установка автомата {} для клиента {}", automaton.getClass().getSimpleName(), uuid);
        automatons.put(client, automaton);
    }

    @Override
    public boolean isExistUseCaseHandler(UUID uuid) {
        Client client = clientById(uuid);
        if (client == null) {
            logger.debug("Клиент с UUID {} не найден при проверке обработчика use case", uuid);
            return false;
        }
        boolean exists = useCaseHandlers.containsKey(client);
        logger.debug("Проверка существования обработчика use case для клиента {}: {}", uuid, exists);
        return exists;
    }

    @Override
    public void setUseCaseHandler(UUID uuid, IUseCaseHandler useCaseHandler) {
        Client client = clientById(uuid);
        if (client == null) {
            logger.warn("Клиент с UUID {} не найден, обработчик use case не может быть установлен", uuid);
            return;
        }
        logger.info("Установка обработчика use case {} для клиента {}", useCaseHandler.getClass().getSimpleName(), uuid);
        useCaseHandlers.put(client, useCaseHandler);
    }

    @Override
    public IUseCaseHandler getUseCaseHandler(UUID uuid) {
        Client client = clientById(uuid);
        if (client == null) {
            logger.warn("Клиент с UUID {} не найден при запросе обработчика use case", uuid);
            return null;
        }
        IUseCaseHandler handler = useCaseHandlers.get(client);
        logger.debug("Получен обработчик use case для клиента {}: {}", uuid, handler != null ? handler.getClass().getSimpleName() : "отсутствует");
        return handler;
    }

    @Override
    public void deleteUseCaseHandler(UUID uuid) {
        Client client = clientById(uuid);
        if (client == null) {
            logger.warn("Клиент с UUID {} не найден, обработчик use case не может быть удалён", uuid);
            return;
        }
        IUseCaseHandler removed = useCaseHandlers.remove(client);
        if (removed != null) {
            logger.info("Обработчик use case {} удалён для клиента {}", removed.getClass().getSimpleName(), uuid);
        } else {
            logger.debug("Обработчик use case для клиента {} не найден при удалении", uuid);
        }
    }
}