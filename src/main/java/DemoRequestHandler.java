import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final public class DemoRequestHandler implements IRequestHandler{
    public @NonNull Response handleRequest(final @NonNull Request request) {
        return new Response("heh");
    }
}