import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

final public class Request {
    private @NonNull final Client requestOwner;
    private @NonNull final String text;

    Request(@NonNull final Client requestOwner, @NonNull final String requestText) {
        if (requestOwner == null || requestText == null) {
            throw new NullPointerException();
        }
        this.requestOwner = requestOwner;
        this.text = requestText;
    }

    public @NonNull Client getRequestOwner() {
        return requestOwner;
    }

    public @NonNull String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return requestOwner.equals(request.requestOwner) && text.equals(request.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestOwner, text);
    }
}
