FROM openjdk:11-jre-slim

WORKDIR /iotsimulator

COPY ./iotsimulator/target/iotsimulator-*.jar /iotsimulator/iotsimulator.jar

ENTRYPOINT ["java", "-jar", "iotsimulator.jar"]