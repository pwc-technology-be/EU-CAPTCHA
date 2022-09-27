FROM adoptopenjdk/openjdk8
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/EuCaptcha.war eucaptcha.war
EXPOSE 8080
# ENTRYPOINT exec java $JAVA_OPTS -jar eucaptcha.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar eucaptcha.war
