FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildJar --no-daemon

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
RUN mkdir /cert
COPY --from=build /home/gradle/src/cert/* /cert/
COPY --from=build /home/gradle/src/build/libs/apiGeneral.jar /app/apiGeneral.jar
ENTRYPOINT ["java","-jar","/app/apiGeneral.jar"]