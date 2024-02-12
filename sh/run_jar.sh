#!/bin/bash

APP_PROFILE="$1"
GOOGLE_APPLICATION_CREDENTIALS="$2"
REVISION="$3"

exec java -jar \
  -Dspring.profiles.active=${APP_PROFILE} \
  -DGOOGLE_APPLICATION_CREDENTIALS=${GOOGLE_APPLICATION_CREDENTIALS} \
  ./build/libs/java17-spring-gradle-bigquery-reference-${REVISION}.jar
