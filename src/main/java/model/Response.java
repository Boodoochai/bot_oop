package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Response(@NotNull String text, @Nullable String[][] options, boolean isTempOptions) {
    public Response(@NotNull String text, @Nullable String[][] options) {
        this(text, options, true);
    }

    public Response(@NotNull String text) {
        this(text, null, true);
    }
}

