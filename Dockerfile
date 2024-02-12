FROM openjdk:17-alpine
ARG JAR_PATH=/build/libs/*.jar
COPY ${JAR_PATH} /home/server.jar
ARG OPEN_AI_KEY
ENV OPEN_AI_KEY=${OPEN_AI_KEY}
ARG DEV_DB_HOST
ENV DEV_DB_HOST=${DEV_DB_HOST}
ARG DEV_DB_PASSWORD
ENV DEV_DB_PASSWORD=${DEV_DB_PASSWORD}
ARG DEV_DB_USER
ENV DEV_DB_USER=${DEV_DB_USER}
ARG PROD_DB_HOST
ENV PROD_DB_HOST=${PROD_DB_HOST}
ARG PROD_DB_PASSWORD
ENV PROD_DB_PASSWORD=${PROD_DB_PASSWORD}
ARG PROD_DB_USER
ENV PROD_DB_USER=${PROD_DB_USER}
ARG ACTIVE_PROFILE
ENV ACTIVE_PROFILE=${ACTIVE_PROFILE}
ARG S3_BUCKET
ENV S3_BUCKET=${S3_BUCKET}
ARG REGION
ENV REGION=${REGION}
ARG ACCESS_KEY
ENV ACCESS_KEY=${ACCESS_KEY}
ARG SECRET_KEY
ENV SECRET_KEY=${SECRET_KEY}
ARG KAKAO_REDIRECT_URI
ENV KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI}
ARG KAKAO_API_KEY
ENV KAKAO_API_KEY=${KAKAO_API_KEY}
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone
ENTRYPOINT ["java","-jar","/home/server.jar"]