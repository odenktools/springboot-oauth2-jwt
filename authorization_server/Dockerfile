FROM openjdk:8-jdk-alpine

ENV TZ Asia/Jakarta
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS
RUN apk update && apk add --no-cache bash tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

USER root

VOLUME ["/opt/odenktools", "/tmp"]

ARG JAR_FILE

COPY ${JAR_FILE} authserver.jar

EXPOSE 8090

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/authserver.jar"]
