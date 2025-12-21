package backend.requestHandler;

import Identification.ClientIdentificationHandler;
import model.Request;
import model.Response;
import storage.IDataStorage;

final public class DemoRequestHandler extends IRequestHandler {

    public DemoRequestHandler(final IDataStorage dataStorage, final ClientIdentificationHandler clientIdentificationHandler) {
        super(dataStorage, clientIdentificationHandler);
    }

    public Response handleRequest(final Request request) {
        final String res = request.requestOwner().clientId().toString();
        return new Response(res);
    }
}