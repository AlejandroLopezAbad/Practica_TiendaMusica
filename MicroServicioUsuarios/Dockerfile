FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM openjdk:17-jdk-slim-buster
EXPOSE 8081:8081
RUN mkdir /app
RUN mkdir /cert
COPY --from=build /home/gradle/src/build/libs/MicroServicioUsuarios-0.0.1-SNAPSHOT.jar /app/MicroServicioUsuarios.jar
ENTRYPOINT ["java", "-jar", "/app/MicroServicioUsuarios.jar"]