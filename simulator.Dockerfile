FROM openjdk:14-slim

WORKDIR /iotsimulator

COPY ./iotsimulator/target/iotsimulator-*.jar /iotsimulator/iotsimulator.jar

ENTRYPOINT ["java", "-jar", "iotsimulator.jar"]