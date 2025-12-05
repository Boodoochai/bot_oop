import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

final public class Meeting {
    private final @NonNull UUID id;
    private final @NonNull String title;
    private final @NonNull LocalDateTime start;
    private final @NonNull LocalDateTime end;
    private final @NonNull Set<Client> participants;
    private final @NonNull String description;

    public Meeting(
            @NonNull final String title,
            @NonNull final LocalDateTime start,
            @NonNull final LocalDateTime end,
            @NonNull final Set<Client> participants,
            @NonNull final String description
    ) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.start = start;
        this.end = end;
        this.participants = participants;
        this.description = description;
    }

    public @NonNull UUID getId() { return id; }
    public @NonNull String getTitle() { return title; }
    public @NonNull LocalDateTime getStart() { return start; }
    public @NonNull LocalDateTime getEnd() { return end; }
    public @NonNull Set<Client> getParticipants() { return participants; }
    public @NonNull String getDescription() { return description; }
}
