FROM adoptopenjdk/openjdk15:alpine-jre

WORKDIR /iotsimulator

COPY ./iotsimulator/target/iotsimulator-*.jar /iotsimulator/iotsimulator.jar

ENTRYPOINT ["java", "-jar", "iotsimulator.jar"]