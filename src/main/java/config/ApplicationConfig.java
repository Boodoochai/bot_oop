package config;

import java.util.Objects;

/**
 * Немутабельная конфигурация приложения.
 */
public final class ApplicationConfig {
    private final Mode mode;
    private final Interface iface;
    private final String botToken;
    private ApplicationConfig(Builder builder) {
        this.mode = Objects.requireNonNull(builder.mode);
        this.iface = Objects.requireNonNull(builder.iface);
        this.botToken = builder.botToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Mode getMode() {
        return mode;
    }

    public Interface getInterface() {
        return iface;
    }

    public String getBotToken() {
        return botToken;
    }

    public enum Mode {PRODUCTION, DEMO}

    public enum Interface {CONSOLE, TELEGRAM}

    public static class Builder {
        private Mode mode = Mode.PRODUCTION;
        private Interface iface = Interface.TELEGRAM;
        private String botToken;

        public Builder mode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder interfaceType(Interface iface) {
            this.iface = iface;
            return this;
        }

        public Builder botToken(String botToken) {
            this.botToken = botToken;
            return this;
        }

        public ApplicationConfig build() {
            return new ApplicationConfig(this);
        }
    }
}