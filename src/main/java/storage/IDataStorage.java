package storage;

import backend.automaton.IAutomaton;
import backend.useCases.handlers.IUseCaseHandler;
import model.Client;
import model.Meeting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IDataStorage {
    Client clientById(final UUID clientUUID);

    Client clientByName(final String clientName);

    boolean isExistClientById(final UUID clientUUID);

    boolean isExistClientByName(final String clientName);

    void putClientById(final UUID uuid, final Client client);

    void putClientByName(final String name, final Client client);

    void addMeeting(final Meeting meeting);

    List<Meeting> getMeetingsBetween(final LocalDateTime from, final LocalDateTime to);

    List<Meeting> getMeetingsForClientBetween(final Client client, final LocalDateTime from, final LocalDateTime to);

    List<Meeting> getMeetingsWithClient(final Client client);

    boolean isExistAutomation(UUID uuid);

    IAutomaton getAutomation(UUID uuid);

    void setAutomation(UUID uuid, IAutomaton automaton);

    boolean isExistUseCaseHandler(UUID uuid);

    void setUseCaseHandler(UUID uuid, IUseCaseHandler useCaseHandler);

    IUseCaseHandler getUseCaseHandler(UUID uuid);

    void deleteUseCaseHandler(UUID uuid);
}
