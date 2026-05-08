FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn -pl report-service -am -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/report-service/target/report-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9113
ENTRYPOINT ["java", "-jar", "app.jar"]
