# Fase di build
FROM gradle:7.6-jdk8 as builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:8-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/injectable-pet-store-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
