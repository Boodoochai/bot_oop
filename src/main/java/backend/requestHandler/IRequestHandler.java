package backend.requestHandler;

import model.Request;
import model.Response;

import java.util.List;

public interface IRequestHandler {
    public abstract List<Response> handleRequest(final Request request);
}
