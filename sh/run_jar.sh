#!/bin/bash

exec java -jar \
  -Dspring.profiles.active=local \
  -DGOOGLE_APPLICATION_CREDENTIALS=$GOOGLE_APPLICATION_CREDENTIALS \
  ./build/libs/java11-spring-gradle-bigquery-reference-0.0.1-SNAPSHOT.jar
