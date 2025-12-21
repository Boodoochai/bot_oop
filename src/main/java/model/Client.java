package model;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Client(@NotNull UUID clientId, @NotNull String name) {
}
