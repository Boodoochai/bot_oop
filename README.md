### Проект по ООП

---

#### To run use:
```
./gradlew run --args="base console" -q --console=plain
```
or
```
./gradlew run --args="base telegram"
```

---

#### To test use:
```
./gradlew clean test
```

#### To test with coverage use:
```
./gradlew clean test jacocoTestReport
```

---

sudo docker run -p 8080:8080 -e BOT_TOKEN=8052483079:AAHFu7aZD_aHn0AL-Qq8SXSxrYQA9E2QtKQ tg-bot

sudo docker build -t tg-bot .

