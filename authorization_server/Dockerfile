FROM alpine:3.8

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8
ENV TZ Asia/Jakarta

# add a simple script that can auto-detect the appropriate JAVA_HOME value
# based on whether the JDK or only the JRE is installed
RUN { \
		echo '#!/bin/sh'; \
		echo 'set -e'; \
		echo; \
		echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
	} > /usr/local/bin/docker-java-home \
	&& chmod +x /usr/local/bin/docker-java-home

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk

ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin

ENV JAVA_VERSION 8u222

RUN set -x \
	&& apk add --update \
		openjdk8 bash tzdata && \
		rm -rf /var/cache/apk/* && \
		[ "$JAVA_HOME" = "$(docker-java-home)" ] && \
	ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
	echo $TZ > /etc/timezone

ARG JAR_FILE

COPY ${JAR_FILE} /usr/local/bin/authserver.jar

COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh

RUN chmod +x /usr/local/bin/wait-for-it.sh

EXPOSE 8090

RUN mkdir /applog

RUN mkdir /authorization-server

VOLUME ["/applog", "/authorization-server"]

STOPSIGNAL SIGTERM

#EOF