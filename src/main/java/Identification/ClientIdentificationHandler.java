package Identification;

import model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.IDataStorage;

import java.util.UUID;

final public class ClientIdentificationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientIdentificationHandler.class);

    private final IDataStorage dataStorage;

    public ClientIdentificationHandler(final IDataStorage dataStorage) {
        this.dataStorage = dataStorage;
        logger.debug("Инициализирован ClientIdentificationHandler с хранилищем: {}", dataStorage.getClass().getSimpleName());
    }

    private void registerNewClient(final String name) {
        final UUID newClientUUID = UUID.randomUUID();
        final Client newClient = new Client(newClientUUID, name);
        dataStorage.putClientByName(name, newClient);
        logger.debug("Зарегистрирован новый клиент: имя='{}', UUID={}", name, newClientUUID);
    }

    private void registerNewClient(final UUID uuid) {
        final Client newClient = new Client(uuid, "");
        dataStorage.putClientById(uuid, newClient);
        logger.debug("Зарегистрирован новый клиент по UUID: {}", uuid);
    }

    public Client getClient(final String name) {
        if (!dataStorage.isExistClientByName(name)) {
            logger.info("Клиент с именем '{}' не найден, создаём нового", name);
            registerNewClient(name);
        } else {
            logger.debug("Найден существующий клиент: имя='{}'", name);
        }
        return dataStorage.clientByName(name);
    }

    public Client getClient(final UUID uuid) {
        if (!dataStorage.isExistClientById(uuid)) {
            logger.info("Клиент с UUID '{}' не найден, создаём нового", uuid);
            registerNewClient(uuid);
        } else {
            logger.debug("Найден существующий клиент: UUID={}", uuid);
        }
        return dataStorage.clientById(uuid);
    }
}