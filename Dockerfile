FROM eclipse-temurin:8-jdk
WORKDIR /app
COPY build/libs/injectable-pet-store-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
