FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM openjdk:17-jdk-slim-buster
EXPOSE 8082:8082
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/ApiProducto-0.0.1-SNAPSHOT.jar /app/ApiProducto.jar
ENTRYPOINT ["java", "-jar", "/app/ApiProducto.jar"]