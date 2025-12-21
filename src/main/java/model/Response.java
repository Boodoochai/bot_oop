package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Response(@NotNull String text, @Nullable String[][] options) {
    public Response(@NotNull String text) {
        this(text, null);
    }
}

