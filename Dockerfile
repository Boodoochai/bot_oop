# Этап 1: Сборка (build stage)
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

# Собираем fat JAR
RUN gradle shadowJar --no-daemon

# Этап 2: Запуск (runtime stage)
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Копируем JAR из этапа сборки
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Порт (если нужно)
EXPOSE 80

# Переменная для токена бота
ENV BOT_TOKEN=""

# Проверка токена и запуск
CMD if [ -z "$BOT_TOKEN" ]; then \
      echo "Ошибка: BOT_TOKEN не задан! Используйте -e BOT_TOKEN=ваш_токен"; \
      exit 1; \
    else \
      java -Dfile.encoding=UTF-8 -jar /app/app.jar; \
    fi