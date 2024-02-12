#!/bin/bash

./gradlew cleanTest test \
  --no-build-cache \
  --tests=org.squidmin.java.spring.gradle.bigquery.service.BigQueryServiceEndToEndTest.createDataset \
  -DAPP_PROFILE=integration \
  -DGCP_PROJECT_ID=${GCP_PROJECT_ID} \
  -DBQ_DATASET=${BQ_DATASET}
