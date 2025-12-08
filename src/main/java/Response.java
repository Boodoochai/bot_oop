import org.checkerframework.checker.nullness.qual.NonNull;

final public class Response {
    private @NonNull final String text;

    Response(@NonNull final String text) {
        this.text = text;
    }

    public @NonNull String getText() {
        return this.text;
    }
}

