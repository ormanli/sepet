FROM openjdk:14-slim

WORKDIR /collector

COPY ./collector/target/collector-*.jar /collector/collector.jar

CMD ["java", "-jar", "collector.jar"]