FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} auth-application.jar
ENTRYPOINT ["java","-jar","/auth-application.jar"]