FROM openjdk:17

VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=releases/JotJungle-1.3/lib/server-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]