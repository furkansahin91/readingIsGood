FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.10_9
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} readingisgood-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/readingisgood-0.0.1-SNAPSHOT.jar"]