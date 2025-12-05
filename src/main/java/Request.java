import org.checkerframework.checker.nullness.qual.NonNull;

final public class Request {
    private @NonNull final Client requestOwner;
    private @NonNull final String text;

    Request(@NonNull final Client requestOwner, @NonNull final String requestText) {
        this.requestOwner = requestOwner;
        this.text = requestText;
    }

    public @NonNull Client getRequestOwner() {
        return requestOwner;
    }

    public @NonNull String getText() {
        return text;
    }
}
