package backend.useCases.handlers;

import model.Request;
import model.Response;
import storage.IDataStorage;

public interface IUseCaseHandler {
    Response handleRequest(Request request, IDataStorage dataStorage);

    boolean isDone();
}
