package backend.requestHandler;

import model.Request;
import model.Response;

public interface IRequestHandler {
    public abstract Response handleRequest(final Request request);
}
