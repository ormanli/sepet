FROM adoptopenjdk/openjdk15:alpine-jre

WORKDIR /collector

COPY ./collector/target/collector-*.jar /collector/collector.jar

CMD ["java", "-jar", "collector.jar"]