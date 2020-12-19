#!/bin/sh

set -e

docker-compose down

MAVEN_DIR="${HOME}/.m2"

docker run \
  --rm \
  -v ${MAVEN_DIR}:/root/.m2 \
  -v ${PWD}:/src \
  --entrypoint="sh" \
 maven:3-openjdk-15-slim \
  -c "mvn -f /src/pom.xml clean package"

docker-compose build

docker-compose up -d