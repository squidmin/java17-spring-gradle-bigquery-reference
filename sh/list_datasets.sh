#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.listDatasets \
  -DPROFILE=integration \
  -DGCP_DEFAULT_PROJECT_ID="lofty-root-378503"
