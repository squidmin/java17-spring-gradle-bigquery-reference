#!/bin/bash

jar -cmvf \
  ./build/tmp/jar/MANIFEST.MF \
  ./build/libs/java11-spring-gradle-bigquery-reference-0.0.1-SNAPSHOT.jar \
  ./build/classes/java/main/org/squidmin/java/spring/gradle/bigquery/JavaSpringGradleBigQueryReferenceApplication.class
