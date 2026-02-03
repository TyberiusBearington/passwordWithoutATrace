# Build stage
FROM gradle:jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN ./gradlew build --no-daemon -x test

# Run stage
FROM eclipse-temurin:17-jre
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
