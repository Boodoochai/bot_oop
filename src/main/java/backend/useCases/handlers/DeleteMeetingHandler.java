package backend.useCases.handlers;

import Identification.ClientIdentificationHandler;
import model.Request;
import model.Response;
import storage.IDataStorage;
//TODO
public class DeleteMeetingHandler implements IUseCaseHandler {
    @Override
    public Response handleRequest(Request request, IDataStorage dataStorage, ClientIdentificationHandler clientIdentificationHandler) {
        return null;
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
