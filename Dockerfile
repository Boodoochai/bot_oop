# ---------- Stage 1: Build ----------
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Копируем Gradle-файлы
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Прогреваем зависимости
RUN gradle dependencies --no-daemon || true

# Копируем исходники
COPY src ./src

# Сборка fat JAR
RUN gradle shadowJar --no-daemon


# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Копируем JAR (с учётом имени)
COPY --from=builder /app/build/libs/telegram-bot-1.0.0.jar /app/app.jar

# Amvera проксирует на 80 порт → ваш бот не должен его слушать
# EXPOSE 80 ← можно убрать, если бот не открывает HTTP-порт

# Переменная для токена
ENV BOT_TOKEN=""

# Запуск
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "/app/app.jar"]