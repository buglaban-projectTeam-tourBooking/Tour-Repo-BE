FROM eclipse-temurin:17-jre-jammy

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api-service.jar

ENTRYPOINT ["java", "-jar", "api-service.jar"]
EXPOSE 8081
