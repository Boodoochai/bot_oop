public class Request {
    private final Client requestOwner;
    private final String text;

    Request(final Client requestOwner, final String requestText) {
        this.requestOwner = requestOwner;
        this.text = requestText;
    }

    public Client getRequestOwner() {
        return requestOwner;
    }

    public String getText() {
        return text;
    }
}
