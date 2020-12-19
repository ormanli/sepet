FROM adoptopenjdk/openjdk15:alpine-jre

WORKDIR /server

COPY ./server/target/server-*.jar /server/server.jar

CMD ["java", "-jar", "server.jar"]