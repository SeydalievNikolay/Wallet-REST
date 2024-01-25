FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} wallet-app.jar
ENTRYPOINT ["java","-jar","/wallet-app.jar"]