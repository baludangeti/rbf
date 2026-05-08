FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn -pl billing-service -am -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/billing-service/target/billing-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9105
ENTRYPOINT ["java", "-jar", "app.jar"]
