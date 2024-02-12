#!/bin/bash

jar -cmvf \
  ./build/tmp/jar/MANIFEST.MF \
  ./build/libs/java11-spring-gradle-bigquery-reference-${REVISION}.jar \
  ./build/classes/java/main/org/squidmin/java/spring/gradle/bigquery/Java17SpringGradleBigQueryReferenceApplication.class
