package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
import model.Request;
import model.Response;
import storage.IDataStorage;

public interface IUseCaseHandler {
    Response handleRequest(Request request, IDataStorage dataStorage, ClientIdentificationHandler clientIdentificationHandler);

    boolean isDone();
}
