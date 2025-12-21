package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Немутабельная конфигурация приложения.
 */
public final class ApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private final Mode mode;
    private final Interface iface;
    private final String botToken;

    private ApplicationConfig(Builder builder) {
        this.mode = Objects.requireNonNull(builder.mode, "Mode не может быть null");
        this.iface = Objects.requireNonNull(builder.iface, "Interface не может быть null");
        this.botToken = builder.botToken;

        logger.debug("Создана конфигурация: mode={}, interface={}, botToken={}",
                mode, iface, botToken != null ? "***" : "отсутствует");
    }

    public static Builder builder() {
        logger.trace("Создание нового билдера конфигурации");
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

    public enum Mode {
        PRODUCTION, DEMO
    }

    public enum Interface {
        CONSOLE, TELEGRAM
    }

    public static class Builder {
        private Mode mode = Mode.PRODUCTION;
        private Interface iface = Interface.TELEGRAM;
        private String botToken;

        public Builder mode(Mode mode) {
            logger.debug("Установка режима: {}", mode);
            this.mode = mode;
            return this;
        }

        public Builder interfaceType(Interface iface) {
            logger.debug("Установка интерфейса: {}", iface);
            this.iface = iface;
            return this;
        }

        public Builder botToken(String botToken) {
            logger.debug("Установка botToken: {}", botToken != null ? "***" : "null");
            this.botToken = botToken;
            return this;
        }

        public ApplicationConfig build() {
            logger.info("Сборка конфигурации: mode={}, interface={}",
                    mode, iface);
            return new ApplicationConfig(this);
        }
    }
}