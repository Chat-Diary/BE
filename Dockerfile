FROM openjdk:17-alpine
ARG JAR_PATH=/build/libs/*.jar
COPY ${JAR_PATH} /home/server.jar
ENTRYPOINT ["java","-jar","/home/server.jar"]
ARG OPEN_AI_KEY
ENV OPEN_AI_KEY=${OPEN_AI_KEY}