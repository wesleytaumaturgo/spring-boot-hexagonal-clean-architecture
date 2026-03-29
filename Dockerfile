# ─────────────────────────────────────────────
# Stage 1: dependency cache
# ─────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21-alpine AS deps
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

# ─────────────────────────────────────────────
# Stage 2: build
# ─────────────────────────────────────────────
FROM deps AS build
COPY src ./src
RUN mvn clean package -DskipTests -B -q

# ─────────────────────────────────────────────
# Stage 3: development (default target)
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS development
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=build /app/target/*.jar app.jar
RUN chown appuser:appgroup app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# ─────────────────────────────────────────────
# Stage 4: production
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS production
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=build /app/target/*.jar app.jar
RUN chown appuser:appgroup app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", \
  "-Xmx512m", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
