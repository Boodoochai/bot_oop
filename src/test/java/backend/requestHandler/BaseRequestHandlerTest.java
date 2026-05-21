package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import backend.automaton.IAutomatonFactory;
import backend.automaton.base.BaseAutomatonFactory;
import backend.automaton.base.BaseTransitionTableFactory;
import backend.useCases.providers.BaseUseCaseProvider;
import model.Client;
import model.Request;
import model.Response;
import org.junit.jupiter.api.Test;
import storage.SimpleDataStorage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseRequestHandlerTest {

    @Test
    void helpShowsOptions() {
        SimpleDataStorage storage = new SimpleDataStorage();
        ClientIdentificationHandler idHandler = new ClientIdentificationHandler(storage);
        IAutomatonFactory automatonFactory = new BaseAutomatonFactory(new BaseTransitionTableFactory());
        BaseUseCaseProvider useCaseProvider = new BaseUseCaseProvider();
        BaseRequestHandler handler = new BaseRequestHandler(storage, idHandler, automatonFactory, useCaseProvider);

        Client owner = idHandler.getClient("alice");
        List<Response> responses = handler.handleRequest(new Request(owner, "Помощь"));

        assertEquals(1, responses.size());
        Response response = responses.get(0);
        assertNotNull(response.options());
        assertTrue(flatten(response.options()).contains("Создать встречу"));
    }

    @Test
    void createMeetingStartsUseCase() {
        SimpleDataStorage storage = new SimpleDataStorage();
        ClientIdentificationHandler idHandler = new ClientIdentificationHandler(storage);
        IAutomatonFactory automatonFactory = new BaseAutomatonFactory(new BaseTransitionTableFactory());
        BaseUseCaseProvider useCaseProvider = new BaseUseCaseProvider();
        BaseRequestHandler handler = new BaseRequestHandler(storage, idHandler, automatonFactory, useCaseProvider);

        Client owner = idHandler.getClient("alice");
        handler.handleRequest(new Request(owner, "Помощь"));

        List<Response> responses = handler.handleRequest(new Request(owner, "Создать встречу"));
        assertEquals(1, responses.size());
        assertEquals("Введите название встречи", responses.get(0).text());
    }

    private static List<String> flatten(String[][] options) {
        if (options == null) {
            return List.of();
        }
        return Stream.of(options).flatMap(Arrays::stream).toList();
    }
}
