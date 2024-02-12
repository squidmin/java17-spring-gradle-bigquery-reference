#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.listDatasets \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID}
