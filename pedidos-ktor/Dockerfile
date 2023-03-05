FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildJar --no-daemon

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/servicio-pedidos.jar /app/servicio-pedidos.jar
ENTRYPOINT ["java","-jar","/app/servicio-pedidos.jar"]