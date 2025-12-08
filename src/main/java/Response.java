import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

final public class Response {
    private @NonNull final String text;

    Response(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text cannot be null");
        }
        this.text = text;
    }

    public @NonNull String getText() {
        return this.text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response response = (Response) o;
        return text.equals(response.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return "Response{" +
                "text='" + text + '\'' +
                '}';
    }
}

