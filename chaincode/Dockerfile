# ── Build stage ───────────────────────────────────────────────────────
FROM gradle:9.2.1-jdk25-alpine AS builder
WORKDIR /home/gradle/

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ gradle/

RUN gradle --no-daemon dependencies

COPY src/ src/
RUN gradle --no-daemon shadowJar


# ── Run stage ─────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-alpine
ARG CC_SERVER_PORT=9999
WORKDIR /app/

RUN addgroup -S javauser && adduser -S javauser -G javauser
USER appuser

COPY --from=builder /home/gradle/build/libs/chaincode.jar chaincode.jar

EXPOSE $CC_SERVER_PORT
ENTRYPOINT ["java", "-jar", "chaincode.jar"]
