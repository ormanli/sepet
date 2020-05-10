FROM openjdk:14-slim

WORKDIR /server

COPY ./server/target/server-*.jar /server/server.jar

CMD ["java", "-jar", "server.jar"]