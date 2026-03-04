# --- ЭТАП 1: Сборка (Build) ---
# Используем образ с полным JDK и Maven для компиляции
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Сначала копируем ТОЛЬКО pom.xml, чтобы закешировать зависимости
COPY pom.xml .

# 2. Скачиваем зависимости. Если pom.xml не менялся, этот шаг пропустится при пересборке
RUN mvn dependency:go-offline -B

# 3. Теперь копируем исходный код
COPY src ./src

# 4. Собираем проект. Библиотеки уже в образе, поэтому сборка будет быстрой
RUN mvn clean package -DskipTests

# --- ЭТАП 2: Запуск (Run) ---
# Используем максимально легкий образ только с JRE (без компиляторов и папок .m2)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 5. Копируем ТОЛЬКО готовый jar-файл из этапа сборки
# Мы переименовываем его в app.jar для удобства
COPY --from=build /app/target/*.jar app.jar

# 6. Указываем порт
EXPOSE 8080

# 7. Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]