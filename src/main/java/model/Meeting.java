package model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record Meeting(@NotNull UUID uuid, @NotNull String title, @NotNull LocalDateTime start,
                      @NotNull LocalDateTime end,
                      @NotNull Set<Client> participants, @NotNull String description) {

    public Meeting(@NotNull String title, @NotNull LocalDateTime start, @NotNull LocalDateTime end,
                   @NotNull Set<Client> participants, @NotNull String description) {
        this(UUID.randomUUID(), title, start, end, participants, description);
    }
}
