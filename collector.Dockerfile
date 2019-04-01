FROM openjdk:11-jre-slim

WORKDIR /collector

COPY ./collector/target/collector-*.jar /collector/collector.jar

CMD ["java", "-jar", "collector.jar"]