FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .
COPY settings.xml /root/.m2/settings.xml
RUN mvn clean package -pl notify-service -am -DskipTests spring-boot:repackage

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/notify-service/target/notify-service-1.0-SNAPSHOT.jar app.jar

EXPOSE 8181

ENTRYPOINT ["java", "-jar", "app.jar"]