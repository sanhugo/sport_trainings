# syntax=docker/dockerfile:1

# ---------- build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
# gradlew может приехать с CRLF из Windows-checkout'а
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Кэшируем зависимости отдельным слоем
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon dependencies > /dev/null || true

COPY src ./src
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon bootJar -x test \
    && cp build/libs/*[!n].jar app.jar

# ---------- runtime ----------
FROM eclipse-temurin:21-jre
# JFreeChart рисует PNG через AWT: нужны fontconfig и шрифты (DejaVu покрывает кириллицу)
RUN apt-get update \
    && apt-get install -y --no-install-recommends fontconfig fonts-dejavu-core \
    && rm -rf /var/lib/apt/lists/* \
    && useradd --system --uid 10001 app

WORKDIR /app
COPY --from=build /workspace/app.jar app.jar
USER app

ENV JAVA_TOOL_OPTIONS="-Djava.awt.headless=true -XX:MaxRAMPercentage=75"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
