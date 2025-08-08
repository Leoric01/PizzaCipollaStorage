# 1. BUILD stage (používá Gradle a JDK)
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

# 2. RUNTIME stage (čisté JDK s hotovým JARem)
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "app.jar"]