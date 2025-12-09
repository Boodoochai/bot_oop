# ---------- Stage 1: Build ----------
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Копируем только Gradle-файлы для кэширования зависимостей
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Прогреваем зависимости (не обязательно, но ускоряет сборку)
RUN gradle dependencies --no-daemon || true

# Копируем исходники
COPY src ./src

# Сборка fat JAR (shadowJar)
RUN gradle shadowJar --no-daemon


# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Копируем собранный jar
COPY --from=builder /app/build/libs/*-all.jar /app/app.jar

# Указываем порт (Amvera будет его проксировать)
EXPOSE 80

# Переменная окружения (пустая, значение подаётся на сервере)
ENV BOT_TOKEN=""

# Команда запуска — именно её и нужно указать в `command` в Amvera UI
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "/app/app.jar"]
