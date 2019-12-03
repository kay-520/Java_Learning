FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD eurka-1.0-SNAPSHOT.jar app.jar
#RUN bash -c 'touch /app.jar'
EXPOSE 8000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]