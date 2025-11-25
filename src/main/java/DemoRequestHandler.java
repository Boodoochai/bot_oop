public class DemoRequestHandler implements IRequestHandler{
    public Response handleRequest(Request request) {
        return new Response("heh");
    }
}
