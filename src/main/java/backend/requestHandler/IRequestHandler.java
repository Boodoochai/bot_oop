package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import model.Request;
import model.Response;
import storage.IDataStorage;

public abstract class IRequestHandler {
    final IDataStorage dataStorage;

    final ClientIdentificationHandler clientIdentificationHandler;

    IRequestHandler(final IDataStorage dataStorage, final ClientIdentificationHandler clientIdentificationHandler) {
        this.dataStorage = dataStorage;
        this.clientIdentificationHandler = clientIdentificationHandler;
    }

    public abstract Response handleRequest(final Request request);
}
